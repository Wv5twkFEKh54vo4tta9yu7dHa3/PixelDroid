package com.h.pixeldroid.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.h.pixeldroid.BuildConfig
import com.h.pixeldroid.R
import com.h.pixeldroid.api.PixelfedAPI
import com.h.pixeldroid.fragments.feeds.PostViewHolder
import com.h.pixeldroid.objects.Status
import com.h.pixeldroid.objects.Status.Companion.DOMAIN_TAG
import com.h.pixeldroid.objects.Status.Companion.POST_TAG


class PostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val status = arguments?.getSerializable(POST_TAG) as Status?
        val domain = arguments?.getString(DOMAIN_TAG)!!
        val root = inflater.inflate(R.layout.post_fragment, container, false)
        val picRequest = Glide.with(this)
            .asDrawable().fitCenter()
            .placeholder(ColorDrawable(Color.GRAY))

        status?.setupPost(root, picRequest, this, domain, true)

        //Setup arguments needed for the onclicklisteners
        val holder = PostViewHolder(root, requireContext())

        val preferences = requireActivity().getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}.pref", Context.MODE_PRIVATE
        )
        val accessToken = preferences.getString("accessToken", "")
        val api = PixelfedAPI.create("${preferences.getString("domain", "")}")

        status?.setDescription(root, api, "Bearer $accessToken")

        //Activate onclickListeners
        status?.activateLiker(holder, api, "Bearer $accessToken", status.favourited)
        status?.activateReblogger(holder, api, "Bearer $accessToken", status.reblogged)
        status?.activateCommenter(holder, api, "Bearer $accessToken")
        status?.showComments(holder, api, "Bearer $accessToken")

        return root
    }
}
