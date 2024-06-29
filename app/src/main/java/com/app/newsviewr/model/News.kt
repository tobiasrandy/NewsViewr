package com.app.newsviewr.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("banner_url")
    val bannerUrl: String? = null,

    @field:SerializedName("time_created")
    val timeCreated: Long? = null,

    @field:SerializedName("rank")
    val rank: Int? = null
) : Parcelable
