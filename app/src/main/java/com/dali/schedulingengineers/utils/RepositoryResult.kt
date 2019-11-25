package com.dali.schedulingengineers.utils

class RepositoryResult<T : Any?>(
    var state: Int,
    data: T? = null,
    var errorMessage: String? = null,
    var rawException: Exception? = null
) {

    val data: T? = data
        get() {
            isDataHandled = true
            return field
        }

    var isDataHandled: Boolean = false
        private set

    fun peekContent(): T? = data

    companion object RepositoryState {
        const val EMPTY = 0
        const val ERROR = 1
        const val SUCCESS = 2
        const val START_LOADING = 3
        const val LOAD_MORE = 4
        const val DELETE_SUCCESS = 5
        const val NO_INTERNET = 6
    }
}

val RepositoryResult<*>?.hasFreshData: Boolean
    get() {
        return this != null && !this.isDataHandled
    }