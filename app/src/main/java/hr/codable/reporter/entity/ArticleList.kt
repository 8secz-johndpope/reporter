package hr.codable.reporter.entity

object ArticleList {

    var everythingList: MutableList<Article> = mutableListOf()
    var topHeadlinesList: MutableList<Article> = mutableListOf()
    var displayEverythingList: MutableList<Article> = mutableListOf()
    var displayTopHeadlinesList: MutableList<Article> = mutableListOf()

    var articlesPerPage = 20
}