package common

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import taro.Taro
import taro.Taro.SuccessCallbackResult
import taro.taro.network.RequestOption
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class HttpClient(
    private val defaultHeaders: Map<String, String> = emptyMap(),
) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun <REQ, RESP> request(
        method: String,
        url: String,
        headers: Map<String, String?> = emptyMap(),
        data: REQ? = null,
        responseType: KType? = null,
    ): Response<RESP> {
        val option = buildOption(method, url, headers, data)
        val result: SuccessCallbackResult<String?> = Taro.request(option)
        return result.toResponse(responseType)
    }

    @ExperimentalStdlibApi
    suspend inline fun <reified RESP> get(
        url: String,
        headers: Map<String, String?> = emptyMap(),
    ): Response<RESP> {
        return request("GET", url, headers, null, typeOf<RESP>())
    }

    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    private fun <REQ> buildOption(
        method: String,
        url: String,
        headers: Map<String, String?>,
        data: REQ?,
    ): RequestOption<REQ> {
        val option = (js("{}") as RequestOption<REQ>)
        option.method = method
        option.url = url
        option.header = buildHeaders(defaultHeaders, headers) as? Any
        option.data = data
        option.dataType = "string" // do not parse JSON
        option.responseType = "text"
        return option
    }

    class Response<RESP>(
        val status: Int,
        val headers: Map<String, String>,
        val data: RESP?,
    )

    class Builder {
        var defaultHeaders = mutableMapOf<String, String>()
        fun build(): HttpClient {
            return HttpClient(
                defaultHeaders = defaultHeaders.toMap()
            )
        }
    }

    companion object {
        operator fun invoke(init: Builder.() -> Unit): HttpClient = Builder().apply(init).build()
    }


    private fun buildHeaders(
        defaultHeaders: Map<String, String>,
        headers: Map<String, String?>,
    ): dynamic {
        val result = js("{}")
        defaultHeaders.forEach {
            result[it.key] = it.value
        }
        headers.forEach {
            if (it.value == null) {
                delete(result[it.key])
            } else {
                result[it.key] = it.value
            }
        }
        return result
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> SuccessCallbackResult<String?>.toResponse(responseType: KType?): Response<T> {
        val data: T? = if (this.data != null && responseType != null) {
            val deserializer = json.serializersModule.serializer(responseType)
            json.decodeFromString(deserializer, this.data as String) as? T
        } else null
        val headers: Map<String, String> = if (header == undefined || header == null) {
            emptyMap()
        } else {
            try {
                val h = header
                val headersJson = js("JSON.stringify(h)") as String
                val root = json.parseToJsonElement(headersJson) as JsonObject
                root.entries.map {
                    it.key to it.value.toString()
                }.toMap()
            } catch (ex: Exception) {
                ex.printStackTrace()
                emptyMap()
            }
        }
        return Response(
            status = statusCode,
            headers = headers,
            data = data,
        )
    }
}

external fun delete(p: dynamic): Boolean = definedExternally
