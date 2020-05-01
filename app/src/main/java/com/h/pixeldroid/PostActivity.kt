package com.h.pixeldroid

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.h.pixeldroid.api.PixelfedAPI
import com.h.pixeldroid.fragments.PostFragment
import com.h.pixeldroid.objects.DiscoverPost
import com.h.pixeldroid.objects.Status
import com.h.pixeldroid.objects.Status.Companion.DISCOVER_TAG
import com.h.pixeldroid.objects.Status.Companion.DOMAIN_TAG
import com.h.pixeldroid.objects.Status.Companion.POST_TAG
import kotlinx.android.synthetic.main.activity_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    lateinit var postFragment : PostFragment
    lateinit var domain : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val status = intent.getSerializableExtra(POST_TAG) as Status?
        val discoverPost: DiscoverPost? = intent.getSerializableExtra(DISCOVER_TAG) as DiscoverPost?

        preferences = getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}.pref", Context.MODE_PRIVATE
        )
        domain = preferences.getString("domain", "")!!

        postFragment = PostFragment()
        val arguments = Bundle()
        arguments.putString(DOMAIN_TAG, domain)

        if (discoverPost != null) {
            getDiscoverPost(arguments, discoverPost)
        } else {
            initializeFragment(arguments, status)
        }
    }

    private fun getDiscoverPost(
        arguments: Bundle,
        discoverPost: DiscoverPost
    ) {
        val api = PixelfedAPI.create(domain)
        val accessToken = preferences.getString("accessToken", "") ?: ""
        val id = discoverPost.url?.substringAfterLast('/') ?: ""
        api.getStatus("Bearer $accessToken", id).enqueue(object : Callback<Status> {

            override fun onFailure(call: Call<Status>, t: Throwable) {
                Log.e("PostActivity:", t.toString())
            }

            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                if(response.code() == 200) {
                    val status = response.body()!!
                    initializeFragment(arguments, status)
                }
            }
        })
    }

    private fun initializeFragment(arguments: Bundle, status: Status?){
        postProgressBar.visibility = View.GONE
        arguments.putSerializable(POST_TAG, status)
        postFragment.arguments = arguments
        supportFragmentManager.beginTransaction()
            .add(R.id.postFragmentSingle, postFragment).commit()
        postFragmentSingle.visibility = View.VISIBLE
    }
}
