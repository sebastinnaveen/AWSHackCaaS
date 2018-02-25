package com.example.demo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
//import com.apigee.flow.execution.ExecutionContext;
//import com.apigee.flow.execution.ExecutionResult;
//import com.apigee.flow.execution.spi.Execution;
//import com.apigee.flow.message.MessageContext;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.geronimo.mail.util.Hex;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.slack.SlackApi;
import com.example.demo.slack.SlackMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController

public class AwsLexController {
	HttpURLConnection connection = null;
	@RequestMapping(value ="/awsbot", method = RequestMethod.GET)
	public String lexResponse(@RequestParam(value="inputtext", defaultValue="Welcome to aws chatbot") String inputtext,@RequestParam(value="botname", defaultValue="HealthBot") String botname,@RequestParam(value="botalias", defaultValue="HealthBot") String botalias) {
		String responseText="";
		try {
            final String method = "POST";
            final String service = "lex";
            final String host = "runtime.lex.us-east-1.amazonaws.com";
            final String region = "us-east-1";
            final String endpoint = "https://runtime.lex.us-east-1.amazonaws.com/";

            final String accessKey = "AKIAJ4KB2LVJ5GLEBEUQ";// Put here user's AWS_ACCESS_KEY_ID
            final String secretKey = "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";// Put here user's AWS_SECRET_ACCESS_KEY
            // Check out "Best Practices for Managing AWS Access Keys":
            // http://docs.aws.amazon.com/general/latest/gr/aws-access-keys-best-practices.html

            final String botName = "HealthBot";
            final String botAlias = "healthbot";
            final String userId = "Lex-user";//some user id, which will be sent in a Lex request field "userId"
            final String postAction = "text";
            // for stream request (text or audio stream)
            //postAction = "content"

            // POST requests use a content type header. For Lex,
            // the content is JSON or stream.
            // for postAction = "text":
            final String contentType = "application/json";
            // for postAction = "content", audio stream in PCM format:
            //contentType = "audio/l16;rate=16000;channels=1"
            //contentType = "audio/x-l16;sample-rate=16000;channel-count=1"
            // for postAction = "content", audio stream in Opus format:
            //contentType = "audio/x-cbr-opus-with-preamble;preamble-size=0;bit-rate=256000;frame-size-milliseconds=4"
            // for postAction = "content", text:
            //contentType = "text/plain;charset=utf-8"

            final String canonicalUri = String.format("/bot/%s/alias/%s/user/%s/%s/", botname, botAlias, userId, postAction);//notice the leading "/"
            final String canonicalQueryString = "";//optional - usually not used in REST API-s
            final String signedHeaders = "content-type;host;x-amz-date";
            final String algorithm = "AWS4-HMAC-SHA256";
            System.out.println("canonicalUri."+canonicalUri);
            final List<Object> providers = new ArrayList<>();
            providers.add(new JacksonJaxbJsonProvider());

            final WebClient client = WebClient.create(endpoint, providers);
            client.accept(contentType)
                    .type(contentType)
                    .path(canonicalUri);

            System.out.println("inputtext."+inputtext);
            System.out.println("botname."+botname);
            System.out.println("botalias."+botalias);
            System.out.println("orig botName."+botName);
          //  final Scanner scanner = new Scanner(System.in);
          // while(true) {
               // String requestText = scanner.nextLine().trim();
              //  if(isEmpty(requestText))
              //      break;
            String requestText ="Book a car";

                final String requestParameters = String.format("{\"inputText\": \"%s\", \"sessionAttributes\": {\"attr_name\" : \"value\"}}", "learn maths");

                final String payloadHash = hexEncode(sha256Hash(requestParameters));
                final ZonedDateTime utcNow = Instant.now().atZone(ZoneOffset.UTC);// Date for headers and the credential string
                final String amzDate = utcNow.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
                final String dateStamp = utcNow.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                final String canonicalHeaders = String.format("content-type:%s\nhost:%s\nx-amz-date:%s\n", contentType, host, amzDate);
                final String canonicalRequest = String.format("%s\n%s\n%s\n%s\n%s\n%s", method, canonicalUri, canonicalQueryString, canonicalHeaders, signedHeaders, payloadHash);
                final String credentialScope = String.format("%s/%s/%s/aws4_request", dateStamp, region, service);
                final String canonicalRequestHash = hexEncode(sha256Hash(canonicalRequest));
                final String stringToSign = String.format("%s\n%s\n%s\n%s", algorithm, amzDate, credentialScope, canonicalRequestHash);
                final byte[] signatureKey = getSignatureKey(secretKey, dateStamp, region, service);
                final String signature = hexEncode(HmacSHA256(stringToSign, signatureKey));
                final String authorizationHeader = String.format("%s Credential=%s/%s, SignedHeaders=%s, Signature=%s", algorithm, accessKey, credentialScope, signedHeaders, signature);
                final MetadataMap<String, String> headersMap = new MetadataMap<>();
                headersMap.add("Content-Type", contentType);
                headersMap.add("X-Amz-Date", amzDate);
                headersMap.add("Authorization", authorizationHeader);
                client.headers(headersMap);

                Response response = client.post(requestParameters);
                InputStream responseStream = (InputStream) response.getEntity();
//                System.out.println(String.format("%s;\t x-amzn-RequestId: %s", Instant.now().atZone(ZoneOffset.UTC),
//                                                    response.getMetadata().get("x-amzn-RequestId")));
                if(response.getStatus() == 200) {
                    JsonNode jsonNode = new ObjectMapper().readTree(responseStream);
                    String dialogState = jsonNode.get("dialogState").asText("");
                    if(dialogState.startsWith("Elicit")) {
                        System.out.println(jsonNode.get("message").asText(""));
                    responseText=jsonNode.get("message").asText("");
                    }
                    else if(dialogState.startsWith("ReadyForFulfillment")) {
                        System.out.println(String.format("ReadyForFulfillment; Intent: %s; Slots: %s", jsonNode.get("intentName").asText(""), jsonNode.get("slots").toString()));
                        responseText=String.format("ReadyForFulfillment; Intent: %s; Slots: %s", jsonNode.get("intentName").asText(""), jsonNode.get("slots").toString());
                    }
                } else {
                    JsonNode errorMessage = new ObjectMapper().readTree(responseStream).get("message");
                    if (errorMessage != null)
                        System.out.println(errorMessage);
                    List<Object> xAmznErrorType = response.getMetadata().get("x-amzn-ErrorType");
                    if (!xAmznErrorType.isEmpty())
                        System.out.println(xAmznErrorType.get(0));
                    responseText=(String)xAmznErrorType.get(0);
                    List<Object> amznRequestId = response.getMetadata().get("x-amzn-RequestId");
                    if (!amznRequestId.isEmpty())
                        System.out.println(amznRequestId.get(0));
                }
         //   }
                SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
                api.call(new SlackMessage("#aws-bot", null, responseText));
                //slackapi(responseText);
                
            System.out.println("Bye.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return responseText;
    }@RequestMapping(value ="/awsbot", method = RequestMethod.POST)
	public JsonNode lexResponsePost(@RequestParam(value="text", defaultValue="Welcome to aws chatbot") String text) {
		String responseText="";
		JsonNode jsonNode =null;
		try {
            final String method = "POST";
            final String service = "lex";
            final String host = "runtime.lex.us-east-1.amazonaws.com";
            final String region = "us-east-1";
            final String endpoint = "https://runtime.lex.us-east-1.amazonaws.com/";

            final String accessKey = "AKIAJ4KB2LVJ5GLEBEUQ";// Put here user's AWS_ACCESS_KEY_ID
            final String secretKey = "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";// Put here user's AWS_SECRET_ACCESS_KEY
            // Check out "Best Practices for Managing AWS Access Keys":
            // http://docs.aws.amazon.com/general/latest/gr/aws-access-keys-best-practices.html

            final String botName = "EduBot";
            final String botAlias = "edubot";
            final String userId = "Lex-user";//some user id, which will be sent in a Lex request field "userId"
            final String postAction = "text";
            // for stream request (text or audio stream)
            //postAction = "content"

            // POST requests use a content type header. For Lex,
            // the content is JSON or stream.
            // for postAction = "text":
            final String contentType = "application/json";
            // for postAction = "content", audio stream in PCM format:
            //contentType = "audio/l16;rate=16000;channels=1"
            //contentType = "audio/x-l16;sample-rate=16000;channel-count=1"
            // for postAction = "content", audio stream in Opus format:
            //contentType = "audio/x-cbr-opus-with-preamble;preamble-size=0;bit-rate=256000;frame-size-milliseconds=4"
            // for postAction = "content", text:
            //contentType = "text/plain;charset=utf-8"

            final String canonicalUri = String.format("/bot/%s/alias/%s/user/%s/%s/", botName, botAlias, userId, postAction);//notice the leading "/"
            final String canonicalQueryString = "";//optional - usually not used in REST API-s
            final String signedHeaders = "content-type;host;x-amz-date";
            final String algorithm = "AWS4-HMAC-SHA256";
            System.out.println("canonicalUri."+canonicalUri);
            final List<Object> providers = new ArrayList<>();
            providers.add(new JacksonJaxbJsonProvider());

            final WebClient client = WebClient.create(endpoint, providers);
            client.accept(contentType)
                    .type(contentType)
                    .path(canonicalUri);

            System.out.println("inputtext."+text);

            String requestText ="Book a car";

                final String requestParameters = String.format("{\"inputText\": \"%s\", \"sessionAttributes\": {\"attr_name\" : \"value\"}}", text);

                final String payloadHash = hexEncode(sha256Hash(requestParameters));
                final ZonedDateTime utcNow = Instant.now().atZone(ZoneOffset.UTC);// Date for headers and the credential string
                final String amzDate = utcNow.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
                final String dateStamp = utcNow.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                final String canonicalHeaders = String.format("content-type:%s\nhost:%s\nx-amz-date:%s\n", contentType, host, amzDate);
                final String canonicalRequest = String.format("%s\n%s\n%s\n%s\n%s\n%s", method, canonicalUri, canonicalQueryString, canonicalHeaders, signedHeaders, payloadHash);
                final String credentialScope = String.format("%s/%s/%s/aws4_request", dateStamp, region, service);
                final String canonicalRequestHash = hexEncode(sha256Hash(canonicalRequest));
                final String stringToSign = String.format("%s\n%s\n%s\n%s", algorithm, amzDate, credentialScope, canonicalRequestHash);
                final byte[] signatureKey = getSignatureKey(secretKey, dateStamp, region, service);
                final String signature = hexEncode(HmacSHA256(stringToSign, signatureKey));
                final String authorizationHeader = String.format("%s Credential=%s/%s, SignedHeaders=%s, Signature=%s", algorithm, accessKey, credentialScope, signedHeaders, signature);
                final MetadataMap<String, String> headersMap = new MetadataMap<>();
                headersMap.add("Content-Type", contentType);
                headersMap.add("X-Amz-Date", amzDate);
                headersMap.add("Authorization", authorizationHeader);
                client.headers(headersMap);

                Response response = client.post(requestParameters);
                InputStream responseStream = (InputStream) response.getEntity();
                System.out.println(String.format("%s;\t x-amzn-RequestId: %s", Instant.now().atZone(ZoneOffset.UTC),
                                                    response.getMetadata().get("x-amzn-RequestId")));
                System.out.println("response.getStatus()."+response.getStatus());
                if(response.getStatus() == 200) {
                     jsonNode = new ObjectMapper().readTree(responseStream);
                    
                    String dialogState = jsonNode.get("dialogState").asText("");
                    System.out.println("dialogState---->"+dialogState);
                    if(dialogState.startsWith("Elicit")) {
                        System.out.println(jsonNode.get("message").asText(""));
                    responseText=jsonNode.get("message").asText("");
                    }
                    else if(dialogState.startsWith("ReadyForFulfillment")) {
                        System.out.println(String.format("ReadyForFulfillment; Intent: %s; Slots: %s", jsonNode.get("intentName").asText(""), jsonNode.get("slots").toString()));
                        responseText=String.format("ReadyForFulfillment; Intent: %s; Slots: %s", jsonNode.get("intentName").asText(""), jsonNode.get("slots").toString());
                    }
                    else if(dialogState.startsWith("Fulfilled")) {
                        System.out.println(String.format("Fulfilled; Intent: %s; Slots: %s", jsonNode.get("intentName").asText(""), jsonNode.get("slots").toString()));
                        responseText=String.format("Fulfilled; Intent: %s; Slots: %s", jsonNode.get("intentName").asText(""), jsonNode.get("slots").toString());
                    }
                } else {
                    JsonNode errorMessage = new ObjectMapper().readTree(responseStream).get("message");
                    if (errorMessage != null)
                        System.out.println(errorMessage);
                    List<Object> xAmznErrorType = response.getMetadata().get("x-amzn-ErrorType");
                    if (!xAmznErrorType.isEmpty())
                        System.out.println(xAmznErrorType.get(0));
                    responseText=(String)xAmznErrorType.get(0);
                    List<Object> amznRequestId = response.getMetadata().get("x-amzn-RequestId");
                    if (!amznRequestId.isEmpty())
                        System.out.println(amznRequestId.get(0));
                }
         //   }
                System.out.println("responseText--->"+responseText);
                SlackApi api = new SlackApi("https://hooks.slack.com/services/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");
//                final String payload = "payload="
//   	                 + URLEncoder.encode("{\"channel\":\"#aws-bot\",\"username\":\"Sizzler-DevOps\",\"text\":\"Hubot message from Naveen #devops\",\"icon_emoji\":\":happy:\"}", "UTF-8");
//                api.ca
                api.call(new SlackMessage("#aws-bot", null, responseText));
                //slackapi(responseText);
                
            System.out.println("Bye.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return jsonNode;
    }
	
	
	private static byte[] sha256Hash(String value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return MessageDigest.getInstance("SHA-256").digest(value.getBytes("UTF8"));
    }

    private static String hexEncode(byte[] data) throws Exception {
        return new String(Hex.encode(data));
    }

    //Source: http://docs.aws.amazon.com/general/latest/gr/signature-v4-examples.html#signature-v4-examples-java
    static byte[] HmacSHA256(String data, byte[] key) throws Exception {
        String algorithm="HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF8"));
    }

    static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
        byte[] kDate = HmacSHA256(dateStamp, kSecret);
        byte[] kRegion = HmacSHA256(regionName, kDate);
        byte[] kService = HmacSHA256(serviceName, kRegion);
        byte[] kSigning = HmacSHA256("aws4_request", kService);
        return kSigning;
    }
    
    public void connectApigee() {
    	
    }
    
    public void slackapi(String responsetext) {
 	   try {
 	         // Create connection
 	         final URL url = new URL("https://hooks.slack.com/services/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");
 	         //https://hooks.slack.com/services/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
 	         connection = (HttpURLConnection) url.openConnection();
 	         connection.setRequestMethod("POST");
 	         connection.setConnectTimeout(5000);
 	         connection.setUseCaches(false);
 	         connection.setDoInput(true);
 	         connection.setDoOutput(true);
 	        connection.setRequestProperty("Content-Type", "application/json");

 	        final String payload = "payload="
	                 + URLEncoder.encode("{\"channel\":\"#aws-bot\",\"username\":\"Sizzler-DevOps\",\"text\":\"{\"dialogState\":\"ElicitSlot\",\"intentName\":\"Learnmaths\",\"message\":\"What is your grade\",\"messageFormat\":\"PlainText\",\"responseCard\":null,\"sessionAttributes\":{\"attr_name\":\"value\"},\"slotToElicit\":\"Mathsgrade\",\"slots\":{\"MathsArea\":null,\"Mathsgrade\":null}}\",\"icon_emoji\":\":happy:\"}", "UTF-8");

 	         // Send request
 	         final DataOutputStream wr = new DataOutputStream(
 	                 connection.getOutputStream());
 	         wr.writeBytes(payload);
 	         wr.flush();
 	         wr.close();
 	        // System.out.println(sampleProperty.getStringProp1());
 	         // Get Response
 	         final InputStream is = connection.getInputStream();
 	         final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
 	         String line;
 	         StringBuilder response = new StringBuilder();
 	         while ((line = rd.readLine()) != null) {
 	             response.append(line);
 	             response.append('\n');
 	         }

 	         rd.close();
 	        // return response.toString();
 	     } catch (Exception e) {
 	        e.printStackTrace();
 	        //return "error--->"+e.getMessage();
 	     } finally {
 	         if (connection != null) {
 	             connection.disconnect();
 	         }
 	     }
    // return "Successfully Posted in Slack!";

 
}

}
