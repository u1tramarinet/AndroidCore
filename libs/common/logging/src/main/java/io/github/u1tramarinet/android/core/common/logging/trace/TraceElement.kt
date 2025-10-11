package io.github.u1tramarinet.android.core.common.logging.trace

data class TraceElement(
    val fileName: String,
    val lineNumber: Int,
    val methodName: String,
    val className: String,
) {
    companion object {
        fun create(element: StackTraceElement): TraceElement {
            return TraceElement(
                element.fileName,
                element.lineNumber,
                element.methodName,
                element.className,
            )
        }
    }
}
