package com.training.moshiintroproj

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Person(val id: Long, val name: String, val age: Int = -1)