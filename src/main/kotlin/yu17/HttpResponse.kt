package yu17

data class HttpResponse(
    val status: Int,
    val headers: Map<String, List<String>>,
    val body: ByteArray
)