package paypay.currencycoversion.network

import org.apache.http.conn.ConnectTimeoutException
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@Suppress("unused") // T is used in extending classes
sealed class GenericApiResponse<T> {

    companion object {
        private val TAG: String = "AppDebug"


        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            if(error is TimeoutException ||
                    error is UnknownHostException ||
                    error is ConnectTimeoutException ||
                    error is ConnectException){
                    return ApiErrorResponse( "No network connection")
            }else {
                return ApiErrorResponse(error.message ?: "unknown error")
            }
        }

        fun <T> create(response: Response<T>): GenericApiResponse<T> {


            if(response.isSuccessful){
                val body = response.body()
                if (body == null || response.code() == 204) {
                    return ApiEmptyResponse()
                }
                else {
                    return ApiSuccessResponse(body = body)
                }
            }
            else{
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                return ApiErrorResponse(errorMsg ?: "unknown error")
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : GenericApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : GenericApiResponse<T>() {}

data class ApiErrorResponse<T>(val errorMessage: String) : GenericApiResponse<T>()