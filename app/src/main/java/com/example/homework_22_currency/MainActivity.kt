package com.example.homework_22_currency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import com.example.homework_22_currency.BuildConfig.API_KEY
import com.example.homework_22_currency.data.model.CurrencyModel
import com.example.homework_22_currency.data.remote.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private val values = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListeners()
        setupNetwork()
    }

    private fun setupNetwork() {
        fetchCurrencies()
    }

    private fun setupListeners() {
        et_left?.doAfterTextChanged {
            if (it.toString().isNotEmpty())
                calculate(it.toString())
        }
    }

    private fun calculate(value: String) {
        val result = value.toDouble() * values[right_spinner.selectedItemPosition].toDouble()
        et_right.setText(result.toString())
    }

    private fun fetchCurrencies() {
        RetrofitBuilder.getService()?.getCurrencies(API_KEY)
            ?.enqueue(object : Callback<CurrencyModel> {


                override fun onFailure(call: Call<CurrencyModel>, t: Throwable) {
                    Log.d("asdasd", "nope")
                }

                override fun onResponse(
                    call: Call<CurrencyModel>,
                    response: Response<CurrencyModel>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()
                        workWithData(data)
                    }

                }

            })
    }

    private fun workWithData(data: CurrencyModel?) {
        val keys = data?.rates?.keySet()?.toList()

        if (keys != null) {
            for (item in keys)
                values.add(data.rates.get(item).toString())
            Log.d("test", "ok")
        }
        val adapter = CurrencyAdapter(applicationContext, R.layout.item_spinner, keys!!)
        left_spinner.adapter = adapter
        right_spinner.adapter = adapter

        left_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

        }
        right_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (et_right.text.toString().isNotEmpty())
                    calculate(et_right.text.toString())

            }

        }
    }


}