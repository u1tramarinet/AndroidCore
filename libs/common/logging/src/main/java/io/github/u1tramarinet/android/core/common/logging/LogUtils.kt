package io.github.u1tramarinet.android.core.common.logging

import io.github.u1tramarinet.android.core.common.logging.facade.DefaultLogFacade
import io.github.u1tramarinet.android.core.common.logging.facade.LogFacade

object LogUtils {
    private var facade: LogFacade = DefaultLogFacade()

    fun setLogFacade(facade: LogFacade) {
        this.facade = facade
    }

    fun verbose(message: String, throwable: Throwable? = null) {
        print(LogFacade.LogLevel.Verbose, message, throwable)
    }

    fun debug(message: String, throwable: Throwable? = null) {
        print(LogFacade.LogLevel.Debug, message, throwable)
    }

    fun info(message: String, throwable: Throwable? = null) {
        print(LogFacade.LogLevel.Info, message, throwable)
    }

    fun warn(message: String, throwable: Throwable? = null) {
        print(LogFacade.LogLevel.Warn, message, throwable)
    }

    fun error(message: String, throwable: Throwable? = null) {
        print(LogFacade.LogLevel.Error, message, throwable)
    }

    private fun print(
        level: LogFacade.LogLevel,
        message: String,
        throwable: Throwable? = null
    ) {
        facade.print(level, "LogUtils", message, throwable)
    }
}