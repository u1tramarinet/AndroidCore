package io.github.u1tramarinet.android.core.common.logging.trace

class Trace(
    private val elements: List<TraceElement>
) {
    fun forEach(action: (TraceElement) -> Unit) = elements.forEach(action)

    fun <T> map(transform: (TraceElement) -> T) = elements.map(transform)

    fun filter(predicate: (TraceElement) -> Boolean) = elements.filter(predicate)

    companion object {
        fun create(stackTrace: List<StackTraceElement>): Trace {
            return Trace(elements = stackTrace.map {
                TraceElement.create(it)
            }
            )
        }
    }
}
