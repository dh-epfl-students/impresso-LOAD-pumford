package impresso;

import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class S3Reader {
	private static Cache<String, JSONObject> newspaperCache;
	private static Cache<String, JSONObject> entityCache;
	private static Properties prop;
	private String year = null;
	private static String bucketName;
	
	public S3Reader() {
		
	}
	
	public S3Reader(String newspaperID, String year, Properties prop) throws IOException {
		
		String accessKey = System.getenv("s3Accesskey");
		String secretKey = System.getenv("s3Secretkey");
		int readTimeout = 100000; //Doubles default timeout
		
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		
		// Set S3 Client Endpoint

        AwsClientBuilder.EndpointConfiguration switchEndpoint = new AwsClientBuilder.EndpointConfiguration(
                prop.getProperty("s3BaseName"),"");
        
    	// Set signer type and http scheme
        ClientConfiguration conf = new ClientConfiguration();
        	    conf.setSignerOverride("S3SignerType");
        	    conf.setSocketTimeout(readTimeout);
		        conf.setProtocol(Protocol.HTTPS);
                
        AmazonS3 S3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(switchEndpoint)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(conf)
                .withPathStyleAccessEnabled(true)
                .build();
        
		bucketName = prop.getProperty("s3BucketName"); //Name of the bucket
        String prefix = prop.getProperty("s3Prefix"); //Name of prefix for S3
        String keySuffix = prop.getProperty("s3KeySuffix"); //Suffix of each BZIP2 
        
        //Creation of a cache
        newspaperCache = CacheBuilder.newBuilder().build();
        
        try{
        	if(year != null) {	
    	        String newspaperKey = prefix + newspaperID + "-" + year + keySuffix;
    	        String entityKey = "mysql-mention-dumps/NZZ/" + newspaperID + "-" + year + "-mentions.jsonl.bz2";
                populateCache(newspaperKey, entityKey, S3Client);
        	}
        	else {
        		String curPrefix = prefix+newspaperID; //Creates the prefix to search for
        		ObjectListing listing = S3Client.listObjects(bucketName, curPrefix);
        		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        		for(S3ObjectSummary summary:summaries) {
        			String key = summary.getKey();
        			System.out.println(key);
                    populateCache(key, null, S3Client);
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
	
	public ImpressoContentItem injectLingusticAnnotations(ImpressoContentItem contentItem) {
		String tempId = contentItem.getId();
		JSONObject jsonObj = newspaperCache.getIfPresent(tempId);
    	JSONArray sents = jsonObj.getJSONArray("sents");
    	int length = sents.length();
    	int totalOffset = 0; //Keeps track of the total offset
    	for(int j=0; j<length; j++) {
    	    JSONObject sentence = sents.getJSONObject(j);
    	    //This is where the injectTokens of a ImpressoContentItem
    	    totalOffset += contentItem.injectTokens(sentence.getJSONArray("tok"), sentence.getString("lg"), true, totalOffset);
    	}

		/*
		 * WHILE THE ENTITIES ARE BEING DUMPED TO THE S3 BUCKET
		 * SHOULD NOT EXIST IN THE FINAL IMPLEMENTATION
		 */
    	
    	jsonObj = entityCache.getIfPresent(tempId);
    	JSONArray mentions = jsonObj.getJSONArray("mentions");
    	length = sents.length();
    	for(int j=0; j<length; j++) {
    	    JSONObject mention = mentions.getJSONObject(j);
    	    //This is where the injectAnnotations of a ImpressoContentItem
    	    contentItem.injectTokens(mentions, null, false, 0);
    	}
    	
		return contentItem;
	}

	
	private static void populateCache(String newspaperKey, String entityKey, AmazonS3 S3Client) throws IOException {
  	    GetObjectRequest object_request = new GetObjectRequest(bucketName, newspaperKey);
	    S3Object fullObject = S3Client.getObject(object_request);
		
		try (Scanner fileIn = new Scanner(new  BZip2CompressorInputStream(fullObject.getObjectContent()))) {
    	  //First download the key
		  // Read the text input stream one line at a as a json object and parse this object into contentitems	      
		  if (null != fileIn) {
			  while (fileIn.hasNext()) {
			      JSONObject jsonObj = new JSONObject(fileIn.nextLine());
			      newspaperCache.put(jsonObj.getString("id"), jsonObj);
			    }
			}
		}
        finally {
            // To ensure that the network connection doesn't remain open, close any open input streams.
            if (fullObject != null) {
                fullObject.close();
            }
        }
        
		/*
		 * WHILE THE ENTITIES ARE BEING DUMPED TO THE S3 BUCKET
		 * SHOULD NOT EXIST IN THE FINAL IMPLEMENTATION
		 */
		
		if(entityKey != null) {
	  	    object_request = new GetObjectRequest("TRANSFER", entityKey);
		    fullObject = S3Client.getObject(object_request);
			try (Scanner fileIn = new Scanner(new  BZip2CompressorInputStream(fullObject.getObjectContent()))) {
		    	  //First download the key
				  // Read the text input stream one line at a as a json object and parse this object into contentitems	      
				  if (null != fileIn) {
					  while (fileIn.hasNext()) {
					      JSONObject jsonObj = new JSONObject(fileIn.nextLine());
					      entityCache.put(jsonObj.getString("id"), jsonObj);
					    }
					}
				}
		        finally {
		            // To ensure that the network connection doesn't remain open, close any open input streams.
		            if (fullObject != null) {
		                fullObject.close();
		            }
		        }
		}
		
    }
	
}
