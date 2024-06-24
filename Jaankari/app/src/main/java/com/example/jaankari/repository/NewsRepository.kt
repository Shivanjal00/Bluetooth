package com.example.jaankari.repository

import com.example.jaankari.api.RetrofitInstance
import com.example.jaankari.db.ArticleDatabase
import com.example.jaankari.models.Article
import retrofit2.http.Query
import java.util.Locale.IsoCountryCode

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getHeadlines(countryCode: String, pagenumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode, pagenumber)

    suspend fun searchNews(searchQuery : String, pageNumber:Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article) = db.getArticlrDao().upsert(article)

    fun  getFavouriteNews() = db.getArticlrDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticlrDao().deleteArticle(article)

}