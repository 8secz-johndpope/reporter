package hr.codable.reporter

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hr.codable.reporter.entity.Article
import hr.codable.reporter.rest.RestFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val API_KEY = "fa2b4e8e826d4f578e36848a1e43c2b7"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoadTopHeadlinesTask().execute()
    }

    private inner class LoadTopHeadlinesTask : AsyncTask<Void, Void, List<Article>?>(){

        var articles: List<Article>? = null

        override fun doInBackground(vararg params: Void?): List<Article>? {

            val service = RestFactory.instance

            return service.getTopHeadlines("techcrunch", API_KEY);
        }

        override fun onPostExecute(result: List<Article>?) {

            authorTextView.text = result?.get(0)?.author
            titleTextView.text = result?.get(0)?.title
            descriptiontextView.text = result?.get(0)?.description
            urlTextView.text = result?.get(0)?.url
            publishedAtTextView.text = result?.get(0)?.publishedAt
            contentTextView.text = result?.get(0)?.content
        }

    }
}
