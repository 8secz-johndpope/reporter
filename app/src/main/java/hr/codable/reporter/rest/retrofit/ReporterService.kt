package hr.codable.reporter.rest.retrofit

import hr.codable.reporter.APIKey
import hr.codable.reporter.entity.Response
import retrofit.http.GET
import retrofit.http.Headers
import retrofit.http.Query

interface ReporterService {

    @Headers("X-Api-Key: " + APIKey.apiKey)
    @GET("/top-headlines")
    fun getTopHeadlines(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response

    @Headers("X-Api-Key: " + APIKey.apiKey)
    @GET("/everything")
    fun getEverything(
        @Query("q") keyword: String,
        @Query("pageSize") pageSize: Int
    ): Response

    @Headers("X-Api-Key: " + APIKey.apiKey)
    @GET("/everything")
    fun getEverything(
        @Query("q") keyword: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response

}
