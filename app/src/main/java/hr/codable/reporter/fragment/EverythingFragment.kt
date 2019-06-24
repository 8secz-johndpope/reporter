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
import hr.codable.reporter.Flag
import hr.codable.reporter.R
import hr.codable.reporter.adapter.RecyclerViewAdapter
import hr.codable.reporter.entity.Article
import hr.codable.reporter.entity.ArticleList
import hr.codable.reporter.rest.RestFactory
import java.lang.ref.WeakReference
import java.net.URLEncoder

class EverythingFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var isLoading = false

    override fun onRefresh() {

        loadEverything(false)
        Log.d("Reporter", "Refresh everything")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.everything_fragment, container, false)

        val swipeRefreshLayout = v?.findViewById<SwipeRefreshLayout>(R.id.everything_swipeRefreshLayout)
        swipeRefreshLayout?.setOnRefreshListener(this)

        val recyclerView = v?.findViewById<RecyclerView>(R.id.everything_recyclerView)
        val recyclerViewAdapter = RecyclerViewAdapter(ArticleList.displayEverythingList)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = recyclerViewAdapter

        // when the fragment is created, fetch data from server
        swipeRefreshLayout?.post {

            loadEverything(false)
        }

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading) {

                    if (linearLayoutManager?.findLastCompletelyVisibleItemPosition()
                        == ArticleList.displayEverythingList.size - 1
                        && !Flag.dynamicSearchActive
                    ) {

                        loadEverything(true)
                        isLoading = true
                    }
                }
            }
        })

        return v
    }

    private class LoadEverythingTask(fragment: EverythingFragment) :
        AsyncTask<String, Void, List<Article>?>() {

        private val reference = WeakReference(fragment)

        override fun doInBackground(vararg params: String): List<Article>? {

            val service = RestFactory.instance

            var list: List<Article> = emptyList()
            try {
                list =
                    service.getEverything(URLEncoder.encode(params[0], "utf-8"), params[1].toInt(), params[2].toInt())
            } finally {
                return list
            }
        }

        override fun onPostExecute(result: List<Article>?) {

            val fragmentReference = reference.get()
            if (result.isNullOrEmpty()) {
                Toast.makeText(
                    fragmentReference?.context,
                    fragmentReference?.getString(R.string.retrofit_error_exception_toast),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // save new data
                ArticleList.everythingList.addAll(result)
                val set: MutableSet<Article> = mutableSetOf()
                // put the data in a set to filter out duplicates
                set.addAll(ArticleList.everythingList)
                ArticleList.everythingList.clear()
                ArticleList.everythingList.addAll(set)

                ArticleList.displayEverythingList.clear()
                ArticleList.displayEverythingList.addAll(ArticleList.everythingList)

                val recyclerView = fragmentReference?.view?.findViewById<RecyclerView>(R.id.everything_recyclerView)
                recyclerView?.adapter?.notifyDataSetChanged()
            }

            val swipeRefreshLayout =
                fragmentReference?.view?.findViewById<SwipeRefreshLayout>(R.id.everything_swipeRefreshLayout)
            swipeRefreshLayout?.isRefreshing = false
            fragmentReference?.isLoading = false
        }
    }

    fun loadEverything(loadMore: Boolean) {

        val swipeRefreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.everything_swipeRefreshLayout)
        swipeRefreshLayout?.isRefreshing = true

        val page: String = if (loadMore) {
            (ArticleList.everythingList.size / ArticleList.articlesPerPage + 1).toString()
        } else {
            "1"
        }
        LoadEverythingTask(this).execute("tech", page, ArticleList.articlesPerPage.toString())
    }
}