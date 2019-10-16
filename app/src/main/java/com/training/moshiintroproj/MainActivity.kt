package com.training.moshiintroproj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.Okio
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private enum class ParserOption { MANUAL, ADAPTER, LAZY }

    private val parser: ParserOption = ParserOption.LAZY

    //TODO MORE Here: https://medium.com/@BladeCoder/advanced-json-parsing-techniques-using-moshi-and-kotlin-daf56a7b963d
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onResume() {
        super.onResume()

        val asset = applicationContext.assets
        val inputStream: InputStream = asset.open("persons.json")
        val reader = JsonReader.of(Okio.buffer(Okio.source(inputStream)))


        when (parser) {
            ParserOption.MANUAL -> manualParser(reader)
            ParserOption.LAZY -> lazyParser(reader)
            ParserOption.ADAPTER -> adapterParser(reader)
        }
    }

    private fun lazyParser(reader: JsonReader) {
        val moshi: Moshi = Moshi.Builder().build()
        val sequence = LazyParser(moshi).parse(reader)
        val filteredList = sequence.filter { it.age >= 18 }.toList()
    }

    private fun adapterParser(reader: JsonReader) {
        val moshi: Moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Person::class.java)
        val adapter: JsonAdapter<List<Person>> = moshi.adapter(listType)
        val result = adapter.fromJson(reader)
        print(result.toString())
    }

    private fun manualParser(reader: JsonReader) {
        val manualParser = ManualParser()
        val persons: List<Person> = manualParser.parse(reader)
    }
}
