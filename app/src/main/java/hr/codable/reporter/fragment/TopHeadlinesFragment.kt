package hr.codable.reporter.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.codable.reporter.R

class TopHeadlinesFragment : Fragment() {

    var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.top_headlines_fragment, container, false)
        return v
    }
}