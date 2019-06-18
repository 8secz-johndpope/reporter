package hr.codable.reporter.rest

import hr.codable.reporter.rest.retrofit.RestRetrofit

object RestFactory {

    val instance: RestRetrofit
        get() = RestRetrofit()
}