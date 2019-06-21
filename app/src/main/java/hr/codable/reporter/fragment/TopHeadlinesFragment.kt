package hr.codable.reporter.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.codable.reporter.R
import hr.codable.reporter.adapter.RecyclerViewAdapter
import hr.codable.reporter.entity.ArticleList

class TopHeadlinesFragment : Fragment() {

    var v: View? = null
    var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.top_headlines_fragment, container, false)
        recyclerView = v?.findViewById(R.id.top_headlines_recyclerView)
        val recyclerViewAdapter = RecyclerViewAdapter(ArticleList.displayTopHeadlinesList)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = recyclerViewAdapter

        val dividerItemDecoration = DividerItemDecoration(recyclerView?.getContext(), 1)
        recyclerView?.addItemDecoration(dividerItemDecoration)

        return v
    }

}