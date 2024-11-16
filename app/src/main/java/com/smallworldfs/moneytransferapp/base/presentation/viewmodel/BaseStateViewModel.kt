package com.smallworldfs.moneytransferapp.base.presentation.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.navigator.BaseNavigator
import com.smallworldfs.moneytransferapp.utils.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.Serializable

abstract class BaseStateViewModel<S : Serializable, N : BaseNavigator> : BaseViewModel<N>() {

    private var viewState: S? = null

    private var baseState: BaseState<S>? = null

    fun getCurrentState(): BaseState<S>? = baseState

    internal val observableState: MutableLiveData<BaseState<S>> = MutableLiveData()

    fun getObservableState(): LiveData<BaseState<S>> = observableState

    abstract val initialViewState: S

    fun onStart(inputState: BaseState<S>?): Boolean {
        if (viewState == null) {
            inputState?.let { viewState = it.data }
        }

        val firstTime = if (baseState == null) {
            val initialStatus = inputState ?: createInitialState()
            baseState = initialStatus
            onStartFirstTime(inputState != null)
            true
        } else false

        observableState.value = baseState

        onResume(firstTime)

        return firstTime
    }

    /**
     * Check the current view state
     */
    fun <T> checkDataState(checkStateFunction: (S) -> T): T {
        return viewState?.let {
            checkStateFunction.invoke(it)
        } ?: let {
            val initialState = initialViewState
            viewState = initialState
            checkStateFunction.invoke(initialState)
        }
    }

    open fun createInitialState(): BaseState<S> {
        if (viewState == null) {
            viewState = initialViewState
        }
        return BaseState.Normal(viewState!!)
    }

    private fun updateView(state: BaseState<S>) {
        this.baseState = state
        this.observableState.postValue(state)
    }

    protected open fun updateToNormalState(changeStateFunction: S.() -> S) {
        viewState?.let {
            viewState = changeStateFunction.invoke(it)
            viewState?.let { newState -> baseState = BaseState.Normal(newState) }
            updateToNormalState()
        }
    }

    protected open fun updateToNormalState() {
        baseState?.let {
            viewState?.let { currentState ->
                updateView(BaseState.Normal(currentState))
            }
        }
    }

    protected fun updateDataState(changeStateFunction: S.() -> S) {
        viewState?.let {
            viewState = changeStateFunction.invoke(it)
            viewState?.let { newState ->
                baseState = when (baseState) {
                    is BaseState.Error -> {
                        val errorState = baseState as BaseState.Error
                        BaseState.Error(newState, errorState.dataError)
                    }
                    is BaseState.Normal -> {
                        BaseState.Normal(newState)
                    }

                    is BaseState.Alternative -> {
                        val alternativeState = baseState as BaseState.Alternative
                        BaseState.Alternative(newState, alternativeState.dataAlternative)
                    }
                    null -> BaseState.Normal(newState)
                }
            }
        }
    }

    protected open fun updateToErrorState(error: Throwable = Throwable()) {
        viewState?.let {
            updateView(BaseState.Error(it, error))
        } ?: throwInitialStateException()
    }

    protected open fun updateToAlternativeState(data: BaseExtraData? = null) {
        viewState?.let { state ->
            val alternativeData: BaseState.Alternative<S> = data?.let {
                BaseState.Alternative(state, dataAlternative = it)
            } ?: BaseState.Alternative(state)

            updateView(alternativeData)
        } ?: throwInitialStateException()
    }

    private fun throwInitialStateException(): Nothing = throw RuntimeException("Initial state has not been created")

    /**
     * Called when is needed to launch first time options
     */
    abstract fun onStartFirstTime(statePreloaded: Boolean)

    /**
     * Called when the activity onResume is being called
     */
    open fun onResume(firstTime: Boolean) {}

    /**
     * Execute use cases with and without exceptions with coroutines
     */
    fun executeUseCaseWithException(block: suspend CoroutineScope.() -> Unit, exceptionBlock: suspend CoroutineScope.(Throwable) -> Unit, handleCancellationManually: Boolean = false) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                if (e !is CancellationException || handleCancellationManually) {
                    exceptionBlock(e)
                } else {
                    throw e
                }
                Log.e("Fatal error executing use case", "Error: $e")
            }
        }
    }

    /**
     * Remove live data observables
     */
    fun unBindObservables(lifecycleOwner: LifecycleOwner) {
        observableState.removeObservers(lifecycleOwner)
    }
}
