package com.restAssured.JobsAPITest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.restAssured.BaseClass.BaseClass;
import com.restAssured.Utilities.ExcelDataProvider;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class TC_PutRequest_003 extends BaseClass {
	public static HashMap data = new HashMap();
	String jsonAsString;

	@BeforeClass
	void getRequest() throws InterruptedException {
		logger.info("**************TC003 PUT Request Start**************");
		RestAssured.basePath = "/Jobs";
	}

	@Test(dataProvider = "Putdata")
	void putJobsData(String ID, String jobTitle, String compName, String jobLocation, String jobType, String postTime,
			String description) throws IOException {
		data.put("Job Id", ID);
		data.put("Job Title", jobTitle);
		data.put("Job Company Name", compName);
		data.put("Job Location", jobLocation);
		data.put("Job Type", jobType);
		data.put("Job Posted time", postTime);
		data.put("Job Description", description);
		logger.info("PUT request Data Entered");
	}
	
	@Test(dependsOnMethods = { "putJobsData" })
	public void postJobsDetails() {
		Response response_put = given().body(data).when().put(BaseClass.BASE_URL);

		int StatusCodeCheck = response_put.getStatusCode();

		if (StatusCodeCheck == 200) {
			System.out.println("Status Code received successfully: " + StatusCodeCheck);
			String responsebody = response_put.getBody().asPrettyString();
			String reresponsebody = responsebody.replace("Nan", "Null");
			String response = reresponsebody.replace(",", "} {");

			response_put.then().assertThat().log().all();

			RestAssured.given().when().post().then()
					.body(JsonSchemaValidator.matchesJsonSchema(new File("./resources/jobsApi_get.json")));
			System.out.println("Put JsonSchema Validation successfully");
		} else if (StatusCodeCheck == 404) {
			System.out.println("JobId Not Found Status Code received : " + StatusCodeCheck);
		}
		assertEquals(response_put != null, true);

	}

	@AfterClass
	void tearDown() {
		logger.info("************** TC003 PUT Request END **************");
	}

	@DataProvider(name = "Putdata")
	String[][] putData() throws IOException {
		String path = System.getProperty("user.dir") + "/Test-Data/Data.xlsx";

		int rownum = ExcelDataProvider.getRowCount(path, "putdataSheet");
		int colcount = ExcelDataProvider.getCellCount(path, "putdataSheet", 1);

		String putdata[][] = new String[rownum][colcount];

		for (int i = 1; i <= rownum; i++) {
			for (int j = 0; j < colcount; j++) 
			{
				putdata[i - 1][j] = ExcelDataProvider.getCellData(path, "putdataSheet", i, j);
			}

		}
		return putdata;
	}

}
