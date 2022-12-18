package Lesson5HW;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProductServiceInterface {
    @GET("products")
    Call<ResponseBody> getProducts();
    @POST("products")
    Call<DtoProduct> postProduct(@Body DtoProduct createProductRequest);
    @PUT("products")
    Call<DtoProduct> putProduct(@Body DtoProduct modifyProductRequest);
    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);
    @GET("products/{id}")
    Call<DtoProduct> getProductById(@Path("id") int id);

}
