package com.femaaccu.cryotechapp

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import kotlinx.android.synthetic.main.activity_item_details.*
import org.json.JSONException
import java.net.URL

class ItemDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        //le damos forma al binding


        //declaramos los extras para pode rrecoger la información del viewHolder
        val extras = intent.extras
        val currencyName = extras!!.getString("currencyName")

        if (currencyName != null) {
            setCandleStickChart(currencyName)
        }
    }
    fun setCandleStickChart(currencyName: String){

        var url = "https://api.coingecko.com/api/v3/coins/bitcoin/ohlc?vs_currency=eur&days=1"


                    val xvalue = ArrayList<String>()
                    xvalue.add("00:00")
                    xvalue.add("01:00")
                    xvalue.add("02:00")
                    xvalue.add("03:00")
                    xvalue.add("04:00")
                    xvalue.add("05:00")
                    xvalue.add("07:00")
                    xvalue.add("08:00")
                    xvalue.add("09:00")
                    xvalue.add("10:00")
                    xvalue.add("11:00")
                    xvalue.add("12:00")
                    xvalue.add("13:00")
                    xvalue.add("14:00")
                    xvalue.add("15:00")
                    xvalue.add("16:00")
                    xvalue.add("17:00")
                    xvalue.add("18:00")
                    xvalue.add("19:00")
                    xvalue.add("20:00")
                    xvalue.add("21:00")
                    xvalue.add("22:00")
                    xvalue.add("23:00")
                    xvalue.add("24:00")

                    //y values

                    val candlestickentry = ArrayList<CandleEntry>()
                    candlestickentry.add(CandleEntry(0, 255.0f, 219.85f, 224.94f, 226.41f))
                    candlestickentry.add(CandleEntry(1, 255.0f, 266.85f, 277.94f, 288.41f))
                    candlestickentry.add(CandleEntry(2, 266.0f, 177.85f, 288.94f, 299.41f))
                    candlestickentry.add(CandleEntry(3, 310.0f, 320.85f, 340.94f, 350.41f))
                    candlestickentry.add(CandleEntry(4, 400.0f, 419.85f, 440.94f, 460.41f))
                    candlestickentry.add(CandleEntry(5, 600.0f, 600.85f, 400.94f, 300.41f))
                    candlestickentry.add(CandleEntry(6, 400.0f, 550.85f, 224.94f, 226.41f))
                    candlestickentry.add(CandleEntry(7, 255.0f, 266.85f, 277.94f, 288.41f))
                    candlestickentry.add(CandleEntry(8, 266.0f, 177.85f, 288.94f, 299.41f))
                    candlestickentry.add(CandleEntry(9, 310.0f, 320.85f, 340.94f, 350.41f))
                    candlestickentry.add(CandleEntry(10, 400.0f, 419.85f, 440.94f, 460.41f))
                    candlestickentry.add(CandleEntry(11, 600.0f, 700.85f, 800.94f, 300.41f))

                    val candledataset = CandleDataSet(candlestickentry, currencyName)

                    candledataset.color = Color.rgb(80,80,80)
                    candledataset.shadowColor = resources.getColor(R.color.purple_200)
                    candledataset.shadowWidth = 1f
                    candledataset.decreasingColor = Color.RED
                    candledataset.decreasingPaintStyle = Paint.Style.FILL

                    candledataset.increasingColor = Color.GREEN
                    candledataset.increasingPaintStyle = Paint.Style.FILL

                    val candledata = CandleData(xvalue, candledataset)
                    candlechart.data = candledata
                    candlechart.setBackgroundColor(resources.getColor(R.color.white))
                    candlechart.animateXY(2000, 2000)

                    val xval= candlechart.xAxis
                    xval.position = XAxis.XAxisPosition.BOTTOM
                    xval.setDrawGridLines(false)

    }

}