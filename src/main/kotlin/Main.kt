package yu17

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch


fun main() {
//    exitProcess(0)
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
            it.jsonPathArray<Int>("$.posts")?.map {
//                println("http://localhost:3000/posts/${it}")
                curlAsync {
                    url = "http://localhost:3000/posts/${it}"
                }
            }?.map {
                val rt = it.await()
                println("await done")
                return@map rt
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
    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        p.start(
            p.type("user"), DataTree(
                """{ "id": 1, "name": "typicode","posts": [1, 2, 3] }""".toByteArray()
            )
        )
    }
    CountDownLatch(1).await()


//    submitRequest(curlScope())
//    CountDownLatch(1).await()

}

typealias mapFunction = (suspend Project.mapFunctionScope.(DataTree) -> List<DataTree>)

