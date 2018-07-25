package TestSuite;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Tests
{  
	private static int successStatusCode = 200;
	private static String responseContentType = "JSON";
	private static String repoName = "MyFirstRepo";
	private static String repoOwnerName = "venug0453";
	
	public Response GetResponse()
	{
		RestAssured.baseURI = "https://api.github.com/repos/" + repoOwnerName + "/" + repoName;		
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
	
	@Test
	public void ValidateRepoFullName()
	{
		String repoNameResponse = GetResponse().jsonPath().getString("full_name").toString();
		assertEquals(repoNameResponse, repoOwnerName + "/" + repoName);
	}
	
	@Test
	public void ValidateResponseTimeStampFormat()
	{
		String responseTimeStamp = GetResponse().getHeader("Date");
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, 1);
		String dateStr = df.format(date);
		String dateStr1 = df.format(calendar.getTime());
		
		if(responseTimeStamp.equals(dateStr))
		{
		}	
		else if(responseTimeStamp.equals(dateStr1))
		{			
		}
		else
		{
			fail("Time stamp under the 'Date' field of the response header is not as expected.");
		}
	}
}
