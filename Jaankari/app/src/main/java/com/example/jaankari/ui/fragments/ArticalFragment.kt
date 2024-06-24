package com.example.jaankari.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.jaankari.ui.NewsActivity
import com.example.jaankari.ui.NewsViewModel
import com.example.jaankari.R
import com.example.jaankari.databinding.ActivityNewsBinding
import com.example.jaankari.databinding.FragmentArticalBinding
import com.google.android.material.snackbar.Snackbar

class ArticalFragment : Fragment(R.layout.fragment_artical) {

    lateinit var newsViewModel: NewsViewModel
    val args:ArticalFragmentArgs by navArgs()
    lateinit var binding: FragmentArticalBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticalBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }

        binding.fab.setOnClickListener{
            newsViewModel.addToFavorites(article)
            Snackbar.make(view, "Added to Favourites",Snackbar.LENGTH_SHORT).show()
        }

    }

}