package com.seweryn.smogapp.tools.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.seweryn.smogapp.data.model.City
import java.lang.reflect.Type

class CityDeserializer : JsonDeserializer<City?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): City? {
        val jsonObject = json?.asJsonObject
        return jsonObject?.let {
            val name = jsonObject.get("name").asString
            val provinceName = it.get("commune").asJsonObject
                .get("provinceName").asString
            City(name, provinceName)
        }
    }

}