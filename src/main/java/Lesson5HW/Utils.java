package Lesson5HW;

import com.sun.deploy.cache.JarSigningData;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sun.util.logging.resources.logging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class Utils {
    static Properties properties = new Properties();
    private static InputStream configFile;

    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static Retrofit getRetrofit() {
        logging.setLevel(BODY);
        httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }


    @SneakyThrows
    public static String getBaseUrl() {
        properties.load(configFile);
        return properties.getProperty("urlMarket");
    }

    static {
        try {
            configFile = new FileInputStream("src/test/resources/lesson3.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }





}

