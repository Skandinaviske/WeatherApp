package com.example.myweatherapp.util

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.SignatureException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class GetData {

    //https://api.seniverse.com/v3/weather/daily.json
    private val TIANQI_API_SECRET_KEY = "S69J9uyzmkgblruE-" //
    private val TIANQI_API_USER_ID = "P0JlWBxcTjNXVY3P2" //

    /**
     * Generate HmacSHA1 signature with given data string and key
     * @param data
     * @param key
     * @return
     * @throws SignatureException
     */
    @Throws(SignatureException::class)
    private fun generateSignature(data: String, key: String): String {
        val result: String
        result = try {
            // get an hmac_sha1 key from the raw key bytes
            val signingKey =
                SecretKeySpec(key.toByteArray(charset("UTF-8")), "HmacSHA1")
            // get an hmac_sha1 Mac instance and initialize with the signing key
            val mac = Mac.getInstance("HmacSHA1")
            mac.init(signingKey)
            // compute the hmac on input data bytes
            val rawHmac = mac.doFinal(data.toByteArray(charset("UTF-8")))
            val base64 =
                Base64.encodeToString(rawHmac, Base64.NO_WRAP)
            base64
        } catch (e: Exception) {
            throw SignatureException("Failed to generate HMAC : " + e.message)
        }
        return result
    }

    /**
     * Generate the URL to get diary weather
     * @param location
     * @param language
     * @param unit
     * @param start
     * @param days
     * @return
     */
    @Throws(SignatureException::class, UnsupportedEncodingException::class)
    fun generateGetDiaryWeatherURL(
        location: String,
        language: String,
        unit: String,
        start: String,
        days: String
    ): String {
        val timestamp = Date().time.toString()
        val params = "ts=$timestamp&ttl=1800&uid=$TIANQI_API_USER_ID"
        URLEncoder.encode(generateSignature(params, TIANQI_API_SECRET_KEY), "UTF-8")
        return "now.json?key=$TIANQI_API_SECRET_KEY&location=$location&language=$language&unit=$unit&start=$start&days=$days/"
    } //https://api.seniverse.com/v3/weather/now.json?key=S69J9uyzmkgblruE-&location=beijing&language=zh-Hans&unit=c
}
