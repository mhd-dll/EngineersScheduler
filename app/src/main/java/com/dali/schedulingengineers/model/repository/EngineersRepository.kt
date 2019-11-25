package com.dali.schedulingengineers.model.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.dali.schedulingengineers.model.api.EngineersListApi
import com.dali.schedulingengineers.model.models.Engineer
import com.dali.schedulingengineers.utils.RepositoryResult

typealias EnigneersListResultLiveData = MutableLiveData<RepositoryResult<List<Engineer>?>>

class EngineersRepository(val context: Context) {
    private val engineersApi by lazy { EngineersListApi(context) }

    val engineersListLiveData = EnigneersListResultLiveData()

    fun getEngineersList() {
        engineersListLiveData.postValue(
            RepositoryResult(RepositoryResult.START_LOADING)
        )
        engineersApi.getEngineersList { error, response ->
            when {
                error != null -> {
                    engineersListLiveData.postValue(
                        RepositoryResult(
                            RepositoryResult.ERROR,
                            errorMessage = error
                        )
                    )
                }
                response != null -> {
                    engineersListLiveData.postValue(
                        RepositoryResult(
                            RepositoryResult.SUCCESS,
                            data = response
                        )
                    )
                }
            }
        }
    }
}