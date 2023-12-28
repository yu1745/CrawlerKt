package yu17

import HttpResponse
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.*
import kotlinx.coroutines.launch
import yu17.Project.curlScope
import java.net.URI

val clientNioEventLoopGroup = NioEventLoopGroup()

//val httpClientTriggerScope = CoroutineScope(Dispatchers.Default)

class HttpResponseHandler(private val curl: curlScope) : SimpleChannelInboundHandler<FullHttpResponse>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: FullHttpResponse) {
        println("HttpResponseHandler.channelRead0")
        curl.getScope().launch {
            curl.channel.send(
                HttpResponse(
                    msg.status().code(),
                    msg.headers().names().associateWith { msg.headers().getAll(it) },
                    msg.content().array()
                )
            )
        }
        ctx.close()
    }
}

fun submitRequest(curl: curlScope) {
    try {
        val bootstrap = Bootstrap().apply {
            group(clientNioEventLoopGroup)
            channel(NioSocketChannel::class.java)
            handler(object : ChannelInitializer<Channel>() {
                public override fun initChannel(ch: Channel) {
                    ch.pipeline().apply {
                        addLast(HttpClientCodec())
                        addLast(HttpObjectAggregator(1024 * 1024))
                        addLast(HttpResponseHandler(curl))
                    }
                }
            })
        }


        val uri = URI.create(curl.url)
        val content = Unpooled.copiedBuffer(curl.body)
        val request: HttpRequest = DefaultFullHttpRequest(
            HttpVersion.HTTP_1_1,
            curl.method,
            uri.path,
            content,
        )
        request.headers().apply {
//            set(HttpHeaderNames.HOST, uri.host+":"+uri.port)
//            set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
            set("Host", uri.host+":"+uri.port)
            set("Connection", HttpHeaderValues.CLOSE)

//            set(HttpHeaderNames.ACCEPT_ENCODING, listOf(HttpHeaderValues.GZIP, HttpHeaderValues.DEFLATE).joinToString(","))
        }

        bootstrap.connect(uri.host, uri.port).addListener(ChannelFutureListener {
            it.channel().writeAndFlush(request)
        })
    } catch (ignored: Exception) {

    }
}