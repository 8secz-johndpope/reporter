package hr.codable.reporter

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import hr.codable.reporter.adapter.ViewPagerAdapter
import hr.codable.reporter.entity.Article
import hr.codable.reporter.entity.ArticleList
import hr.codable.reporter.fragment.EverythingFragment
import hr.codable.reporter.fragment.TopHeadlinesFragment
import hr.codable.reporter.rest.RestFactory
import java.lang.ref.WeakReference
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout_id)
        val viewPager = findViewById<ViewPager>(R.id.viewpager_id)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(TopHeadlinesFragment(), getString(R.string.top_headlines))
        viewPagerAdapter.addFragment(EverythingFragment(), getString(R.string.everything))

        viewPager?.adapter = viewPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.search_bar_id)
        val searchView = searchItem?.actionView as SearchView?
        searchView?.queryHint = getString(R.string.search_in_everything)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                val tabLayout = findViewById<TabLayout>(R.id.tabLayout_id)
                tabLayout.getTabAt(1)?.select()

                if (newText.isNotBlank()) {

                    Flag.dynamicSearchActive = true

                    ArticleList.displayEverythingList.clear()
                    for (article in ArticleList.everythingList) {
                        if (article.title.toString().contains(newText, true)
                            || article.description.toString().contains(newText, true)
                            || article.source?.name.toString().contains(newText, true)
                            || article.author.toString().contains(newText, true)
                        ) {

                            ArticleList.displayEverythingList.add(article)
                        }

                    }
                    val recyclerView = findViewById<RecyclerView>(R.id.everything_recyclerView)
                    recyclerView.adapter?.notifyDataSetChanged()

                    return true
                } else {
                    ArticleList.displayEverythingList.clear()
                    ArticleList.displayEverythingList.addAll(ArticleList.everythingList)
                }

                Flag.dynamicSearchActive = false

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                if (query.isNotBlank()) {

                    searchInEverything(query, this@MainActivity)
                    return true
                }

                return false
            }
        })

        searchView?.setOnCloseListener {
            val recyclerView = findViewById<RecyclerView>(R.id.everything_recyclerView)
            recyclerView.adapter?.notifyDataSetChanged()
            false
        }

        return super.onCreateOptionsMenu(menu)
    }

    private class SearchInEverythingTask(activity: MainActivity) : AsyncTask<String, Void, List<Article>>() {

        private val reference = WeakReference(activity)

        override fun doInBackground(vararg params: String?): List<Article> {

            val service = RestFactory.instance

            var list: List<Article> = emptyList()
            try {
                list = service.getEverything(URLEncoder.encode(params[0], "utf-8"))
            } finally {
                return list
            }
        }

        override fun onPostExecute(result: List<Article>?) {

            val activityReference = reference.get()

            if (result.isNullOrEmpty()) {
                Toast.makeText(
                    activityReference,
                    activityReference?.getString(R.string.retrofit_error_exception_toast),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                ArticleList.everythingList.addAll(result)
                ArticleList.displayEverythingList.clear()
                ArticleList.displayEverythingList.addAll(ArticleList.everythingList)
                ArticleList.displayEverythingList.reverse()

                val recyclerView = activityReference?.findViewById<RecyclerView>(R.id.everything_recyclerView)
                recyclerView?.adapter?.notifyDataSetChanged()
            }

            val swipeRefreshLayout =
                activityReference?.findViewById<SwipeRefreshLayout>(R.id.everything_swipeRefreshLayout)
            swipeRefreshLayout?.isRefreshing = false

            Log.d("Reporter", "Done searching")

        }
    }

    private fun searchInEverything(query: String, activity: MainActivity) {

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.everything_swipeRefreshLayout)
        swipeRefreshLayout.isRefreshing = true

        SearchInEverythingTask(activity).execute(query)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout_id)
        tabLayout.getTabAt(1)?.select()
    }
}
