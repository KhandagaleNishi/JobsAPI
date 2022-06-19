package com.restAssured.BaseClass;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;

public class BaseClass {

public static Logger logger;
public static final String BASE_URL = ""; 
	
@BeforeClass
	public void setup() {
		RestAssured.baseURI = "";
		logger = Logger.getLogger("Rest Assured Jobs API Automation");
		PropertyConfigurator.configure("log4j.properties");
		logger.setLevel(Level.DEBUG);
	}
	
	public String randomeNum() {
	String generatedNum = RandomStringUtils.randomNumeric(4);
	return (generatedNum);
}
	
}
