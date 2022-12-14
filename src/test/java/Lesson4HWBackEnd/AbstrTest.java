package Lesson4HWBackEnd;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AbstrTest {
    static Properties properties = new Properties();
    private static String baseUrl;
    private static String apiKey;
    private static InputStream configFile;
    private static ResponseSpecification responseSpecification;
    private static RequestSpecification requestSpecification;

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static ResponseSpecification getResponseSpecification() {
        return responseSpecification;
    }

    public static RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

    @BeforeAll
    static void beforeAllTests() throws IOException {
        configFile = new FileInputStream("src/test/resources/lesson3.properties");
        properties.load(configFile);
        apiKey = properties.getProperty("apiKey");
        baseUrl = properties.getProperty("baseUrl");


        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .log(LogDetail.ALL)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

        RestAssured.responseSpecification = responseSpecification;
        RestAssured.requestSpecification = requestSpecification;
    }
    @AfterEach
    void afterEachTestLine(){
        System.out.println("+++++++++++++++++++++++++++++++");
    }
}
