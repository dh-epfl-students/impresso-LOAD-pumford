package impresso;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Reader {

	
	public S3Reader() {
		
	}
	
	public S3Reader(String newspaperID, String year) {
		
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
        
        ListObjectsV2Request req = new
        ListObjectsV2Request().withBucketName(bucketName).withPrefix(prefix).withDelimiter("/");
        ListObjectsV2Result result = S3Client.listObjectsV2(req);
        
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) 
        {
      	  
      	  
          System.out.println(" --- " + objectSummary.getKey() +" "
          + "(size = " + objectSummary.getSize() + ")" +" "
          + "(eTag = " + objectSummary.getETag() + ")");
          System.out.println();
          
          GetObjectRequest object_request = new GetObjectRequest(bucketName, objectSummary.getKey());
          S3Client.getObject(object_request);
        }

        
        
        /*
        //Example for 
        InputStream fin = Files.newInputStream(Paths.get("archive.tar.bz2"));
        BufferedInputStream in = new BufferedInputStream(fin);
        OutputStream out = Files.newOutputStream(Paths.get("archive.tar"));
        BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
        final byte[] buffer = new byte[prop.getProperty("bufferSize")];
        int n = 0;
        while (-1 != (n = bzIn.read(buffer))) {
            out.write(buffer, 0, n);
        }
        out.close();
        bzIn.close();*/
	}
}
