package com.example.conversordedolar

import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {

    var baseCurrency = "USD"
    var convertedToCurrency = "BRL"
    var conversionRate = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        spinnerSetup()
        textChangedStuff()

    }

    private fun textChangedStuff() {
        txtWrite.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    getApiResult()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Escreva um valor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Main", "Before Text Changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Main", "OnTextChanged")
            }

        })

    }

    private fun getApiResult(){
        if (txtWrite != null && txtWrite.text.isNotBlank() && txtWrite.text.isNotEmpty()){

            val API = "https://api.ratesapi.io/api/latest?base=$baseCurrency&symbols=$convertedToCurrency"

            if (baseCurrency == convertedToCurrency){
                Toast.makeText(applicationContext,"Não é possível converter para a mesma moeda", Toast.LENGTH_SHORT).show()
            } else{
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val apiResult = URL(API).readText()
                        val jsonObject = JSONObject(apiResult)

                        conversionRate = jsonObject.getJSONObject("rates").getString(convertedToCurrency).toFloat()

                        Log.d("Main", "$conversionRate")
                        Log.d("Main", apiResult)

                        withContext(Dispatchers.Main){
                            val text = ((txtWrite.text.toString().toFloat()) * conversionRate).toString()
                            textView?.setText(text)

                        }
                    } catch (e:Exception){
                        Log.e("Main", "$e")
                    }
                }
            }
        }
    }

    private fun spinnerSetup(){
        val spinner: Spinner = findViewById(R.id.spinner1)
        val spinner2: Spinner = findViewById(R.id.spinner2)

        ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter= adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.currencies2,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter= adapter
        }

        spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                baseCurrency = parent?.getItemAtPosition(position).toString()
                getApiResult()
            }

        })

        spinner2.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                convertedToCurrency = parent?.getItemAtPosition(position).toString()
                getApiResult()
            }

        })
    }

}

