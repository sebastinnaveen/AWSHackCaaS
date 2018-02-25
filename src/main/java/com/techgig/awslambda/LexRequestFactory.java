package com.techgig.awslambda;


import java.util.Date;
import java.util.Map;
import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;


public class LexRequestFactory {
    public static LexRequest createLexRequest(Map<String, Object> input) {
        Map<String, Object> botMap = (Map<String, Object>) input.get("bot");
        String botName = (String) botMap.get("name");
        System.out.println(botName);
        LexRequest lexRequest = new LexRequest();
        if(botName.equalsIgnoreCase("CarBookingBot")) {
        
        lexRequest.setBotName(botName);
        Map<String, Object> currentIntent = (Map<String, Object>) input.get("currentIntent");
        System.out.println( input.get("currentIntent"));
      //  lexRequest.setDepartmentName((String)currentIntent.get("name"));
        Map<String, Object> slots = (Map<String, Object>) currentIntent.get("slots");
        lexRequest.setPickupCity((String)slots.get("PickupCity"));
        lexRequest.setPickupDate((String)slots.get("PickupDate"));
        lexRequest.setReturnDate((String)slots.get("ReturnDate"));
        }
        else if(botName.equalsIgnoreCase("HealthBot")) {
        	Map<String, Object> currentIntent = (Map<String, Object>) input.get("currentIntent");
            System.out.println( input.get("currentIntent"));
            lexRequest.setBotName(botName);
          //  lexRequest.setDepartmentName((String)currentIntent.get("name"));
            Map<String, Object> slots = (Map<String, Object>) currentIntent.get("slots");
            lexRequest.setMathsGrade((String)slots.get("Mathsgrade"));
            lexRequest.setMathsArea((String)slots.get("MathsArea"));
          //  lexRequest.setReturnDate((String)slots.get("ReturnDate"));
            lexRequest.setVideo(loadVideo((String)slots.get("MathsArea")));
        	
        }
        
        else if(botName.equalsIgnoreCase("EduBot")) {
        	Map<String, Object> currentIntent = (Map<String, Object>) input.get("currentIntent");
            System.out.println( input.get("currentIntent"));
            if("Learnmaths".equalsIgnoreCase(String.valueOf(currentIntent.get("name")))) {
            lexRequest.setBotName(botName);
        
            Map<String, Object> slots = (Map<String, Object>) currentIntent.get("slots");
            lexRequest.setMathsGrade((String)slots.get("Mathsgrade"));
            lexRequest.setMathsArea((String)slots.get("MathsArea"));
            lexRequest.setVideo(loadVideo("https://youtu.be/49_TJymgXgM?autoplay=1"));
            }
            else if("Learnscience".equalsIgnoreCase(String.valueOf(currentIntent.get("name")))) {
                lexRequest.setBotName(botName);
            
                Map<String, Object> slots = (Map<String, Object>) currentIntent.get("slots");
                lexRequest.setMathsGrade((String)slots.get("ScienceArea"));
                lexRequest.setMathsArea((String)slots.get("Sciencegrade"));
                lexRequest.setVideo(loadVideo("https://youtu.be/49_TJymgXgM?autoplay=1"));
                }
            else if("Problemsolving".equalsIgnoreCase(String.valueOf(currentIntent.get("name")))) {
                lexRequest.setBotName(botName);
            
                Map<String, Object> slots = (Map<String, Object>) currentIntent.get("slots");
                lexRequest.setMathsGrade((String)slots.get("ProblemSubject"));
                lexRequest.setMathsArea((String)slots.get("Grade"));
                lexRequest.setVideo(loadVideo("https://youtu.be/49_TJymgXgM?autoplay=1"));
                }
          //  lexRequest.setReturnDate((String)slots.get("ReturnDate"));
           // lexRequest.setVideo(loadVideo((String)slots.get("MathsArea")));
        	
        }
        return lexRequest;
    }
    public static String loadVideo(String mathsArea) {
    	String resp="";
    try {
      
		   AIConfiguration configuration = new AIConfiguration("69385f2daae34c338ec2ff3cb6413936");

		    AIDataService dataService = new AIDataService(configuration);

		    
		  
		  

		          try {
		            AIRequest request = new AIRequest("video url");
		            
		           // AIServiceContext customContext = AIServiceContextBuilder.buildFromSessionId("566");
		            System.out.println("called before");
		            AIResponse response = dataService.request(request);
		            System.out.println("called after");
		            if (response.getStatus().getCode() == 200) {
		              System.out.println(response.getResult().getFulfillment().getSpeech());
		              System.out.println(response.getResult().getMetadata().getIntentName());
		              System.out.println(response.getResult().getResolvedQuery());
		              
		               resp = response.getResult().getFulfillment().getSpeech();
		            } else {
		              System.err.println(response.getStatus().getErrorDetails());
		            }
		          } catch (Exception ex) {
		            ex.printStackTrace();
		          }

		        
	   } catch (Exception e) {
	        e.printStackTrace();
	        return "error--->"+e.getMessage();
	     } finally {
	        
	     }
    
    return resp;
    
    }
    
//    public static void main(String[] args) {
//    	LexRequestFactory request = new LexRequestFactory();
//    	System.out.println(request.loadVideo(""));
//    }
}
