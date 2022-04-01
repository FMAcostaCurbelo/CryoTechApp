package com.femaaccu.cryotechapp

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import kotlinx.android.synthetic.main.activity_item_details.*

class ItemDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        setCandleStickChart()
    }
    fun setCandleStickChart(){
        //x values
        val xvalue = ArrayList<String>()
        xvalue.add("00:00")
        xvalue.add("01:00")
        xvalue.add("02:00")
        xvalue.add("03:00")
        xvalue.add("04:00")
        xvalue.add("05:00")

        //y values

        val candlestickentry = ArrayList<CandleEntry>()
        candlestickentry.add(CandleEntry(0, 255.0f, 219.85f, 224.94f, 226.41f))
        candlestickentry.add(CandleEntry(1, 255.0f, 266.85f, 277.94f, 288.41f))
        candlestickentry.add(CandleEntry(2, 266.0f, 177.85f, 288.94f, 299.41f))
        candlestickentry.add(CandleEntry(3, 310.0f, 320.85f, 340.94f, 350.41f))
        candlestickentry.add(CandleEntry(4, 400.0f, 419.85f, 440.94f, 460.41f))
        candlestickentry.add(CandleEntry(5, 600.0f, 700.85f, 800.94f, 900.41f))

        val candledataset = CandleDataSet(candlestickentry, "first")

        candledataset.color = Color.rgb(80,80,80)
        candledataset.shadowColor = resources.getColor(R.color.purple_200)
        candledataset.shadowWidth = 1f
        candledataset.decreasingColor = resources.getColor(R.color.teal_200)
        candledataset.decreasingPaintStyle = Paint.Style.FILL

        candledataset.decreasingColor = resources.getColor(R.color.black)
        candledataset.decreasingPaintStyle = Paint.Style.FILL

        val candledata = CandleData(xvalue, candledataset)
        candlechart.data = candledata
        candlechart.setBackgroundColor(resources.getColor(R.color.white))
        candlechart.animateXY(2000, 2000)

        val xval= candlechart.xAxis
        xval.position = XAxis.XAxisPosition.BOTTOM
        xval.setDrawGridLines(false)


    }
}