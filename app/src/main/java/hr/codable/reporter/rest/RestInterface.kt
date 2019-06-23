package hr.codable.reporter.rest

import hr.codable.reporter.entity.Article

interface RestInterface {

    fun getTopHeadlines(language: String, page: Int): List<Article>

    fun getEverything(keyword: String): List<Article>

    fun getEverything(keyword: String, page: Int): List<Article>
}