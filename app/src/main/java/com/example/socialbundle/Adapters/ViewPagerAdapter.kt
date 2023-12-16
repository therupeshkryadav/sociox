import android.graphics.drawable.Icon
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = mutableListOf<Fragment>()
    private val iconList = mutableListOf<Icon>()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getIconResource(position: Int): Icon {
        return iconList[position]
    }

    fun addFragment(fragment: Fragment, icon: Icon) {
        fragmentList.add(fragment)
        iconList.add(icon)
    }
}
