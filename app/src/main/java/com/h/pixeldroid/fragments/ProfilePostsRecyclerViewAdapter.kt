package com.h.pixeldroid.fragments

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.h.pixeldroid.PostActivity
import com.h.pixeldroid.R
import com.h.pixeldroid.objects.Status
import com.h.pixeldroid.utils.ImageConverter.Companion.setSquareImageFromURL

/**
 * [RecyclerView.Adapter] that can display a list of [PostMiniature]s and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class ProfilePostsRecyclerViewAdapter(
    private val context: Context
) : RecyclerView.Adapter<ProfilePostsRecyclerViewAdapter.ViewHolder>() {
    private val posts: ArrayList<Status> = ArrayList()

    fun addPosts(newPosts : List<Status>) {
        val size = posts.size
        posts.addAll(newPosts)
        notifyItemRangeInserted(size, newPosts.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_profile_posts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        setSquareImageFromURL(holder.postView, post.getPostPreviewURL(), holder.postPreview)
        holder.postPreview.setOnClickListener {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra(Status.POST_TAG, post)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = posts.size

    inner class ViewHolder(val postView: View) : RecyclerView.ViewHolder(postView) {
        val postPreview: ImageView = postView.findViewById(R.id.postPreview)
    }
}
