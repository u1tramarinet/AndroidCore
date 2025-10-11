package io.github.u1tramarinet.android.core.common.logging.facade

/**
 * ログの窓口インターフェース.
 */
interface LogFacade {
    fun print(level: LogLevel, tag: String, message: String, throwable: Throwable? = null)

    /**
     * ログレベル.
     *
     * https://source.android.com/docs/core/tests/debug/understanding-logging?hl=ja
     */
    enum class LogLevel {
        Error,
        Warn,
        Info,
        Debug,
        Verbose,
        ;
    }
}
