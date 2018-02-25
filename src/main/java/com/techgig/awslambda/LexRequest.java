package com.techgig.awslambda;

import java.util.Date;

public class LexRequest {
    private String botName;
    private String intentName;
   
    public String getPickupCity() {
		return pickupCity;
	}

	public void setPickupCity(String pickupCity) {
		this.pickupCity = pickupCity;
	}

	

	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public int getDriverAge() {
		return driverAge;
	}

	public void setDriverAge(int driverAge) {
		this.driverAge = driverAge;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	private String pickupCity;
    private String pickupDate;
    private String returnDate;
    private int driverAge;
    private String carType;

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotName() {
        return botName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public String getIntentName() {
        return intentName;
    }
    public String getMathsGrade() {
		return mathsGrade;
	}

	public void setMathsGrade(String mathsGrade) {
		this.mathsGrade = mathsGrade;
	}

	public String getMathsArea() {
		return mathsArea;
	}

	public void setMathsArea(String mathsArea) {
		this.mathsArea = mathsArea;
	}

	private String mathsGrade;
    private String mathsArea;
   public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

private String video;

   
}
