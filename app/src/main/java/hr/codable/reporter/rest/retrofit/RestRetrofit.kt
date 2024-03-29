package hr.codable.reporter.rest.retrofit

import hr.codable.reporter.entity.Article
import hr.codable.reporter.rest.RestInterface
import retrofit.RestAdapter
import retrofit.android.AndroidLog


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

    override fun getTopHeadlines(language: String, page: Int, pageSize: Int): List<Article> {

        return service.getTopHeadlines(language, page, pageSize).articles
    }

    override fun getEverything(keyword: String, pageSize: Int): List<Article> {

        return service.getEverything(keyword, pageSize).articles
    }

    override fun getEverything(keyword: String, page: Int, pageSize: Int): List<Article> {

        return service.getEverything(keyword, page, pageSize).articles
    }
}