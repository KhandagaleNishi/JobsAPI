package com.restAssured.JobsAPITest;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.restAssured.BaseClass.BaseClass;
import com.restAssured.Utilities.ExcelDataProvider;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;


public class TC_GetRequest_001 {
	public static HashMap data = new HashMap();
@Test
	public void getJobsDetials() {
	Response response_get  =	
			given()
		.when()
		.get(BaseClass.BASE_URL);

		
	int StatusCodeCheck = response_get.getStatusCode();

	if (StatusCodeCheck == 200) {
		System.out.println("Status Code received successfully: " + StatusCodeCheck);
		String responsebody = response_get.getBody().asPrettyString();
		String reresponsebody = responsebody.replace("Nan", "Null");
		String response = reresponsebody.replace(",","} {");
		
		response_get.then().assertThat().log().all();
		
		RestAssured.given().when().then().body(JsonSchemaValidator.matchesJsonSchema(new File("./resources/jobsApi_get.json")));
		System.out.println("Get All JsonSchema Validation successfully");
	} 
	else if(StatusCodeCheck == 404) {
		System.out.println("JobID Not Found Status Code received: " + StatusCodeCheck);
	}
	assertEquals(response_get != null, true);
	}
@Test(dataProvider = "getData")
void getJobsData(String ID) {
	data.put("Job Id", ID);
	
}

@Test(dependsOnMethods = { "getJobsData" })
public void get1JobsDetails() {
Response response_get  =	
		given()
		.body(data)
	.when()
	.get(BaseClass.BASE_URL);

	
int StatusCodeCheck = response_get.getStatusCode();

if (StatusCodeCheck == 200) {
	System.out.println("Status Code received successfully: " + StatusCodeCheck);
	String responsebody = response_get.getBody().asPrettyString();
	String reresponsebody = responsebody.replace("Nan", "Null");
	String response = reresponsebody.replace(",","} {");
	
	response_get.then().assertThat().log().all();
	
	RestAssured.given().when().then().body(JsonSchemaValidator.matchesJsonSchema(new File("./resources/jobsApi_get.json")));
	System.out.println("Get One JsonSchema Validation successfully");
} 
assertEquals(response_get != null, true);
}

@DataProvider(name = "getData")
String[][] putData() throws IOException {
	String path = System.getProperty("user.dir") + "/Test-Data/Data.xlsx";

	int rownum = ExcelDataProvider.getRowCount(path, "get");
	int colcount = ExcelDataProvider.getCellCount(path, "get", 1);

	String deleteData[][] = new String[rownum][colcount];

	for (int i = 1; i <= rownum; i++) {
		for (int j = 0; j < colcount; j++) 
		{
			deleteData[i - 1][j] = ExcelDataProvider.getCellData(path, "get", i, j);
		}
	}
	return deleteData;
}

}
