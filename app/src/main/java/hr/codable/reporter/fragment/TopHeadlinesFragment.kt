package hr.codable.reporter.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.codable.reporter.R
import hr.codable.reporter.adapter.RecyclerViewAdapter
import hr.codable.reporter.entity.Article
import hr.codable.reporter.entity.ArticleList
import hr.codable.reporter.rest.RestFactory

class TopHeadlinesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onRefresh() {

        loadTopHeadlines()
        Log.d("Reporter", "Refresh top headlines")
    }

    private var v: View? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.top_headlines_fragment, container, false)

        swipeRefreshLayout = v?.findViewById(R.id.top_headlines_swipeRefreshLayout)
        swipeRefreshLayout?.setOnRefreshListener(this)

        recyclerView = v?.findViewById(R.id.top_headlines_recyclerView)
        val recyclerViewAdapter = RecyclerViewAdapter(ArticleList.displayTopHeadlinesList)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = recyclerViewAdapter

        // when the fragment is created, fetch data from server
        swipeRefreshLayout?.post {

            loadTopHeadlines()
        }

        return v
    }

    private class LoadTopHeadlinesTask(private val topHeadlinesFragment: TopHeadlinesFragment) :
        AsyncTask<Void, Void, List<Article>?>() {

        override fun doInBackground(vararg params: Void?): List<Article>? {

            val service = RestFactory.instance

            return service.getTopHeadlines("us")
        }

        override fun onPostExecute(result: List<Article>?) {

            // save new data
            ArticleList.topHeadlinesList.addAll(result as Collection<Article>)
            val set: MutableSet<Article> = mutableSetOf()
            // put the data in a set to filter out duplicates
            set.addAll(ArticleList.topHeadlinesList)
            ArticleList.topHeadlinesList.clear()
            ArticleList.topHeadlinesList.addAll(set)

            ArticleList.displayTopHeadlinesList.addAll(ArticleList.topHeadlinesList)

            topHeadlinesFragment.recyclerView?.adapter?.notifyDataSetChanged()
            topHeadlinesFragment.swipeRefreshLayout?.isRefreshing = false
        }

    }

    private fun loadTopHeadlines() {

        swipeRefreshLayout?.isRefreshing = true
        LoadTopHeadlinesTask(this).execute()
    }

}