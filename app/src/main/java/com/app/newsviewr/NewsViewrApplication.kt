package com.app.newsviewr

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsViewrApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }

}