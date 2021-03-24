package com.example.conversordedolar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btCalcular = btnCalculator



        btCalcular.setOnClickListener {
            val calculo = Integer.parseInt(txtWrite.text.toString())
            val resultado: Double = calculo / 5.51
            val textofinal = "A CONVERSÂO DO DÓLAR HOJE É  $resultado"
            textView.text = textofinal
        }


    }


}