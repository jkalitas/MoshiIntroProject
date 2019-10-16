package com.training.moshiintroproj

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi

class LazyParser(moshi: Moshi) {
    private val personAdapter: JsonAdapter<Person> = moshi.adapter(Person::class.java)

    fun parse(reader: JsonReader): Sequence<Person> {
        return sequence {
            reader.readArray {
                yield(personAdapter.fromJson(reader)!!)
            }
        }
    }

    inline fun JsonReader.readArray(body: () -> Unit) {
        beginArray()
        while (hasNext()) {
            body()
        }
        endArray()
    }
}