package hr.codable.reporter.entity

data class Response(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)