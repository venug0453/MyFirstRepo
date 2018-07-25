package TestSuite;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestCases 
{  
	private static int successStatusCode = 200;
	private static String responseContentType = "JSON";
	private static String repoName = "MyFirstRepo";
	
	public Response GetResponse()
	{
		RestAssured.baseURI = "https://api.github.com/repos/venug0453/MyFirstRepo";		
		Response response = when().get().then().extract().response();		
		return response;
	}
	
	@Test
	public void ValidateResponseStatusCode() 
	{	
		int statusCode = GetResponse().statusCode();
		assertEquals(statusCode, successStatusCode);
	}  
	
	@Test
	public void ValidateResponseContentType() 
	{
		String contentType = GetResponse().getContentType();
		assertTrue(contentType.toUpperCase().contains(responseContentType), "Content type is '" + contentType + "' which is not " + responseContentType);
	}
	
	@Test
	public void ValidateRepoName()
	{
		String repoNameResponse = GetResponse().jsonPath().getString("name").toString();
		assertEquals(repoNameResponse, repoName);
	}
}
