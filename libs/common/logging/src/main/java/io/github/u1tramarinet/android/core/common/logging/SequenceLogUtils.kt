package io.github.u1tramarinet.android.core.common.logging

object SequenceLogUtils {
    private const val FUN_IN = "[IN ]"
    private const val FUN_OUT = "[OUT]"

    fun funIn() {
        LogUtils.debug(FUN_IN)
    }

    fun funOut() {
        LogUtils.debug(FUN_OUT)
    }
}
