package yu17

import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read

//data class DataTree(
//    val data: ByteArray,
//) {
//    fun jsonPath(path: String): String? {
//        return JsonPath.parse(String(data))?.read<String>(path)
//    }
//
//    fun <T> jsonPathArray(path: String): List<T>? {
//        return JsonPath.parse(String(data))?.read<List<T>>(path)
//    }
//
//    override fun toString(): String {
//        return String(data)
//    }
//}


abstract class Data

fun buildData(data: String): Data {
    //todo 判断是json还是html还是raw
    return JsonData(data)
}

class JsonData(private val data: String) : Data() {

    fun jsonPath(path: String): String? {
        return JsonPath.parse(data)?.read<String>(path)
    }

    fun <T> jsonPathArray(path: String): List<T>? {
        return JsonPath.parse(data)?.read<List<T>>(path)
    }

    override fun toString(): String {
        return data
    }

    fun map(path:String):String?{
        return jsonPath(path)
    }

    fun <T> flatMap(path:String):List<T>?{
        return jsonPathArray(path)
    }
}

class HtmlData(private val html: String) : Data() {
    override fun toString(): String {
        return html
    }
}
