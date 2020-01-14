package com.xfinity.features.masterdetail.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xfinity.R

class ItemDetailFragment : Fragment() {

    private var tabletSize: Boolean = false
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_detail, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabletSize = resources.getBoolean(R.bool.isTablet)

        if (tabletSize) {
            progressBar = view.findViewById(R.id.progress_circular)
            if (arguments != null) progressBar.visibility = View.VISIBLE;

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                // For tablet show loading animation instead of transition animation
                loadContent(view, arguments)
            }, 400)
        } else {
            loadContent(view, arguments)
        }
    }

    private fun loadContent(view: View, bundle: Bundle?) {
        if (bundle != null) {
            if (bundle.containsKey(ITEM)) {
                val item = bundle.getString(ITEM)
                val descriptionTextView = view.findViewById<TextView>(R.id.textview_description)
                descriptionTextView.text = item
            }
            if (bundle.containsKey(ICON)) {
                val iconUrl = bundle.getString(ICON)
                val animalImageView = view.findViewById<ImageView>(R.id.imageview_item)
                activity?.let {
                    if (iconUrl == "") {
                        Glide.with(it)
                                .setDefaultRequestOptions(RequestOptions()
                                        .circleCrop())
                                .load(R.drawable.ic_launcher)
                                .placeholder(R.drawable.android3)
                                .into(animalImageView)
                    } else {
                        Glide.with(it)
                                .setDefaultRequestOptions(RequestOptions()
                                        .circleCrop())
                                .load(iconUrl)
                                .placeholder(R.drawable.android3)
                                .into(animalImageView)
                    }
                }
            }
            if (tabletSize) {
                progressBar.visibility = View.GONE;
            }
        }
    }

    companion object {
        const val ITEM = "item"
        const val ICON = "icon"
    }
}