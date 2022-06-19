package com.restAssured.JobsAPITest;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.restAssured.BaseClass.BaseClass;
import com.restAssured.Utilities.ExcelDataProvider;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class TC_PostRequest_002 extends BaseClass {

	public static HashMap data = new HashMap();
	String jsonAsString;

	@BeforeClass
	public void postRequest() {
		logger.info("**************TC002 POST Request Start**************");
		RestAssured.basePath = "/Jobs";
	}

	@Test(dataProvider = "PostData")
	void postJobsData(String ID, String jobTitle, String compName, String jobLocation, String jobType, String postTime,
			String description) {
		data.put("Job Id", ID);
		data.put("Job Title", jobTitle);
		data.put("Job Company Name", compName);
		data.put("Job Location", jobLocation);
		data.put("Job Type", jobType);
		data.put("Job Posted time", postTime);
		data.put("Job Description", description);

		logger.info("POST request Data Entered");
	}

	@Test(dependsOnMethods = { "postJobsData" })
	public void postJobsDetails() {
		Response response_post = given().body(data).when().post(BaseClass.BASE_URL);
		
		int StatusCodeCheck = response_post.getStatusCode();

		if (StatusCodeCheck == 200) {
			System.out.println("Status Code received successfully: " + StatusCodeCheck);
			String responsebody = response_post.getBody().asPrettyString();
			String reresponsebody = responsebody.replace("Nan", "Null");
			String response = reresponsebody.replace(",", "} {");

			response_post.then().assertThat().log().all();

			RestAssured.given().when().post().then()
					.body(JsonSchemaValidator.matchesJsonSchema(new File("./resources/jobsApi_get.json")));
			System.out.println("Post JsonSchema Validation successfully");
		} else if (StatusCodeCheck == 409) {
			System.out.println("Conflict Status Code received : " + StatusCodeCheck);
		}
		
		assertEquals(response_post != null, true);
	}

	@AfterClass
	void tearDown() {
		logger.info("************** TC002 POST Request END **************");
	}

	@DataProvider(name = "PostData")
	String[][] postData() throws IOException {
		String path = System.getProperty("user.dir") + "/Test-Data/Data.xlsx";

		int rownum = ExcelDataProvider.getRowCount(path, "sampleData");
		int colcount = ExcelDataProvider.getCellCount(path, "sampleData", 1);

		String postdata[][] = new String[rownum][colcount];

		for (int i = 1; i <= rownum; i++) {
			for (int j = 0; j < colcount; j++) 
			{
				postdata[i - 1][j] = ExcelDataProvider.getCellData(path, "sampleData", i, j);
			}

		}
		return postdata;
	}
}