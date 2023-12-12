package yu17

import HttpResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


fun main() {
    val p = Project()
    p.config {
//        val A = type("A")
//        val B = type("B")
//        val C = type("C")
//        val D = type("D")
//        val E = type("E")
        val user = type("user")
        val post = type("post")
        user to post useMap {
            it.jsonPathArray<List<Int>>("$.posts")?.map {
                    curlAsync {
                        url = "http://localhost:3000/posts/${it}"
                    }
            }?.map {
                it.await()
            } ?: listOf()
        }
//        B toMulti {
//            D useMap {
//                it
//            }
//            E useMap {
//                it
//            }
//        }
    }

//    submitRequest(curlScope())
//    CountDownLatch(1).await()

}

typealias mapFunction = (suspend mapFunctionScope.(DataTree) -> List<DataTree>)

class mapFunctionScope {
    //    val response: DataTree? = null
    suspend fun async(fn: suspend () -> HttpResponse): Deferred<DataTree> {
        return coroutineScope {
            return@coroutineScope async {
                DataTree(fn().body)
            }
        }
    }
}