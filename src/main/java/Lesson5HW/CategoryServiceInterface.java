package Lesson5HW;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryServiceInterface {
    @GET("categories/{id}")
    Call<DtoCategory>getCategory(@Path("id") int id);

}
