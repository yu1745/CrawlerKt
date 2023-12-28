package yu17

import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read

data class DataTree(
//    val type: Project.Type,
    val data: ByteArray,
) {
    fun jsonPath(path: String): String? {
        return JsonPath.parse(String(data))?.read<String>(path)
    }

    fun <T> jsonPathArray(path: String): List<T>? {
        return JsonPath.parse(String(data))?.read<List<T>>(path)
    }
}