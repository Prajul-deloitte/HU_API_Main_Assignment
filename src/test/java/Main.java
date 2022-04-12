import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static io.restassured.RestAssured.given;

public class Main {
    @Test
    public void get_call(){
        RestAssured.useRelaxedHTTPSValidation();
        given().
                baseUri("https://gorest.co.in/public/v1/users").
        when().
                get().
        then().statusCode(200);

    }

    @Test
    public void test_gender(){
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given().
        when().get("https://gorest.co.in/public/v1/users").
        then().extract().response();

        JSONObject obj = new JSONObject(response.asString());
        JSONArray arr = obj.getJSONArray("data");
        for(int gen = 0;gen<arr.length();gen++){
            assertThat(arr.getJSONObject(gen).get("gender"),anyOf(is(equalTo("male")),is(equalTo("female"))));
        }

    }

    @Test
    public void check_email_extension(){
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given().
        when().get("https://gorest.co.in/public/v1/users").
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
            String req = "name";
            if(temp.equals(req)){
                count = count + 1;
            }
        }
        assertThat(count,is(greaterThanOrEqualTo(2)));

    }
}
