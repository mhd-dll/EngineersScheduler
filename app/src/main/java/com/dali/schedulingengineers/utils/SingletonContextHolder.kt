package com.dali.schedulingengineers.utils

/**
 * Code obtained from
 * https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
 *
 */

open class SingletonContextHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val instance = instance
        if (instance != null) {
            return instance
        }

        return synchronized(this) {
            val synchronizedInstance = this.instance
            if (synchronizedInstance != null) {
                synchronizedInstance
            } else {
                val created = creator!!(arg)
                this.instance = created
                creator = null
                created
            }
        }
    }
}