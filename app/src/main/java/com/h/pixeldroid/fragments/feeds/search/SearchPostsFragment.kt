package com.h.pixeldroid.fragments.feeds.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.h.pixeldroid.fragments.feeds.FeedFragment
import com.h.pixeldroid.fragments.feeds.PostViewHolder
import com.h.pixeldroid.fragments.feeds.PostsFeedFragment
import com.h.pixeldroid.objects.Results
import com.h.pixeldroid.objects.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPostsFragment: PostsFeedFragment(){

    private lateinit var query: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        query = arguments?.getSerializable("searchFeed") as String

        return view
    }

    inner class SearchFeedDataSource(
    ) : FeedDataSource(null, null){

        override fun newSource(): FeedDataSource {
            return SearchFeedDataSource()
        }

        private fun makeInitialCall(requestedLoadSize: Int): Call<Results> {
            return pixelfedAPI
                .search("Bearer $accessToken",
                    limit="$requestedLoadSize", q=query,
                    type = Results.SearchType.statuses)
        }
        private fun makeAfterCall(requestedLoadSize: Int, key: String): Call<Results> {
            return pixelfedAPI
                .search("Bearer $accessToken", max_id=key,
                    limit="$requestedLoadSize", q = query,
                    type = Results.SearchType.statuses)
        }
        override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<Status>
        ) {
            enqueueCall(makeInitialCall(params.requestedLoadSize), callback)
        }

        //This is called to when we get to the bottom of the loaded content, so we want statuses
        //older than the given key (params.key)
        override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<Status>) {
            enqueueCall(makeAfterCall(params.requestedLoadSize, params.key), callback)
        }

        private fun enqueueCall(call: Call<Results>, callback: LoadCallback<Status>){

            call.enqueue(object : Callback<Results> {
                override fun onResponse(call: Call<Results>, response: Response<Results>) {
                    if (response.code() == 200) {
                        val notifications = response.body()!!.statuses as ArrayList<Status>
                        callback.onResult(notifications as List<Status>)

                    } else{
                        Log.e("FeedFragment", "got response code ${response.code()}")
                    }
                    swipeRefreshLayout.isRefreshing = false
                    loadingIndicator.visibility = View.GONE
                }

                override fun onFailure(call: Call<Results>, t: Throwable) {
                    Log.e("FeedFragment", t.toString())
                }
            })
        }


    }

    override fun makeContent(): LiveData<PagedList<Status>> {
        val config: PagedList.Config = PagedList.Config.Builder().setPageSize(10).build()
        factory = FeedFragment<Status, PostViewHolder>()
            .FeedDataSourceFactory(SearchFeedDataSource())
        return LivePagedListBuilder(factory, config).build()
    }
}