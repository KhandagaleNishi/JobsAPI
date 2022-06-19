package com.restAssured.JobsAPITest;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertThat;
import static org.testng.Assert.assertEquals;

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

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_DeleteRequest_004 extends BaseClass {
	public static HashMap data = new HashMap();
	String jsonAsString;

	@BeforeClass
	void deleteRequest() throws InterruptedException {
		logger.info("**************TC004 DELETE Request Start**************");
		RestAssured.basePath = "/Jobs";
	}

	@Test(dataProvider = "Deletedata")
	void deleteJobsData(String ID) throws IOException, InterruptedException {
		data.put("Job Id", ID);
	}

	@Test(dependsOnMethods = { "deleteJobsData" })
	public void deleteJobsDetails() {
		Response response_del = given().body(data).when().delete(BaseClass.BASE_URL);

		int StatusCodeCheck = response_del.getStatusCode();

		if (StatusCodeCheck == 200) {
			System.out.println("Status Code received successfully: " + StatusCodeCheck);
			String responsebody = response_del.getBody().asPrettyString();
			String reresponsebody = responsebody.replace("Nan", "Null");
			String response = reresponsebody.replace(",", "} {");
			response_del.then().assertThat().log().all();

			RestAssured.given().when().post().then()
					.body(JsonSchemaValidator.matchesJsonSchema(new File("./resources/jobsApi_get.json")));
			System.out.println("Delete JsonSchema Validation successfully");
		} else if (StatusCodeCheck == 404) {
			System.out.println("JobId Not Found Status Code received : " + StatusCodeCheck);
		}
		assertEquals(response_del != null, true);
	}
	

	@AfterClass
	void tearDown() {
		logger.info("************** TC004 DELETE Request END **************");
	}


	@DataProvider(name = "Deletedata")
	String[][] putData() throws IOException {
		String path = System.getProperty("user.dir") + "/Test-Data/Data.xlsx";

		int rownum = ExcelDataProvider.getRowCount(path, "deleteDataSheet");
		int colcount = ExcelDataProvider.getCellCount(path, "deleteDataSheet", 1);

		String deleteData[][] = new String[rownum][colcount];

		for (int i = 1; i <= rownum; i++) {
			for (int j = 0; j < colcount; j++) 
			{
				deleteData[i - 1][j] = ExcelDataProvider.getCellData(path, "deleteDataSheet", i, j);
			}
		}
		return deleteData;
	}

}
