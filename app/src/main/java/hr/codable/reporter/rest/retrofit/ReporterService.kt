package hr.codable.reporter.rest.retrofit

import hr.codable.reporter.entity.Response
import retrofit.http.GET
import retrofit.http.Headers
import retrofit.http.Query

interface ReporterService {

    @Headers("X-Api-Key: fa2b4e8e826d4f578e36848a1e43c2b7")
    @GET("/top-headlines")
    fun getTopHeadlines(@Query("language") language: String, @Query("page") page: Int): Response

    @Headers("X-Api-Key: fa2b4e8e826d4f578e36848a1e43c2b7")
    @GET("/everything")
    fun getEverything(@Query("q") keyword: String): Response

    @Headers("X-Api-Key: fa2b4e8e826d4f578e36848a1e43c2b7")
    @GET("/everything")
    fun getEverything(@Query("q") keyword: String, @Query("page") page: Int): Response

}
