package eu.maxkim.boredombuster1.activity.framework.api

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import eu.maxkim.boredombuster1.activity.model.Activity

@Suppress("unused")
class ActivityTypeAdapter {
    @ToJson
    fun toJson(type: Activity.Type): String {
        return type.toString().lowercase()
    }

    @FromJson
    fun fromJson(string: String): Activity.Type {
        return Activity.Type.valueOf(string.replaceFirstChar(Char::uppercase))
    }
}