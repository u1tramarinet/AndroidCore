package io.github.u1tramarinet.android.core.common.logging.facade

import android.util.Log

/**
 *
 */
class DefaultLogFacade : LogFacade {
    override fun print(
        level: LogFacade.LogLevel,
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        when (level) {
            LogFacade.LogLevel.Error -> Log.e(tag, message, throwable)
            LogFacade.LogLevel.Warn -> Log.w(tag, message, throwable)
            LogFacade.LogLevel.Info -> Log.i(tag, message, throwable)
            LogFacade.LogLevel.Debug -> Log.d(tag, message, throwable)
            else -> Log.v(tag, message, throwable)
        }
    }
}
