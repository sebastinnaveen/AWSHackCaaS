package com.example.demo.controller;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.RequestPOJO;
import com.example.demo.model.chatInformation;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



@RestController
@RequestMapping("/chatbot")
public class chatController {

	chatInformation ochat = new chatInformation();
	@RequestMapping(value="/{chatInput}",method=RequestMethod.POST,produces={MediaType.APPLICATION_JSON_VALUE})
	public String getResponse(@RequestBody RequestPOJO requestPOJO) throws ParseException,IOException	{
		String response = "",responseOutput="";
		Gson gson =null,reqGson=null;
		String requestBodyFromApiAi="~~~";
		String requestActionFromApiAi="",responseFromApigee="";
		try {
			//requestBodyFromApiAi=getBody(request);
			requestBodyFromApiAi=requestPOJO.getResult().getAction();
			System.out.println("^^^^^^^^^^requestBodyFromApiAi"+requestBodyFromApiAi+"^^^^^^^^^^^^");
			
			System.out.println("^^^^^^^^^^requestActionFromApiAi"+requestBodyFromApiAi+"^^^^^^^^^^^^");
			
			
			  System.setProperty("java.net.useSystemProxies", "true"); 
				URL url = new URL("https://apibaas-trial.apigee.net/ramorg/testapp/"+requestBodyFromApiAi);//+requestActionFromApiAi+"s");//+requestActionFromApiAi);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

				
				String output = "";
//				Gson reqGSON
				while ((output = br.readLine()) != null) {
					//System.out.println(output);
					response +=output;
					
					//return  output;
				}
				System.out.println("^^^^^^^^^^response From Apigee"+response+"^^^^^^^^^^^^");
				JsonParser parser = new JsonParser();
				JsonObject json = new JsonObject();
				json = (JsonObject) parser.parse(""+response);
				 JsonArray jsonArray = (JsonArray) json.get("entities");
				//responseFromApigee=""+json.get("entities");
					json = (JsonObject) parser.parse(""+jsonArray.get(0));
					responseFromApigee=""+json.get("desc");
				 System.out.println("^^^^^^^^^^entities from Apigee"+responseFromApigee+"^^^^^^^^^^^^");
				
				
				chatInformation ci = new chatInformation();
				//ci.setData((JsonObject) json.get("desc"));
				ci.setSpeech("speech from vz api");
				ci.setDisplayText("displayText from vz api");
				ci.setSource("source from vz api");
				gson= new Gson();
				responseOutput=gson.toJson(ci);

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			  }
		System.out.println("@@@@@@@"+responseOutput+"@@@@");
		return responseOutput;		
	}
	
	public String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}

}
//vzcustomerservice.contact
//vzoffer.offers
//vzinstallation.install
//"{"id":"d579a226-5db7-4d70-a822-38cdf32ec330","timestamp":"2017-02-28T09:46:32.779Z","lang":"en","result":{"source":"agent","resolvedQuery":"iOS installation","speech":"","action":"vzinstallation.install","actionIncomplete":false,"parameters":{},"contexts":[],"metadata":{"intentId":"0e289ee5-5006-4624-9a36-1aa0e7126eaa","webhookUsed":"true","webhookForSlotFillingUsed":"false","intentName":"VzInstallation"},"fulfillment":{"speech":"installation","messages":[{"type":0,"speech":"installation"}]},"score":0.9},"status":{"code":200,"errorType":"success"},"sessionId":"171b1f54-a14b-4cc7-9747-67001046902f","originalRequest":null}"
		/*
		 * {
  "lang": "en", 
  "status": {
      "errorType": "success", 
      "code": 200
  }, 
  "timestamp": "2017-02-09T16:06:01.908Z", 
  "sessionId": "1486656220806", 
  "result": {
      "parameters": {
          "city": "Rome", 
          "name": "Ana"
      }, 
      "contexts": [], 
      "resolvedQuery": "my name is Ana and I live in Rome", 
      "source": "agent", 
      "score": 1.0, 
      "speech": "", 
      "fulfillment": {
          "messages": [
              {
                  "speech": "Hi Ana! Nice to meet you!", 
                  "type": 0
              }
          ], 
          "speech": "Hi Ana! Nice to meet you!"
      }, 
      "actionIncomplete": false, 
      "action": "greetings", 
      "metadata": {
          "intentId": "9f41ef7c-82fa-42a7-9a30-49a93e2c14d0", 
          "webhookForSlotFillingUsed": "false", 
          "intentName": "greetings", 
          "webhookUsed": "true"
      }
  }, 
  "id": "ab30d214-f4bb-4cdd-ae36-31caac7a6693", 
  "originalRequest": {
      "source": "google", 
      "data": {
          "inputs": [
              {
                  "raw_inputs": [
                      {
                          "query": "my name is Ana and I live in Rome", 
                          "input_type": 2
                      }
                  ], 
                  "intent": "assistant.intent.action.TEXT", 
                  "arguments": [
                      {
                          "text_value": "my name is Ana and I live in Rome", 
                          "raw_text": "my name is Ana and I live in Rome", 
                          "name": "text"
                      }
                  ]
              }
          ], 
          "user": {
              "user_id": "PuQndWs1OMjUYwVJMYqwJv0/KT8satJHAUQGiGPDQ7A="
          }, 
          "conversation": {
              "conversation_id": "1486656220806", 
              "type": 2, 
              "conversation_token": "[]"
          }
      }
  }
}
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * */
