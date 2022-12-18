package Lesson5Tests;

import Lesson5HW.CategoryServiceInterface;
import Lesson5HW.DtoCategory;
import Lesson5HW.Utils;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static Lesson5HW.Utils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CategoryTest {
    static CategoryServiceInterface catService;
    @BeforeAll
    static void beforeAll() {
        catService = Utils.getRetrofit().create(CategoryServiceInterface.class);
    }
    @Test
    @SneakyThrows
    void getCategoryTest(){
        Response<DtoCategory> response = catService.getCategory(2).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(2));
        assertThat(response.body().getTitle(), equalTo("Electronic"));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo("Electronic")));
    }
}
