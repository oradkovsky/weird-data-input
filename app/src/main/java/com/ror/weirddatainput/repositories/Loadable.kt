package com.ror.weirddatainput.repositories

data class Loadable<out T>(
    val status: LoadableStatus,
    val data: T? = null,
    val exception: Throwable? = null
) {
    companion object {
        fun <T> success(data: T): Loadable<T> {
            return Loadable(LoadableStatus.SUCCESS, data)
        }

        fun <T> error(throwable: Throwable, data: T? = null): Loadable<T> {
            return Loadable(LoadableStatus.ERROR, data, exception = throwable)
        }

        fun <T> loading(): Loadable<T> {
            return Loadable(LoadableStatus.LOADING)
        }
    }
}

enum class LoadableStatus {
    SUCCESS,
    ERROR,
    LOADING
}