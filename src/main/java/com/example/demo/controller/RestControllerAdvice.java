package com.example.demo.controller;

import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class RestControllerAdvice {
    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(",", false, false));
    }
}
