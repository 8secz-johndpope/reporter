package hr.codable.reporter.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter constructor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {

        return fragments.get(position)
    }

    override fun getCount(): Int {

        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return titles.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {

        fragments.add(fragment)
        titles.add(title)
    }


}