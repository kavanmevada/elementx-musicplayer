package elementx.test.musicplayer

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import elementx.test.musicplayer.databinding.ListItemTwoTxtImgBinding
import elementx.test.musicplayer.databinding.NowplayingItemViewBinding


class CustomPagerAdapter(val list: MutableList<User>) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val binding = NowplayingItemViewBinding.inflate(LayoutInflater.from(collection.context), collection, false)
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

class CustomViewPager(context: Context, attributeSet: AttributeSet) : ViewPager(context, attributeSet) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpece = heightMeasureSpec
        try {
            val wrapHeight = View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.AT_MOST
            if (wrapHeight) {
                val child = getChildAt(0)
                if (child != null) {
                    child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                    val h = child.measuredHeight
                    heightMeasureSpece = View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpece)
    }
}