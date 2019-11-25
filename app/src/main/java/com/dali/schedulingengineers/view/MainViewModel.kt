package com.dali.schedulingengineers.view

import android.app.Application
import androidx.lifecycle.*
import com.dali.schedulingengineers.model.models.Engineer
import com.dali.schedulingengineers.model.repository.EngineersRepository
import com.dali.schedulingengineers.utils.RepositoryResult
import com.dali.schedulingengineers.utils.ScheduleFinder
import com.dali.schedulingengineers.utils.hasFreshData

typealias EngineersListViewStateLiveData = LiveData<MainViewModel.EngineersListViewState?>
typealias EngineersScheduleLiveData = MutableLiveData<RepositoryResult<Array<Array<Engineer?>>>>

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = EngineersRepository(application)
    val engineersList = mutableListOf<Engineer>()

    enum class EngineersListViewState(var errorMessage: String? = null) {
        SHOW_LIST,
        LOADING,
        DO_NOTHING,
        ERROR
    }

    var viewStateLiveData: EngineersListViewStateLiveData = MutableLiveData()
        private set
    var engineersScheduleLiveData = EngineersScheduleLiveData()
        private set

    var viewStateMediator: MediatorLiveData<EngineersListViewState>

    init {
        viewStateMediator = MediatorLiveData<EngineersListViewState>().apply {
            addSource(repository.engineersListLiveData) { result ->
                if (result?.hasFreshData == true) {
                    val viewState = when {
                        (result.state == RepositoryResult.SUCCESS) -> {
                            result.data?.let { response ->
                                engineersList.addAll(response)

                                EngineersListViewState.SHOW_LIST
                            } ?: EngineersListViewState.ERROR.apply {
                                errorMessage = result.errorMessage
                            }
                        }
                        (result.state == RepositoryResult.START_LOADING) -> {
                            EngineersListViewState.LOADING
                        }
                        else -> EngineersListViewState.ERROR.apply {
                            errorMessage = result.errorMessage
                        }
                    }
                    this.value = viewState
                }
            }
        }
        viewStateLiveData = Transformations.map(viewStateMediator) { it }
    }

    fun getEngineersList(isRefresh: Boolean) {
        if (isRefresh)
            engineersList.clear()
        repository.getEngineersList()
    }

    fun findSchedule() {
        if (engineersList.isNotEmpty()) {
            engineersScheduleLiveData.value = RepositoryResult(RepositoryResult.START_LOADING)
            ScheduleFinder(engineersList = engineersList) { isSolutionFound, result ->
                if (isSolutionFound) {
                    engineersScheduleLiveData.value =
                        RepositoryResult(RepositoryResult.SUCCESS, result)
                } else {
                    engineersScheduleLiveData.value = RepositoryResult(RepositoryResult.ERROR)
                }
            }.findSchedule()
        }
    }
}