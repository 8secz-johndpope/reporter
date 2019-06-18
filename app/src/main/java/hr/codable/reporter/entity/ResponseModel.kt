package hr.codable.reporter.entity

data class ResponseModel(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)