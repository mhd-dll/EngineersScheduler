package com.dali.schedulingengineers.model.models


import com.google.gson.annotations.SerializedName

data class EngineersList(
    @SerializedName("engineers")
    val engineers: List<Engineer>? = listOf()
)