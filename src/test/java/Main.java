import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
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

    String URL = "https://gorest.co.in/public/v1/users";
    String token = "1e3fbfc04d510423f16db6b1976fb8d49a339e4e486ef5006ebe118b552a2a32";

    @Test(priority = 1)
    public void get_call(){
        RestAssured.useRelaxedHTTPSValidation();
        given().
                baseUri("https://gorest.co.in/public/v1/users").
        when().
                get().
        then().statusCode(200);

    }

    @Test(priority = 2)
    public void test_gender(){
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given().
        when().get(URL).
        then().statusCode(200).extract().response();

        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");
        for(int gen = 0;gen<arr.length();gen++){
            assertThat(arr.getJSONObject(gen).get("gender"),anyOf(is(equalTo("male")),is(equalTo("female"))));
        }

    }

    @Test(priority = 3)
    public void check_email_extension(){
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given().
        when().get(URL).
        then().extract().response();

        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");

        int count = 0;
        for(int dom=0;dom<arr.length();dom++){
            String extension = arr.getJSONObject(dom).get("email").toString();
            //System.out.println(extension);
            String temp = "";
            for(int i=extension.length()-1;i>=0;i--){
                if(extension.charAt(i)=='.'){
                    break;
                }
                temp = temp + extension.charAt(i);
            }//System.out.println(temp);
            StringBuilder sb=new StringBuilder(temp);
            sb.reverse();
            temp = sb.toString();
            //System.out.println(temp);
            String req = "biz";
            if(temp.equals(req)){
                count = count + 1;
            }
        }
        assertThat(count,is(greaterThanOrEqualTo(2)));

    }

    @Test(priority = 4)
    public void unique_id(){
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given().
        when().get(URL).
        then().extract().response();

        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");
        int result = 1;
        Set<Integer> ID = new HashSet<Integer>();

        for(int traverse = 0;traverse<arr.length();traverse++){
            int ids = (int)arr.getJSONObject(traverse).get("id");
            //System.out.println(ids);
            if(ID.contains(ids)){
                result = 0;
                System.out.println(ids);
            }else {
                ID.add(ids);
            }
        }
        assertThat(result,is(equalTo(1)));

    }

    @Test(priority = 5)
    public void jsonSchemaValidation(){
        RestAssured.useRelaxedHTTPSValidation();
        given().
                baseUri(URL).
        when().get().
        then().assertThat().body(matchesJsonSchemaInClasspath("json_schema.json")).statusCode(200);
        /*Response response = given().
        when().get("https://gorest.co.in/public/v1/users").
        then().extract().response();
        assertThat(response,matchesJsonSchemaInClasspath("json_schema.json")));*/


    }

    @Test(priority = 6)
    public void test_post_call() throws IOException {
        RestAssured.useRelaxedHTTPSValidation();
        ExcelManager excel = new ExcelManager();
        int value = 3;
        org.json.simple.JSONObject jd = excel.get_data(value);
        given().
                header("Authorization","Bearer 1e3fbfc04d510423f16db6b1976fb8d49a339e4e486ef5006ebe118b552a2a32").
                header("Content-Type","application/json").
                baseUri(URL).
                body(jd).
        when().
                post().
        then().statusCode(201);
    }

    @Test(priority = 7)
    public void test_existing_data_post_call() throws IOException {
        RestAssured.useRelaxedHTTPSValidation();
        ExcelManager excel = new ExcelManager();
        int value = 3;
        org.json.simple.JSONObject jd = excel.get_data(value);

        given().
                header("Authorization","Bearer 1e3fbfc04d510423f16db6b1976fb8d49a339e4e486ef5006ebe118b552a2a32").
                header("Content-Type","application/json").
                baseUri(URL).
                body(jd).
        when().
                post().
        then().statusCode(422);
    }
}
