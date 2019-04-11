package elementx.test.musicplayer

import android.graphics.Color
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

fun View.toCardView(corRadius: Float, z: Float = 0f) {
    if (background==null) setBackgroundColor(Color.TRANSPARENT)
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline?) {
            outline?.setRoundRect(view.background.bounds, corRadius)
        }
    }
    clipToOutline = true
    setZ(z)
}
