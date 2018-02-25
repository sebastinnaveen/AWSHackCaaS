package com.example.demo.model;

import java.util.List;

public class Fulfillment
{
    private String speech;

    private List<Messages> messages;

    public String getSpeech ()
    {
        return speech;
    }

    public void setSpeech (String speech)
    {
        this.speech = speech;
    }


    public List<Messages> getMessages() {
		return messages;
	}

	public void setMessages(List<Messages> messages) {
		this.messages = messages;
	}

	@Override
    public String toString()
    {
        return "ClassPojo [speech = "+speech+", messages = "+messages+"]";
    }
}