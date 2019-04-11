package elementx.test.musicplayer

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_player.*
import kotlinx.android.synthetic.main.bottom_sheet_player.view.*
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main_content.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//
//        val list = mutableListOf(User("Kavan", "Mevada"), User("Damini", "Tripathi"))
//
//        // specify an adapter (see also next example)
//        val mAdapter = ListAdapter(list)
//        recyclerView.adapter = mAdapter


        val list = mutableListOf(User("Kavan", "Mevada"), User("Damini", "Tripathi"))
        viewPager.adapter = CustomPagerAdapter(list)



        // bottom sheet player behavior
        BottomSheetBehavior.from(bottom_sheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            isHideable = false
            imageView.toCardView(16f)
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        option_panel.visibility = View.VISIBLE
                    } else option_panel.visibility = View.GONE
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    btn_play_pause.apply {
                        val offset = slideOffset - 0.5f
                        if(offset>0) setSheetButton(R.drawable.ic_arrow_down_24px)
                        else setSheetButton(R.drawable.ic_play_arrow_24px)
                    }

                    // Convert [0 0.5 1] => [1 0.5 1]
                    val opacity = slideOffset.let { if (it<.5f) 1f-(it*2f) else (it-.5f)*2f }
                    btn_play_pause.alpha = opacity


                    // Other content to invisible on expand
                    listOf<View>(
                            bottomSheet.title,
                            description,
                            imageView,
                            progressBar
                    ).setAlpha(1f-slideOffset)
                }
            })
        }


        imageView2.toCardView(46f)

    }

    private fun ImageView.setSheetButton(resId: Int) {
        if (tag != resId) setImageResource(resId).also { tag = resId }
    }

    private fun List<View>.setAlpha(alpha: Float) = forEach { it.alpha = alpha }
    private fun List<View>.setVisibility(visibility: Int) = forEach { it.visibility = visibility }
}
