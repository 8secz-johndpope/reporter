package hr.codable.reporter.rest.retrofit

import hr.codable.reporter.entity.Article
import hr.codable.reporter.entity.ResponseModel
import hr.codable.reporter.rest.RestInterface
import retrofit.RestAdapter
import retrofit.android.AndroidLog
import retrofit.http.Query


class RestRetrofit : RestInterface {

    private val service: ReporterService

    init {

        val baseURL = "https://newsapi.org/v2"
        val retrofit = RestAdapter.Builder()
            .setEndpoint(baseURL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setLog(AndroidLog("REPORTER REST"))
            .build()

        service = retrofit.create(ReporterService::class.java)
    }

    override fun getTopHeadlines(source: String, apiKey: String): List<Article> {

        return service.response.articles
    }
}