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
import android.widget.Toast
import hr.codable.reporter.R
import hr.codable.reporter.adapter.RecyclerViewAdapter
import hr.codable.reporter.entity.Article
import hr.codable.reporter.entity.ArticleList
import hr.codable.reporter.rest.RestFactory
import java.net.URLEncoder

class EverythingFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onRefresh() {

        loadEverything("tech")
        Log.d("Reporter", "Refresh everything")
    }

    private var v: View? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.everything_fragment, container, false)

        swipeRefreshLayout = v?.findViewById(R.id.everything_swipeRefreshLayout)
        swipeRefreshLayout?.setOnRefreshListener(this)

        recyclerView = v?.findViewById(R.id.everything_recyclerView)
        val recyclerViewAdapter = RecyclerViewAdapter(ArticleList.displayEverythingList)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = recyclerViewAdapter

        // when the fragment is created, fetch data from server
        swipeRefreshLayout?.post {
            loadEverything("tech")
        }

        return v
    }

    private class LoadEverythingTask(private val everythingFragment: EverythingFragment) :
        AsyncTask<String, Void, List<Article>?>() {

        override fun doInBackground(vararg params: String): List<Article>? {

            val service = RestFactory.instance

            var list: List<Article> = emptyList()
            try {
                list = service.getEverything(URLEncoder.encode(params[0], "utf-8"))
            } finally {
                return list
            }
        }

        override fun onPostExecute(result: List<Article>?) {

            if (result.isNullOrEmpty()) {
                Toast.makeText(
                    everythingFragment.context,
                    everythingFragment.getString(R.string.retrofit_error_exception_toast),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // save new data
                ArticleList.everythingList.addAll(result as Collection<Article>)
                val set: MutableSet<Article> = mutableSetOf()
                // put the data in a set to filter out duplicates
                set.addAll(ArticleList.everythingList)
                ArticleList.everythingList.clear()
                ArticleList.everythingList.addAll(set)

                ArticleList.displayEverythingList.addAll(ArticleList.everythingList)
                everythingFragment.recyclerView?.adapter?.notifyDataSetChanged()
            }

            everythingFragment.swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun loadEverything(query: String) {
        swipeRefreshLayout?.isRefreshing = true
        LoadEverythingTask(this).execute(query)
    }
}