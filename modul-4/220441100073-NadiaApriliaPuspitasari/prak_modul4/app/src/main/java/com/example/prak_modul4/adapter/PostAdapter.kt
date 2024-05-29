package com.example.prak_modul4.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prak_modul4.R
import com.example.prak_modul4.room.PostDatabase
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

// Kelas adapter untuk RecyclerView dengan tampilan LinearLayoutManager
class PostAdapter(private var postList: List<PostDatabase>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    // Deklarasi variabel untuk callback ketika item diklik
    private lateinit var onItemClickCallback: OnItemClickCallback

    // Map untuk menyimpan status suka dari setiap item
    private val likedStatus = mutableMapOf<Int, Boolean>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PostDatabase)
    }

    // Kelas ViewHolder untuk menyimpan referensi view yang digunakan dalam RecyclerView
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTitle: TextView = itemView.findViewById(R.id.post_title)
        val postDesc: TextView = itemView.findViewById(R.id.post_desc)
        val postImg: ShapeableImageView = itemView.findViewById(R.id.post_img)
        val postLike: TextView = itemView.findViewById(R.id.post_like)
        val btnLike: ImageView = itemView.findViewById(R.id.btn_like)
//        val postDate: MaterialTextView = itemView.findViewById(R.id.post_time)

//        val postDate: TextView = itemView.findViewById(R.id.post_time)

        //btn

//        val btnMore: ImageView = itemView.findViewById(R.id.btn_more)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    // Fungsi untuk mengikat data dengan ViewHolder
    // (memasukkan data yang kita miliki ke dalam XML ViewHolder)
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val data = postList[position]

        holder.postTitle.text = data.name
        holder.postDesc.text = data.description.shorten(500)
        holder.postLike.text = data.like.toString()
//        holder.postDate.text = data.waktu

        // Mengatur image
        val uri = Uri.fromFile(data.image)
        holder.postImg.setImageURI(uri)

        // Set initial like status and icon
        val isLiked = likedStatus[position] ?: false
        holder.btnLike.setImageResource(if (isLiked) R.drawable.baseline_favorite_24 else R.drawable.outline_favorite_border_24)
        holder.postLike.text = data.like.toString()

//        holder.btnLike.setOnClickListener {
//            data.like += 1
//            holder.postLike.text = data.like.toString()
//        }
        holder.btnLike.setOnClickListener {
            val newLikedStatus = !(likedStatus[position] ?: false)
            likedStatus[position] = newLikedStatus

            if (newLikedStatus) {
                holder.btnLike.setImageResource(R.drawable.baseline_favorite_24)
                data.like += 1
            } else {
                holder.btnLike.setImageResource(R.drawable.outline_favorite_border_24)
                data.like -= 1
            }

            holder.postLike.text = data.like.toString()
        }

//        holder.btnMore.setOnClickListener { onItemClickCallback.onItemClicked(postList[holder.absoluteAdapterPosition]) }
    }

    override fun getItemCount(): Int = postList.size

    fun updatePosts(posts: List<PostDatabase>) {
        postList = posts
        notifyDataSetChanged()
    }
    private fun String.shorten(maxLength: Int): String {
        return if (this.length <= maxLength) this else "${this.substring(0, maxLength)}..."
    }
}