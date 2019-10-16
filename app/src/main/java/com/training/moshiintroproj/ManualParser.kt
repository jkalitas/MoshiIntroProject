package com.training.moshiintroproj

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader


class ManualParser {
    companion object {
        val NAMES = JsonReader.Options.of("id", "name", "age")
    }

    fun parse(reader: JsonReader): List<Person> {
        return reader.readArrayToList {

            var id: Long = -1L
            var name: String = ""
            var age: Int = -1

            reader.readObject {
                when (reader.selectName(NAMES)) {
                    0 -> id = reader.nextLong()
                    1 -> name = reader.nextString()
                    2 -> age = reader.nextInt()
                    else -> reader.skipNameAndValue()
                }
            }

            if (id == -1L || name == "") {
                throw JsonDataException("Missing required field")
            }
            Person(id, name, age)
        }
    }

    fun JsonReader.skipNameAndValue() {
        skipName()
        skipValue()
    }

    inline fun JsonReader.readObject(body: () -> Unit) {
        beginObject()
        while (hasNext()) {
            body()
        }
        endObject()
    }

    inline fun JsonReader.readArray(body: () -> Unit) {
        beginArray()
        while (hasNext()) {
            body()
        }
        endArray()
    }

    inline fun <T : Any> JsonReader.readArrayToList(body: () -> T?): List<T> {
        val result = mutableListOf<T>()

        beginArray()
        while (hasNext()) {
            body()?.let { result.add(it) }
        }
        endArray()

        return result
    }
}