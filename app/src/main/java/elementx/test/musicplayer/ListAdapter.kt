package elementx.test.musicplayer

import android.content.Context
import android.support.v4.media.session.MediaControllerCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elementx.media.extensions.isPlaying
import elementx.media.extensions.setQueueList
import elementx.media.models.MediaItem
import elementx.test.musicplayer.databinding.ListItemTwoTxtImgBinding
import elementx.test.musicplayer.databinding.ListItemTwoTxtImgBinding.inflate


class ListAdapter(
    val list: MutableList<MediaItem>,
    val mediaControllerCompat: MediaControllerCompat
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.context, inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)


    inner class ViewHolder(private val context: Context, private val binding: ListItemTwoTxtImgBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.mediaItem = list[position]
        }

        init {
            itemView.setOnClickListener {
                mediaControllerCompat.setQueueList(context, list)
                mediaControllerCompat.transportControls.skipToQueueItem(adapterPosition.toLong())
                if(!mediaControllerCompat.isPlaying) mediaControllerCompat.transportControls.play()
            }
        }
    }
}