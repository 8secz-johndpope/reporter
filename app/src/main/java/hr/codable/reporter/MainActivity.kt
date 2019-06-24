package hr.codable.reporter

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
        val articlesPerPage = findViewById<EditText>(R.id.articles_per_page_id)

        viewPagerAdapter.addFragment(TopHeadlinesFragment(), getString(R.string.top_headlines))
        viewPagerAdapter.addFragment(EverythingFragment(), getString(R.string.everything))

        viewPager?.adapter = viewPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)

        articlesPerPage.setOnKeyListener(object : View.OnKeyListener {

            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                if (KeyEvent.ACTION_DOWN == event.action && keyCode == KEYCODE_ENTER) {

                    if (articlesPerPage.text.toString().toInt() < 20 || articlesPerPage.text.toString().toInt() > 100) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.between_twenty_and_hundred_warning),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        ArticleList.articlesPerPage = articlesPerPage.text.toString().toInt()
                    }
                    val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    return true
                }
                return false
            }
        })
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

                    searchInEverything(query)
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

        override fun doInBackground(vararg params: String): List<Article> {

            val service = RestFactory.instance

            var list: List<Article> = emptyList()
            try {
                list = service.getEverything(URLEncoder.encode(params[0], "utf-8"), params[1].toInt())
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
                val set: MutableSet<Article> = mutableSetOf()
                set.addAll(ArticleList.everythingList)
                set.addAll(result)
                ArticleList.displayEverythingList.clear()
                ArticleList.everythingList.addAll(set)
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

    private fun searchInEverything(query: String) {

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.everything_swipeRefreshLayout)
        swipeRefreshLayout.isRefreshing = true

        SearchInEverythingTask(this@MainActivity).execute(query, ArticleList.articlesPerPage.toString())
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout_id)
        tabLayout.getTabAt(1)?.select()
    }
}
