package elementx.test.musicplayer

import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import elementx.test.musicplayer.databinding.ListItemTwoTxtImgBinding


class CustomPagerAdapter(val list: MutableList<User>) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val binding = ListItemTwoTxtImgBinding.inflate(LayoutInflater.from(collection.context), collection, false)
        binding.user = list[position]
        collection.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount() = list.size

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

}