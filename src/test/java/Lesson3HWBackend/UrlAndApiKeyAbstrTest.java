package Lesson3HWBackend;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class UrlAndApiKeyAbstrTest {
    static Properties properties = new Properties();
    private static String baseUrl;
    private static String apiKey;
    private static InputStream configFile;

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getApiKey() {
        return apiKey;
    }


    @BeforeAll
    static void beforeAllTests() throws IOException {
        configFile = new FileInputStream("src/test/resources/lesson3.properties");
        properties.load(configFile);
        apiKey = properties.getProperty("apiKey");
        baseUrl = properties.getProperty("baseUrl");

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    @AfterEach
    void afterEachTestLine(){
        System.out.println("+++++++++++++++++++++++++++++++");
    }
}
