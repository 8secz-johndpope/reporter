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

class EverythingFragment : Fragment() {

    var v: View? = null
    private var recyclerView: RecyclerView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.everything_fragment, container, false)
        recyclerView = v?.findViewById(R.id.everything_recyclerView)
        val recyclerViewAdapter = RecyclerViewAdapter(ArticleList.displayEverythingList)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = recyclerViewAdapter
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ArticleList.displayEverythingList.add(
            Article(
                "sors", "kreativac", "tajtl",
                "deskripsn", "juarel", "url na sliku", "20.20.1082.", "kontetn"
            )
        )
    }
}