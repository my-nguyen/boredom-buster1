package eu.maxkim.boredombuster1.model

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T): Result<T>()
    data class Error(val error: Exception): Result<Nothing>()
}

inline fun <T, R : Any> T.runCatching(block: T.() -> R): Result<R> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(e)
    }
}