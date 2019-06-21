package hr.codable.reporter

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import hr.codable.reporter.adapter.ViewPagerAdapter
import hr.codable.reporter.entity.Article
import hr.codable.reporter.entity.ArticleList
import hr.codable.reporter.fragment.EverythingFragment
import hr.codable.reporter.fragment.TopHeadlinesFragment
import hr.codable.reporter.rest.RestFactory
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout_id)
        viewPager = findViewById(R.id.viewpager_id)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter?.addFragment(TopHeadlinesFragment(), getString(R.string.top_headlines))
        viewPagerAdapter?.addFragment(EverythingFragment(), getString(R.string.everything))

        viewPager?.adapter = viewPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)

        LoadTopHeadlinesTask().execute()
        LoadEverythingTask().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.search_bar_id)
        searchView = searchItem?.actionView as SearchView?
        searchView?.queryHint = getString(R.string.search_in_everything)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onQueryTextChange(newText: String): Boolean {

                val tabLayout = findViewById<TabLayout>(R.id.tabLayout_id)
                tabLayout.getTabAt(1)?.select()

                if (newText.isNotBlank()) {

                    ArticleList.displayEverythingList.removeIf {
                        !(it.title.contains(newText) || it.description.contains(newText))
                        // TODO fix this search algorithm
                    }
                    val recyclerView = findViewById<RecyclerView>(R.id.everything_recyclerView)
                    recyclerView.adapter?.notifyDataSetChanged()
                    return true
                } else {
                    ArticleList.displayEverythingList.addAll(ArticleList.everythingList)
                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                if (query.isNotBlank()) {
                    SearchInEverythingTask().execute(query)

                    val tabLayout = findViewById<TabLayout>(R.id.tabLayout_id)
                    tabLayout.getTabAt(1)?.select()

                    return true
                }

                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private inner class LoadTopHeadlinesTask : AsyncTask<Void, Void, List<Article>?>() {

        override fun doInBackground(vararg params: Void?): List<Article>? {

            val service = RestFactory.instance

            return service.getTopHeadlines("us")
        }

        override fun onPostExecute(result: List<Article>?) {

            ArticleList.topHeadlinesList.addAll(result as Collection<Article>)
            ArticleList.displayTopHeadlinesList.addAll(ArticleList.topHeadlinesList)

            val recyclerView = findViewById<RecyclerView>(R.id.top_headlines_recyclerView)
            recyclerView.adapter?.notifyDataSetChanged()
        }

    }

    private inner class LoadEverythingTask : AsyncTask<Void, Void, List<Article>?>() {

        override fun doInBackground(vararg params: Void?): List<Article>? {

            val service = RestFactory.instance

            return service.getEverything("tech")
        }

        override fun onPostExecute(result: List<Article>?) {

            ArticleList.everythingList.addAll(result as Collection<Article>)
            ArticleList.displayEverythingList.addAll(ArticleList.everythingList)

            val recyclerView = findViewById<RecyclerView>(R.id.everything_recyclerView)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private inner class SearchInEverythingTask : AsyncTask<String, Void, List<Article>>() {

        override fun doInBackground(vararg params: String?): List<Article> {

            val service = RestFactory.instance

            return service.getEverything(URLEncoder.encode(params[0], "utf-8"))
        }

        override fun onPostExecute(result: List<Article>?) {

            ArticleList.displayEverythingList.clear()
            ArticleList.displayEverythingList.addAll(result as Collection<Article>)

            val recyclerView = findViewById<RecyclerView>(R.id.everything_recyclerView)
            recyclerView.adapter?.notifyDataSetChanged()

            Log.d("Reporter", "Done searching")

        }
    }
}
