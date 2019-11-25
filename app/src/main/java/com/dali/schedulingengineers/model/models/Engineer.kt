package com.dali.schedulingengineers.model.models


import com.google.gson.annotations.SerializedName

data class Engineer(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String? = ""
)