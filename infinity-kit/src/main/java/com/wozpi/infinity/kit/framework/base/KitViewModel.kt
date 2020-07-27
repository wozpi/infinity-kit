package com.wozpi.infinity.kit.framework.base

import androidx.lifecycle.MutableLiveData
import com.wozpi.infinity.kit.framework.model.ErrorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class KitViewModel {
    private val mParentJob = Job()
    private val mCoroutineContext: CoroutineContext
        get() = mParentJob + Dispatchers.Main

    open val mScope = CoroutineScope(mCoroutineContext)
    val mCommonErrorLive = MutableLiveData<ErrorResponse>()

    fun cancelAllRequest(){

    }
}