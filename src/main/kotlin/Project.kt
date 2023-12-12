package yu17

import HttpResponse
import Path
import io.netty.handler.codec.http.HttpMethod
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope

class Project {
    private val types = ArrayList<Type>()
    val maps = HashMap<Path, mapFunction>()
    var liveConns = 0

    inner class Type(val name: String) {
        infix fun to(other: Type): Path {
            // 将path注册到paths
            return Path(this, other)
        }

        infix fun toMulti(other: toMultiScope.() -> Unit) {
            other.invoke(toMultiScope(this))
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Type

            return name == other.name
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }


        override fun toString(): String {
            return name;
        }
    }

    inner class toMultiScope(private val type: Type) {
        infix fun Type.useMap(other: mapFunction) {
            maps[Path(type, this)] = other
        }
    }

    inner class curlScope {
        var url: String = ""
        var method: HttpMethod = HttpMethod.GET
        var body: ByteArray = ByteArray(0)
        var headers: Map<String, List<String>> = HashMap()
        var timeout: Long = 1000L
        var localAddr: String? = null
        var proxyAddr: String? = null
        var channel: Channel<HttpResponse> = Channel()
//        var response: HttpResponse? = null
    }

    fun type(s: String): Type {
        if (!types.contains(Type(s))) {
            types.add(Type(s))
        }
        return Type(s)
    }


    fun config(it: Project.() -> Unit) {
        it()
        maps.forEach {
            println("${it.key} : ${it.value}")
        }
    }

    infix fun Path.useMap(mapFunction: mapFunction) {
        maps[this] = mapFunction
    }

    //todo 统计并发数，执行时+1，完成时-1
    suspend fun curl(it: curlScope.() -> Unit): HttpResponse {
        val scope = curlScope()
        it.invoke(scope)
        submitRequest(scope)
        return scope.channel.receive()
    }

    suspend fun curlAsync(it: curlScope.() -> Unit): Deferred<DataTree> {
        val scope = curlScope()
        it.invoke(scope)
        submitRequest(scope)
        return coroutineScope {
            async {
                DataTree(scope.channel.receive().body)
            }
        }
    }

    fun start(type: Type, initData: DataTree) {
        maps.keys.filter { it.from == type }.forEach {
            maps[it].invoke(mapFunctionScope(), initData)
        }
    }
}