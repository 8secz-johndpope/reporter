package hr.codable.reporter
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import hr.codable.reporter.adapter.ViewPagerAdapter
import hr.codable.reporter.fragment.EverythingFragment
import hr.codable.reporter.fragment.TopHeadlinesFragment

class MainActivity : AppCompatActivity() {

//    private val API_KEY = "fa2b4e8e826d4f578e36848a1e43c2b7"

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout_id)
        viewPager = findViewById(R.id.viewpager_id)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter?.addFragment(TopHeadlinesFragment(), "Top Headlines")
        viewPagerAdapter?.addFragment(EverythingFragment(), "Everything")

        viewPager?.adapter = viewPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)


//        LoadTopHeadlinesTask().execute()
    }
}


//    private inner class LoadTopHeadlinesTask : AsyncTask<Void, Void, List<Article>?>(){
//
//        var articles: List<Article>? = null
//
//        override fun doInBackground(vararg params: Void?): List<Article>? {
//
//            val service = RestFactory.instance
//
//            return service.getTopHeadlines("techcrunch", API_KEY);
//        }
//
//        override fun onPostExecute(result: List<Article>?) {
//
//            authorTextView.text = result?.get(0)?.author
//            titleTextView.text = result?.get(0)?.title
//            descriptiontextView.text = result?.get(0)?.description
//            urlTextView.text = result?.get(0)?.url
//            publishedAtTextView.text = result?.get(0)?.publishedAt
//            contentTextView.text = result?.get(0)?.content
//        }
//
//    }
//}
