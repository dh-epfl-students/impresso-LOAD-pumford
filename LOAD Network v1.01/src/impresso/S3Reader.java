package impresso;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Reader {

	
	public S3Reader() {
		
	}
	
	public S3Reader(String newspaperID, String year) throws IOException {
		
		Properties prop=new Properties();
		String propFilePath = "../resources/config.properties";
		
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(propFilePath);
			prop.load(inputStream);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String accessKey = System.getenv("s3Accesskey");
		String secretKey = System.getenv("s3Secretkey");

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		
		// Set S3 Client Endpoint

        AwsClientBuilder.EndpointConfiguration switchEndpoint = new AwsClientBuilder.EndpointConfiguration(
                prop.getProperty("s3BaseName"),"");
        
    	// Set signer type and http scheme
        ClientConfiguration conf = new ClientConfiguration();
        	    conf.setSignerOverride("S3SignerType");
		        conf.setProtocol(Protocol.HTTPS);
                
        AmazonS3 S3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(switchEndpoint)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(conf)
                .withPathStyleAccessEnabled(true)
                .build();
		
		String bucketName = "processed-canonical-data"; //Name of the bucket
        String prefix = "linguistic-processing/2020-03-11/";
        String keySuffix = ".ling.annotation.jsonl.bz2";
        
        S3Object fullObject = null;
        try{
	        String key = prefix + newspaperID + "-" + year + keySuffix; 
        	GetObjectRequest object_request = new GetObjectRequest(bucketName, key);
	        fullObject = S3Client.getObject(object_request);
            displayTextInputStream(fullObject.getObjectContent());
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
        finally {
            // To ensure that the network connection doesn't remain open, close any open input streams.
            if (fullObject != null) {
                fullObject.close();
            }
        }

	}
	
	public ImpressoContentItem injectLingusticAnnotations(ImpressoContentItem contentItem) {
		
		
		return contentItem;
	}
	
	private static void displayTextInputStream(InputStream input) throws IOException {
      // Read the text input stream one line at a time and display each line.
		Scanner fileIn = new Scanner(new  BZip2CompressorInputStream(input));
	    if (null != fileIn) {
	        //while (fileIn.hasNext()) {
	        	JSONObject jsonObj = new JSONObject(fileIn.nextLine());
	        	JSONArray sents = jsonObj.getJSONArray("sents");
	        	int length = sents.length();
	        	for(int j=0; j<length; j++) {
	        	    JSONObject sentence = sents.getJSONObject(j);
	        	    JSONArray toks = sentence.getJSONArray("toks");
		        	int sent_length = sents.length();
		        	for(int k=0; j<sent_length; k++) {
		        	    JSONObject tok = sentence.getJSONObject(k);
		        	    
		        	  }
	        	  }
	        //}
	    }
  }
}
