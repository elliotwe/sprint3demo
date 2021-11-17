package com.example.testrecipeapi

import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.btnSearch)


        btnSearch.setOnClickListener {
            val txtSearchVal = findViewById<TextInputEditText>(R.id.txtSearch)

            val str = txtSearchVal.text.toString()

            doAsync {
                search(str)
            }.execute()

            val txtRes = findViewById<TextView>(R.id.txtResult)

            txtRes.text = "hey1\nyou"
        }

        val answer = JSONObject("""{"name":"test name", "age":25}""")


    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun search(searchval : String)
    {
        try {

            var urlm = "https://api.edamam.com/api/recipes/v2?type=public&q=" + searchval + "&app_id=9e739484&app_key=e3b4d6f7a98479690a4e75d907cd721c%09"
             val url = URL(urlm)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"  // optional default is GET

                println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        println(line)

                        val data = JSONObject(line)


                        val ary = (data["hits"] as JSONArray)

                        var list = mutableListOf("")

                        for (i in 0 until ary.length()) {
                            val item = ary.getJSONObject(i)

                            val rec = item["recipe"]

                            val name = (rec as JSONObject).get("label")

                            list.add(name as String)
                            // Your code here
                        }
                    }
                }
            }
        }
        catch (e : Exception)
        {
            println(e.message)
        }
    }
}

class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}

