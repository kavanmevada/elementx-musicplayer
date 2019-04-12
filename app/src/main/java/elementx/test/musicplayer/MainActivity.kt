package elementx.test.musicplayer

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import elementx.bottomsheet.BottomSheetBehavior
import elementx.bottomsheet.BottomSheetLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.bottom_sheet_player.*
import kotlinx.android.synthetic.main.bottom_sheet_player.view.*


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




        // All bellow these methods are optional
        // `bottomSheet` is ID of <elementx.bottomsheet.BottomSheetLayout...>
        // To set it's state before draw
        bottomSheetLayout.setState(BottomSheetBehavior.STATE_COLLAPSED)
        bottomSheetLayout.sheet?.imageView?.toCardView(16f)
        // Add Listen to listen its callback
        bottomSheetLayout.eventListener = object : BottomSheetLayout.BottomSheetCallback {
            override fun onStateChanged(bottomSheet: View, newState: BottomSheetBehavior) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    option_panel.visibility = View.VISIBLE
                } else option_panel.visibility = View.GONE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // it gives animated values from [0-1] during animation

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
        }

    }

    private fun ImageView.setSheetButton(resId: Int) {
        if (tag != resId) setImageResource(resId).also { tag = resId }
    }

    private fun List<View>.setAlpha(alpha: Float) = forEach { it.alpha = alpha }
    private fun List<View>.setVisibility(visibility: Int) = forEach { it.visibility = visibility }
}
