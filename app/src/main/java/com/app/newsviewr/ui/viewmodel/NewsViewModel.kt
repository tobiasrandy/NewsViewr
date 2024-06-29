package com.app.newsviewr.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.newsviewr.model.News
import com.app.newsviewr.repository.MainRepository
import com.app.newsviewr.util.LoadingType
import com.app.newsviewr.util.NetworkManager
import com.app.newsviewr.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class NewsViewModel @Inject constructor(app: Application, private val repository: MainRepository) : AndroidViewModel(app) {

    val newsData: MutableLiveData<Resource<MutableList<News>>> = MutableLiveData()
    private var newsList: MutableList<News> = mutableListOf()
    var loadingType: LoadingType = LoadingType.INITIAL

    var filter: String = "recent"
        set(value) {
            field = value
            getNewsList()
        }

    fun getNewsList() = viewModelScope.launch {
        safeNewsListCall()
    }

    fun clearNewsList() {
        loadingType = LoadingType.INITIAL
        newsList.clear()
    }

    private suspend fun safeNewsListCall() {
        newsData.postValue(Resource.Loading(type = loadingType))
        try {
            if(NetworkManager(getApplication()).isNetworkAvailable()) {
                val response = repository.getNewsList()
                newsData.postValue(handleNewsResponse(response))
            } else {
                newsData.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> newsData.postValue(Resource.Error("Network Failure"))
                else -> newsData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleNewsResponse(response: Response<MutableList<News>>) : Resource<MutableList<News>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                    if(filter == "popular") {
                        resultResponse.sortWith(
                            compareBy<News> { it.rank }
                                .thenByDescending { it.timeCreated }
                        )
                    } else resultResponse.sortByDescending { it.timeCreated }

                newsList.addAll(resultResponse)

                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}