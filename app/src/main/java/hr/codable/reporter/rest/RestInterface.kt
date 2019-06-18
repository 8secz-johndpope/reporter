package hr.codable.reporter.rest

import hr.codable.reporter.entity.Article

interface RestInterface {

    fun getTopHeadlines(source: String, apiKey: String): List<Article>
}