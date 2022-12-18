package Lesson5Tests;

import Lesson5HW.DtoProduct;
import Lesson5HW.ProductServiceInterface;
import Lesson5HW.Utils;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTests {
    static ProductServiceInterface productService;
    DtoProduct product = null;
    Faker faker = new Faker();
    int id;

    @BeforeAll
    static void beforeAll() {
    productService = Utils.getRetrofit().create(ProductServiceInterface.class);
    }

    @Test
    @SneakyThrows
    void createGetAndDeleteProductTest() {
        product = new DtoProduct()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
        Response<DtoProduct> response = productService.postProduct(product)
                .execute();
        id =  response.body().getId();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(201));

        Response<DtoProduct> response1 = productService.getProductById(id).execute();
        assertThat(response1.body().getId(), equalTo(id));
        assertThat(response1.code(), equalTo(200));


        Response<ResponseBody> response3 = productService.deleteProduct(id).execute();
        assertThat(response3.isSuccessful(), CoreMatchers.is(true));

    }
    @Test
    @SneakyThrows
    @Order(2)
    void getProductByIdTest() {
        Response<DtoProduct> response = productService.getProductById(1).execute();
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.code(), equalTo(200));
    }
    @Test
    @SneakyThrows
    @Order(1)
    void getProductsTest() {
        Response<ResponseBody> response = productService.getProducts().execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(200));
    }
}
