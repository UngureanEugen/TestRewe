package android.com.testrewe

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ReweService {

    @GET("/")
    fun getPage(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/basket/add-product/")
    fun addProduct(@HeaderMap headers: Map<String, String>, @Field("context") context: String, @Field("nan") nan: String, @Field("quantity") quantity: String, @Field("token") token: String): Call<ResponseBody>
}