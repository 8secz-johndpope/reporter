package hr.codable.reporter.rest.retrofit

import hr.codable.reporter.entity.ResponseModel
import retrofit.http.GET

interface ReporterService {

    @get:GET("/top-headlines?country=us&category=business&apiKey=fa2b4e8e826d4f578e36848a1e43c2b7")
    val response: ResponseModel
}
