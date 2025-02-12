package io.github.u1tramarinet.android.core.ui.oss

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.RawRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.text.split
import kotlin.text.toInt

/**
 * Libraryマネージャー.
 */
class LibraryManager(private val context: Context) {

    private data class LibraryMetadata(val offset: Int, val length: Int)

    private val librariesMap = mutableMapOf<Library, LibraryMetadata>()

    private val licensesMap = mutableMapOf<LibraryMetadata, License>()

    private var isLibrariesQueried = false

    private val mutex = Mutex()

    suspend fun getLibraries(): List<Library> {
        mutex.withLock {
            if (!isLibrariesQueried) {
                val map = mutableMapOf<Library, LibraryMetadata>()
                queryLibraries().toSortedMap().onEachIndexed { index, entry ->
                    map[Library(index, entry.key)] = entry.value
                }
                librariesMap.putAll(map)
                isLibrariesQueried = true
            }
        }
        return librariesMap.keys.toList()
    }

    suspend fun queryLicense(library: Library): License? {
        return mutex.withLock {
            val metadata = librariesMap[library]
            if (metadata != null) {
                val existing = licensesMap[metadata]
                if (existing == null) {
                    val content = queryLicense(metadata)
                    if (content != null) {
                        val license = License(licensesMap.size, content)
                        licensesMap[metadata] = license
                        license
                    } else {
                        null
                    }
                } else {
                    existing
                }
            } else {
                null
            }
        }
    }

    private suspend fun queryLibraries(): Map<String, LibraryMetadata> {
        return withContext(Dispatchers.IO) {
            val items = mutableMapOf<String, LibraryMetadata>()
            val rawId = getRawResourceId(LIBRARIES_RESOURCE_NAME)
            if (rawId != null) {
                context.resources
                    .openRawResource(rawId)
                    .source()
                    .buffer()
                    .use {
                        var line = it.readUtf8Line()
                        while (line != null) {
                            val (metadata, name) = line.split(" ", limit = 2)
                            val (offset, length) = metadata.split(":").map { it.toInt() }
                            items[name] = LibraryMetadata(offset, length)
                            line = it.readUtf8Line()
                        }
                    }
            }
            items
        }
    }

    private fun queryLicense(metadata: LibraryMetadata): String? {
        val rawId = getRawResourceId(LICENSES_RESOURCE_NAME)
        return if (rawId != null) {
            context.resources
                .openRawResource(rawId)
                .source()
                .buffer()
                .use { source ->
                    source.skip(metadata.offset.toLong())
                    val content = source.readUtf8(metadata.length.toLong())
                    content
                }
        } else null
    }

    @SuppressLint("DiscouragedApi")
    @RawRes
    private fun getRawResourceId(name: String): Int? {
        val id = context.resources.getIdentifier(name, "raw", context.packageName)
        return if (id == 0) null else id
    }

    private companion object {
        private const val LIBRARIES_RESOURCE_NAME = "third_party_license_metadata"
        private const val LICENSES_RESOURCE_NAME = "third_party_licenses"
    }
}
