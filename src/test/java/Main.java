import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static io.restassured.RestAssured.given;

public class Main {

    //Global variables
    String URL = "https://gorest.co.in/public/v1/users";
    ResponseSpecification responseSpecification;
    RequestSpecification requestSpecification;

    //Setup method for reusability
    @BeforeTest
    public void setup(){

        RestAssured.useRelaxedHTTPSValidation();

        //request specbuilder for request specification
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(URL).
                addHeader("Content-Type","application/json");
        requestSpecification = RestAssured.given().spec(requestSpecBuilder.build());

        //response specification for testing status code and content type
        responseSpecification = RestAssured.expect().contentType(ContentType.JSON).statusCode(200);
    }

    //Test for testing response
    @Test(priority = 1)
    public void get_call(){
        //RestAssured.useRelaxedHTTPSValidation();
        given().
                spec(requestSpecification).
        when().
                get().
        then().spec(responseSpecification);

    }

    //Test for checking Gender value should be either male or female
    @Test(priority = 2)
    public void test_gender(){
        //RestAssured.useRelaxedHTTPSValidation();
        Response response = given().
                spec(requestSpecification).
        when().get().
        then().spec(responseSpecification).extract().response();

        //creating json object for the response
        JSONObject obj = new JSONObject(response.asString());
        //getting the data array from the response
        JSONArray arr = obj.getJSONArray("data");

        //loop through the array
        for(int gen = 0;gen<arr.length();gen++){
            assertThat(arr.getJSONObject(gen).get("gender"),anyOf(is(equalTo("male")),is(equalTo("female"))));
        }

    }

    //Test for checking	Response should contain at least 2 users with domain extension as .biz
    @Test(priority = 3)
    public void check_email_extension(){
        //RestAssured.useRelaxedHTTPSValidation();
        Response response = given().
                spec(requestSpecification).
        when().get().
        then().spec(responseSpecification).extract().response();

        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");

        int minimum_required = 2;
        int count = 0;

        //loop for getting each email from response
        for(int dom=0;dom<arr.length();dom++){
            String extension = arr.getJSONObject(dom).get("email").toString();
            String temp = "";

            //loop for taking the domain extension
            for(int i=extension.length()-1;i>=0;i--){
                if(extension.charAt(i)=='.'){
                    break;
                }
                temp = temp + extension.charAt(i);
            }

            //reversing the domain extension
            StringBuilder sb=new StringBuilder(temp);
            sb.reverse();
            temp = sb.toString();
            //System.out.println(temp);
            String req = "biz";
            if(temp.equals(req)){
                count = count + 1;
            }
        }
        assertThat(count,is(greaterThanOrEqualTo(minimum_required)));

    }

    //Test for checking id values in the response should be unique
    @Test(priority = 4)
    public void unique_id(){
        //RestAssured.useRelaxedHTTPSValidation();
        Response response = given().
                spec(requestSpecification).
        when().get().
        then().spec(responseSpecification).extract().response();


        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");
        int result = 1;

        //creating a hashset for storing id
        Set<Integer> ID = new HashSet<Integer>();

        //loop for getting each id from the response
        for(int traverse = 0;traverse<arr.length();traverse++){
            int ids = (int)arr.getJSONObject(traverse).get("id");
            if(ID.contains(ids)){
                result = 0;
                System.out.println(ids);
            }else {
                ID.add(ids);
            }
        }
        assertThat(result,is(equalTo(1)));

    }

    //Test for Validate the json schema for the response
    @Test(priority = 5)
    public void jsonSchemaValidation(){
        //RestAssured.useRelaxedHTTPSValidation();
        given().
                spec(requestSpecification).
        when().get().
        then().spec(responseSpecification).assertThat().body(matchesJsonSchemaInClasspath("json_schema.json"));
    }

    //Test for post call and getting data from excel
    @Test(priority = 6)
    public void test_post_call() throws IOException {
        //RestAssured.useRelaxedHTTPSValidation();
        ExcelManager excel = new ExcelManager();
        int value = 4;

        //calling method getdata for getting data from excel
        org.json.simple.JSONObject jd = excel.get_data(value);
        given().
                header("Authorization","Bearer 1e3fbfc04d510423f16db6b1976fb8d49a339e4e486ef5006ebe118b552a2a32").
                spec(requestSpecification).
                body(jd).
        when().
                post().
        then().statusCode(201);
    }

    //Test for	Validate the error code for already used data
    @Test(priority = 7)
    public void test_existing_data_post_call() throws IOException {
        //RestAssured.useRelaxedHTTPSValidation();
        ExcelManager excel = new ExcelManager();
        int value = 4;
        org.json.simple.JSONObject jd = excel.get_data(value);

        given().
                header("Authorization","Bearer 1e3fbfc04d510423f16db6b1976fb8d49a339e4e486ef5006ebe118b552a2a32").
                spec(requestSpecification).
                body(jd).
        when().
                post().
        then().statusCode(422);
    }
}
