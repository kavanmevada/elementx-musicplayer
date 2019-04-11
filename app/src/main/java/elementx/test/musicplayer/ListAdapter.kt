package elementx.test.musicplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import elementx.test.musicplayer.databinding.ListItemTwoTxtImgBinding
import elementx.test.musicplayer.databinding.ListItemTwoTxtImgBinding.inflate


class ListAdapter(val list: MutableList<User>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)


    inner class ViewHolder(private val binding: ListItemTwoTxtImgBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.user = list[position]
        }
    }
}