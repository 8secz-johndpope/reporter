package hr.codable.reporter.rest

import hr.codable.reporter.entity.Article

interface RestInterface {

    fun getTopHeadlines(language: String, page: Int, pageSize: Int): List<Article>

    fun getEverything(keyword: String, pageSize: Int): List<Article>

    fun getEverything(keyword: String, page: Int, pageSize: Int): List<Article>
}