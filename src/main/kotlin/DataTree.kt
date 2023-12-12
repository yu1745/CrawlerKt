package yu17

import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read

data class DataTree(
    val data: ByteArray,
) {
    fun jsonPath(path: String): String? {
        return JsonPath.parse(String(data))?.read<String>(path)
    }

    fun <T> jsonPathArray(path: String): List<T>? {
        return JsonPath.parse(String(data))?.read<List<T>>(path)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataTree

        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}