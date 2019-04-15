package elementx.test.musicplayer

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import elementx.bottomsheet.BottomSheetBehavior
import elementx.bottomsheet.BottomSheetLayout
import elementx.media.MediaCompatActivity
import elementx.media.elements.setProgressBars
import elementx.media.extensions.getQueuIndex
import elementx.media.mediacatalog.retrieveMediaAsync
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.bottom_sheet_player.*
import kotlinx.android.synthetic.main.bottom_sheet_player.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.opengl.ETC1.getHeight
import androidx.core.view.ViewCompat.animate
import android.R.attr.translationY




class MainActivity : MediaCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // All bellow these methods are optional
        // `bottomSheet` is ID of <elementx.test.musicplayer.BottomSheetLayout...>
        // To set it's state before draw
        bottomSheetLayout.setState(BottomSheetBehavior.STATE_COLLAPSED)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        btn_close.setOnClickListener {
            review_dialog.animate()
                .alpha(0.0f)
                .setDuration(700)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        review_dialog.visibility = View.GONE
                    }
                })
        }

    }

    override fun onBackPressed() {
        if (bottomSheetLayout.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetLayout.close()
        else super.onBackPressed()
    }


    override fun OnQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
        super.OnQueueChanged(queue)
        queue?.let {
            bottomSheetLayout.viewPager?.adapter = CustomPagerAdapter(it)
            bottomSheetLayout.viewPager?.adapter?.notifyDataSetChanged()
        }
    }

    private var syncPlay: Job? = null

    override fun OnServiceConnected(controller: MediaControllerCompat) {
        super.OnServiceConnected(controller)
        controller.queue?.let {
            bottomSheetLayout.viewPager?.adapter = CustomPagerAdapter(it)
            bottomSheetLayout.viewPager?.adapter?.notifyDataSetChanged()
        }

        bottomSheetLayout.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            private var isSwipeScroll = false
            override fun onPageScrollStateChanged(state: Int) {
                if (state == 1) isSwipeScroll = true
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (isSwipeScroll) {
                    syncPlay?.cancel()
                    syncPlay = GlobalScope.launch {
                        delay(1000)
                        controller.transportControls.skipToQueueItem(position.toLong())
                    }
                }
            }

        })

        bottomSheetLayout.btn_control_playpause.setOnClickListener {
            if (controller.playbackState.state == PlaybackStateCompat.STATE_PLAYING){
                controller.transportControls.pause()
            } else controller.transportControls.play()
        }
        bottomSheetLayout.btn_control_previous.setOnClickListener {
            controller.transportControls.skipToPrevious()
        }
        bottomSheetLayout.btn_control_next.setOnClickListener {
            controller.transportControls.skipToNext()
        }



        bottomSheetLayout.viewPager.currentItem = controller.getQueuIndex ?: 0

        // Add Listen to listen its callback
        bottomSheetLayout.eventListener = object : BottomSheetLayout.BottomSheetCallback {
            override fun onStateChanged(bottomSheet: View, newState: BottomSheetBehavior) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheet.option_panel.visibility = View.VISIBLE
                } else bottomSheet.option_panel.visibility = View.GONE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                // it gives animated values from [0-1] during animation
//                bottomSheet.btn_playpause.apply {
//                    val offset = slideOffset - 0.5f
//                    when {
//                        offset>0 -> setSheetButton(R.drawable.ic_arrow_down_24px)
//                        controller.isPlaying -> setSheetButton(R.drawable.ic_pause_24px)
//                        else -> setSheetButton(R.drawable.ic_play_arrow_24px)
//                    }
//                }
//
//                // Convert [0 0.5 1] => [1 0.5 1]
//                val opacity = slideOffset.let { if (it<.5f) 1f-(it*2f) else (it-.5f)*2f }
//                bottomSheet.btn_playpause.alpha = opacity

                val params = bottomSheet.mini_player.layoutParams as MarginLayoutParams
                params.setMargins(0, 0,0, (0-120*slideOffset).toInt())
                bottomSheet.mini_player.layoutParams = params

                // Other content to invisible on expand
                listOf<View>(
                    bottomSheet.title,
                    bottomSheet.description,
                    bottomSheet.imageView,
                    bottomSheet.progressBar
                ).setAlpha(1f-slideOffset)
            }
        }

        controller.setProgressBars(seekBar, progressBar)

        retrieveMediaAsync {
            runOnUiThread {
                //                viewPager.adapter = CustomPagerAdapter(it)
//                viewPager.adapter?.notifyDataSetChanged()

                // specify an adapter (see also next example)
                recyclerView.adapter = ListAdapter(it, controller)

            }
        }
    }

    override fun OnMetadataChanged(metadata: MediaMetadataCompat, queuIndex: Int) {
        super.OnMetadataChanged(metadata, queuIndex)
        bottomSheetLayout.sheet?.imageView?.setImageBitmap(metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART))
        bottomSheetLayout.sheet?.title?.text = metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
        bottomSheetLayout.sheet?.description?.text = metadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE)
        bottomSheetLayout.viewPager.currentItem = queuIndex
    }

    override fun OnPlaybackStateChanged(state: PlaybackStateCompat) {
        super.OnPlaybackStateChanged(state)

        if (state.state == PlaybackStateCompat.STATE_PLAYING) {
            R.drawable.ic_pause_24px.let {
                btn_control_playpause.setImageResource(it)
            }
        } else {
            R.drawable.ic_play_arrow_24px.let {
                btn_control_playpause.setImageResource(it)
            }
        }



    }



    private fun ImageView.setSheetButton(resId: Int) {
        if (tag != resId) setImageResource(resId).also { tag = resId }
    }

    private fun List<View>.setAlpha(alpha: Float) = forEach { it.alpha = alpha }
    private fun List<View>.setVisibility(visibility: Int) = forEach { it.visibility = visibility }
}
