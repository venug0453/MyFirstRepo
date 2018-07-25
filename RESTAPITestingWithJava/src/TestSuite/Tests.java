package TestSuite;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Tests
{  
	//Variables
	private static int successStatusCode = 200;
	private static String responseContentType = "JSON";
	private static String repoName = "MyFirstRepo";
	private static String repoOwnerName = "venug0453";
	private static String baseURI = "https://api.github.com/repos/" + repoOwnerName + "/" + repoName;	
	
	//Get Response
	public Response GetResponse()
	{
		RestAssured.baseURI = baseURI;		
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
	public void ValidateResponseDateHeaderFormat()
	{
		String responseTimeStamp = GetResponse().getHeader("Date");
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, 1);
		String dateStr = df.format(date);
		String dateStr1 = df.format(calendar.getTime());
		
		assertTrue((responseTimeStamp.equals(dateStr) || responseTimeStamp.equals(dateStr1)), "The 'Date' field under the response header is not as expected.");		
	}
	
	@Test
	public void ValidateTimeStampsFormatUnderResponse()
	{
		Response response = GetResponse();
		String responseCreatedAtTimeStamp = response.jsonPath().getString("created_at").toString();
		String responseUpdatedAtTimeStamp = response.jsonPath().getString("updated_at").toString();
		String responsePushedAtTimeStamp = response.jsonPath().getString("pushed_at").toString();
		
		String matchPattern = "[\\d]{4}\\-[\\d]{2}\\-[\\d]{2}T[\\d]{2}:[\\d]{2}:[\\d]{2}Z";
		assertTrue(responseCreatedAtTimeStamp.matches(matchPattern), "'created_at' field under the response is not in the expected format.");
		assertTrue(responseUpdatedAtTimeStamp.matches(matchPattern), "'updated_at' field under the response is not in the expected format.");
		assertTrue(responsePushedAtTimeStamp.matches(matchPattern), "'pushed_at' field under the response is not in the expected format.");
	}
	
	@Test
	public void ValidateResponseURLField()
	{
		String urlFieldResponse = GetResponse().jsonPath().getString("url").toString();
		assertEquals(urlFieldResponse, baseURI, "Expected 'url' field <" + baseURI + "> but was <" + urlFieldResponse + ">.");
	}
	
	@Test
	public void ValidateOwnerLoginFromResponse()
	{
		String ownerLogin = GetResponse().jsonPath().getString("owner.login").toString();
		assertEquals(ownerLogin, repoOwnerName, "Expected 'owner.login' field <" + repoOwnerName + "> but was <" + ownerLogin + ">.");
	}
}
