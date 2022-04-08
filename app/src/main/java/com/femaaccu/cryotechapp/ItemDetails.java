package com.femaaccu.cryotechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.femaaccu.cryotechapp.databinding.ActivityItemDetailsBinding;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemDetails extends AppCompatActivity {
    private ActivityItemDetailsBinding binding;
    Bundle extras;
    Button bDay, bWeek, bMonth, bYear, bMax;
    ImageView arrowImage, tviconImage;
    TextView tvchange;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());

        extras = getIntent().getExtras();
        View view = binding.getRoot();
        setContentView(view);

        String currencyId = extras.getString("currencyId");


        bDay = binding.buttonChartDay;
        bWeek = binding.buttonChartWeek;
        bMonth = binding.buttonChartMonth;
        bYear = binding.buttonChartYear;
        bMax = binding.buttonChartMax;
        arrowImage = binding.IDDetailArrow;
        tvchange = binding.idTextViewChange;
        tviconImage = binding.TViconImage;

        setCandleStickChart(currencyId, "1");

        bDay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setCandleStickChart(currencyId, "1");
            }
        });
        bWeek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setCandleStickChart(currencyId, "7");
            }
        });
        bMonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                setCandleStickChart(currencyId, "30");
            }
        });
        bYear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                setCandleStickChart(currencyId, "365");
            }
        });
        bMax.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                setCandleStickChart(currencyId, "max");
            }
        });

    }

    public void setCandleStickChart(String currencyID, String days){


        String url = "https://api.coingecko.com/api/v3/coins/"+currencyID+"/ohlc?vs_currency=eur&days="+days;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                   @Override
                    public void onResponse(JSONArray response) {
                        // display response

                        ArrayList<CandleEntry> candlestickentry = new ArrayList<>();
                        ArrayList<String> xvalue = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONArray array = response.getJSONArray(i);
                                String epochTime = array.getString(0);
                                Date time = new Date(Long.parseLong(epochTime));
                                dateObject realTime= new dateObject(time.toString());
                                //Toast.makeText(ItemDetails.this, "el tiempo en string es  "+tiempo, Toast.LENGTH_LONG).show();

                                String open = array.getString(1);
                                String high = array.getString(2);
                                String low = array.getString(3);
                                String close = array.getString(4);

                                float floatOpen = Float.parseFloat(open);
                                float floatHigh = Float.parseFloat(high);
                                float floatLow = Float.parseFloat(low);
                                float floatClose = Float.parseFloat(close);

                                switch (days){
                                    case "1":
                                        xvalue.add(realTime.hour+":"+realTime.minute);
                                        candlestickentry.add(new CandleEntry(i, floatHigh, floatLow, floatOpen, floatClose));
                                        break;
                                    case "7":
                                        xvalue.add(realTime.dayName+" "+realTime.dayNumber);
                                        candlestickentry.add(new CandleEntry(i, floatHigh, floatLow, floatOpen, floatClose));
                                        break;
                                    case "30":
                                    case "365":
                                        xvalue.add(realTime.dayNumber+" "+realTime.monthName);
                                        candlestickentry.add(new CandleEntry(i, floatHigh, floatLow, floatOpen, floatClose));

                                        break;
                                    case "max":
                                        xvalue.add(realTime.monthName+" "+realTime.year);
                                        candlestickentry.add(new CandleEntry(i, floatHigh, floatLow, floatOpen, floatClose));
                                        break;
                                    default:
                                        xvalue.add("error not API data");
                                        break;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        CandleEntry ceObj = candlestickentry.get(0);
                        float iniValue = ceObj.getHigh();
                        ceObj = candlestickentry.get(candlestickentry.size()-1);
                        float lastValue = ceObj.getHigh();

                        if (days.equals("1")){
                            double changerate = extras.getDouble("changeRate");
                            tvchange.setText("%"+df2.format(changerate));
                            if (changerate >= 0){
                                arrowImage.setImageResource(R.drawable.greenarrow);
                            }else{
                                arrowImage.setImageResource(R.drawable.redarrowdown);
                            }

                        }else{

                            if (lastValue>=iniValue){
                                arrowImage.setImageResource(R.drawable.greenarrow);
                            }else{
                                arrowImage.setImageResource(R.drawable.redarrowdown);
                            }
                            float changerate = (((lastValue * 100)/iniValue)-100);
                            String formatValue = "%"+df2.format(changerate);
                            tvchange.setText(formatValue);
                        }
                        String currencyname = extras.getString("currencyName");
                        String iconImage = extras.getString("iconImage");

                        Picasso.get().load(iconImage).into(tviconImage);
                        CandleDataSet candledataset = new CandleDataSet(candlestickentry, currencyname);

                        candledataset.setColor(Color.rgb(80,80,80));
                        candledataset.setShadowColor(android.R.color.holo_blue_bright);
                        candledataset.setShadowWidth(1f);
                        candledataset.setDecreasingColor(Color.RED);
                        candledataset.setDecreasingPaintStyle(Paint.Style.FILL);
                        candledataset.setIncreasingColor(Color.GREEN);
                        candledataset.setIncreasingPaintStyle(Paint.Style.FILL);

                        CandleData candledata = new CandleData(xvalue, candledataset);

                        CandleStickChart candlechart = binding.candlechart;
                        candlechart.setData(candledata);
                        candlechart.setBackgroundColor(Color.WHITE);
                        candlechart.animateXY(1500,1000);
                        XAxis xval = candlechart.getXAxis();
                        xval.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xval.setDrawGridLines(false);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ItemDetails.this, "ERROR IS"+error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
             // add it to the RequestQueue
            queue.add(getRequest);
    }
    private class dateObject {
        //Thu Apr 07 13:30:00 GMT+02:00 2022
        String date;
        String dayName;
        String monthName;
        String dayNumber;
        String hour;
        String minute;
        String year;

        public dateObject(String date){
            this.date = date;
            this.dayName = makedayName(date);
            this.monthName = makemonthName(date);
            this.dayNumber = makedayNumber(date);
            this.hour = makeHour(date);
            this.minute = makeMinute(date);
            this.year = makeYear(date);
        }
        private String makedayName(String date){
            String dName = date.substring(0, 3);
            return dName;
        }
        private String makemonthName(String date){
            String mName = date.substring(4, 7);
            return mName;
        }
        private String makedayNumber(String date){
            String dyNumber = date.substring(8, 10);
            return dyNumber;
        }
        private String makeHour(String date){
            String mhour = date.substring(11, 13);
            return mhour;
        }
        private String makeMinute(String date){
            String mMinute = date.substring(14, 16);
            return mMinute;
        }
        private String makeYear(String date){
            String mYear = "Error";
            if (date.length()>33)
            mYear = date.substring(30, 34);
            return mYear;
        }
    }


}