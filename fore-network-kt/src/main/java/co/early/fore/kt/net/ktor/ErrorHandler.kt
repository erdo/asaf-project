package co.early.fore.kt.net.ktor

import co.early.fore.net.MessageProvider

/**
 *
 * @param <F> Failure message class to be used throughout the app, such as an enum
</F> */
interface ErrorHandler<F> {
    /**
     *
     * @param t throwable that caused the error, may be null
     * @param customErrorClazz custom error class expected from the errorResponse, may be null
     * @param <CE> class type of the custom error, may be null
     * @return the parsed error from the server
    </CE> */
    suspend fun <CE : MessageProvider<F>> handleError(t: Throwable, customErrorClazz: Class<CE>?): F
}