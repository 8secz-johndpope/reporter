package hr.codable.reporter.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.codable.reporter.R
import hr.codable.reporter.adapter.RecyclerViewAdapter
import hr.codable.reporter.entity.Article
import hr.codable.reporter.entity.ArticleList

class TopHeadlinesFragment : Fragment() {

    var v: View? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.top_headlines_fragment, container, false)
        recyclerView = v?.findViewById(R.id.top_headlines_recyclerView)
        val recyclerViewAdapter = RecyclerViewAdapter(ArticleList.displayTopHeadlinesList)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = recyclerViewAdapter
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ArticleList.displayTopHeadlinesList.add(
            Article(
                "izvor", "autor", "naslov",
                "opis", "juarel", "url na sliku", "3.7.2019.", "sadrzaj"
            )
        )
    }
}