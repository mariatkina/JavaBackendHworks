package Lesson5Tests;

import Lesson5HW.DtoProduct;
import Lesson5HW.ProductServiceInterface;
import Lesson5HW.Utils;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ProductCreateTest {
    static ProductServiceInterface productService;
    DtoProduct product = null;
    Faker faker = new Faker();
    int id;

    @BeforeAll
    static void beforeAll() {
        productService = Utils.getRetrofit().create(ProductServiceInterface.class);
    }

    @BeforeEach
    @SneakyThrows
    void createProduct(){
        product = new DtoProduct()
                .withTitle("Butter")
                .withCategoryTitle("Food")
                .withPrice(300);
        Response<DtoProduct> response = productService.postProduct(product)
                .execute();
        id =  response.body().getId();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(201));
        assertThat(response.body().getPrice(), equalTo(300));
    }
    @Test
    @SneakyThrows
    void getProduct(){
        Response<DtoProduct> response = productService.getProductById(id).execute();
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.code(), equalTo(200));
    }
    @Test
    @SneakyThrows
    void modifyProduct(){
        product = product.withId(id).withTitle("Butter").withCategoryTitle("Food").withPrice(400);
        Response<DtoProduct> response1 = productService.putProduct(product).execute();
        assertThat(response1.code(), equalTo(200));
        assertThat(response1.body().getPrice(), equalTo(400));
    }
    @AfterEach
    @SneakyThrows
    void deleteProduct(){
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }
}
