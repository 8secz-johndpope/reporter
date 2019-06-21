package hr.codable.reporter.rest

import hr.codable.reporter.entity.Article

interface RestInterface {

    fun getTopHeadlines(country: String): List<Article>

    fun getEverything(keyword: String): List<Article>
}