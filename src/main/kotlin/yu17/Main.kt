package yu17

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch


fun main() {
    val p = Project()
    p.config {
        val user = type("user")
        val post = type("post")
        user to post jsonMap {
            it.flatMap<Int>("$.posts")?.map {
                curlAsync {
                    url = "http://debian.wangyu.website:3000/posts/${it}"
                }
            }?.map { it.await() } ?: listOf()
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
            p.type("user"), buildData(
                """{ "id": 1, "name": "typicode","posts": [1, 2, 3] }"""
            )
        )
    }
    CountDownLatch(1).await()


//    submitRequest(curlScope())
//    CountDownLatch(1).await()

}

typealias mapFunction = (suspend Project.mapFunctionScope.(Data) -> List<Data>)
typealias jsonMapFunction = (suspend Project.mapFunctionScope.(JsonData) -> List<Data>)

