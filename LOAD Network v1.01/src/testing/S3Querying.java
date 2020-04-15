package testing;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import com.amazonaws.*;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.util.StringUtils;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.annotation.*;

public class S3Querying {

	public static void main(String[] args) {
		String accessKey = "XXXX";
		String secretKey = "XXXX";

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		
		// Set S3 Client Endpoint to Symphony

        AwsClientBuilder.EndpointConfiguration symphonyEndpoint = new AwsClientBuilder.EndpointConfiguration(
                "https://os.zhdk.cloud.switch.ch","");
        
    	// Set signer type and http scheme
        ClientConfiguration conf = new ClientConfiguration();
        	    conf.setSignerOverride("S3SignerType");
		        conf.setProtocol(Protocol.HTTPS);
                
        AmazonS3 S3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(symphonyEndpoint)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(conf)
                .withPathStyleAccessEnabled(true)
                .build();
		
        System.out.println("===========================================");
        System.out.println(" Connection to the S3 ");
        System.out.println("===========================================\n");
        
        try { 
            /*
            * List of buckets and objects in our account
            */
            System.out.println("Listing buckets and objects");
            for (Bucket bucket : S3Client.listBuckets())
            {
              System.out.println(" - " + bucket.getName() +" "
                + "(owner = " + bucket.getOwner()
                + " "
                + "(creationDate = " + bucket.getCreationDate());
              ObjectListing objectListing = S3Client.listObjects(new ListObjectsRequest()
                .withBucketName(bucket.getName()));
              for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) 
              {
                System.out.println(" --- " + objectSummary.getKey() +" "
                + "(size = " + objectSummary.getSize() + ")" +" "
                + "(eTag = " + objectSummary.getETag() + ")");
                System.out.println();
              }
            }
          }
          catch (AmazonServiceException ase)
          {
            System.out.println("Caught an AmazonServiceException, which means your request made it to S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code: " + ase.getErrorCode());
            System.out.println("Error Type: " + ase.getErrorType());
            System.out.println("Request ID: " + ase.getRequestId());
          }
          catch (AmazonClientException ace)
          {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
            + "a serious internal problem while trying to communicate with S3 such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
          }

	}

}
