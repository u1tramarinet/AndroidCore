package io.github.u1tramarinet.android.core.common.logging.trace

import io.github.u1tramarinet.android.core.common.logging.LogUtils
import io.github.u1tramarinet.android.core.common.logging.facade.LogFacade

object TraceAnalyzer {
    private const val DEBUG = true

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    fun analyze(stackTrace: Array<StackTraceElement> = Throwable().stackTrace): Trace {
        log("analyze[start]")
        val trace = Trace.create(stackTrace.toList())
        trace.forEach { element ->
            log(element.toString())
        }
        log("analyze[end]")
        return trace
    }

    private fun log(message: String) {
        if (DEBUG) {
            val facade = LogUtils.getLogFacade()
            facade.print(level = LogFacade.LogLevel.Debug, tag = "TraceAnalyzer", message)
        }
    }
}
