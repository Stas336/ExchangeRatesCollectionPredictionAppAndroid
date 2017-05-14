package com.stasl.datacollectionandpredictionfinanceappandroid.activity;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stasl.datacollectionandpredictionfinanceappandroid.R;
import com.stasl.datacollectionandpredictionfinanceappandroid.adapter.BankListAdapter;
import com.stasl.datacollectionandpredictionfinanceappandroid.api.NBRBAPIExecutioner;
import com.stasl.datacollectionandpredictionfinanceappandroid.api.NBRBAPIParameters;
import com.stasl.datacollectionandpredictionfinanceappandroid.currency.Currencies;
import com.stasl.datacollectionandpredictionfinanceappandroid.currency.Currency;
import com.stasl.datacollectionandpredictionfinanceappandroid.parser.SelectBYCoursesParser;
import com.stasl.datacollectionandpredictionfinanceappandroid.predict.Predict;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instances;

public class BankListActivity extends AppCompatActivity {
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String predictStartDate = "2016-07-01"; // TOO MANY FOR ANDROID PHONES
    private static final int predictMonthWindow = 1;
    private static final int predictSteps = 1;
    private static float bestUSDBUY = 0, bestUSDSELL = 0, bestEURBUY = 0, bestEURSELL = 0, bestRUBBUY = 0, bestRUBSELL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        BankListAdapter adapter = null;
        SelectBYCoursesParser selectBYCoursesParser = new SelectBYCoursesParser();
        try {
            adapter = new BankListAdapter(this, selectBYCoursesParser.execute().get());
        } catch (InterruptedException | ExecutionException e)
        {
            //Snackbar.make(findViewById(android.R.id.content), "Loading failed...", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        ListView list = (ListView)findViewById(R.id.List);
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        list.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null, false));
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(df.format(new Date()));
        try {
            predict();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        list.setAdapter(adapter);
    }
    public void predict() throws Exception
    {
        SimpleDateFormat date = new SimpleDateFormat(dateFormat);
        for (Currencies currency:Currencies.values())
        {
            Map<String, Double> data = new HashMap<>();
            NBRBAPIExecutioner executioner = new NBRBAPIExecutioner();
            for (Currency currency1:executioner.execute(new NBRBAPIParameters(currency.getCode(), date.format(DateTime.now().minusMonths(predictMonthWindow).toDate()), date.format(new Date()))).get())
            {
                data.put(date.format(date.parse(currency1.getDate())), Double.valueOf(currency1.getCur_OfficialRate()));
            }
            Instances trainingSet = Predict.initializeTrainingSet(data);
            WekaForecaster forecaster = Predict.initializeForecaster(trainingSet);
            Map<String, Double> result = Predict.predict(trainingSet, forecaster, predictSteps);
            Log.d(currency.name(), result.toString());
            if (currency.name().equals("USD"))
            {
                ((TextView)findViewById(R.id.textUSDBUYPredict)).setText(String.valueOf(result.values().iterator().next()));
                ((TextView)findViewById(R.id.textUSDBUYPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                ((TextView)findViewById(R.id.textUSDSELLPredict)).setText(String.valueOf(result.values().iterator().next()));
                ((TextView)findViewById(R.id.textUSDSELLPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                if (result.values().iterator().next() > getBestUSDBUY())
                {
                    ((TextView)findViewById(R.id.textUSDBUYPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }
                if (result.values().iterator().next() > getBestUSDSELL())
                {
                    ((TextView)findViewById(R.id.textUSDSELLPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }
            }
            else if (currency.name().equals("EUR"))
            {
                ((TextView)findViewById(R.id.textEURBUYPredict)).setText(String.valueOf(result.values().iterator().next()));
                ((TextView)findViewById(R.id.textEURBUYPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                ((TextView)findViewById(R.id.textEURSELLPredict)).setText(String.valueOf(result.values().iterator().next()));
                ((TextView)findViewById(R.id.textEURSELLPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                if (result.values().iterator().next() > getBestEURBUY())
                {
                    ((TextView)findViewById(R.id.textEURBUYPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }
                if (result.values().iterator().next() > getBestEURSELL())
                {
                    ((TextView)findViewById(R.id.textEURSELLPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }
            }
            else if (currency.name().equals("RUB"))
            {
                ((TextView)findViewById(R.id.textRUBBUYPredict)).setText(String.valueOf(result.values().iterator().next()));
                ((TextView)findViewById(R.id.textRUBBUYPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                ((TextView)findViewById(R.id.textRUBSELLPredict)).setText(String.valueOf(result.values().iterator().next()));
                ((TextView)findViewById(R.id.textRUBSELLPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                if (result.values().iterator().next() > getBestRUBBUY())
                {
                    ((TextView)findViewById(R.id.textRUBBUYPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }
                if (result.values().iterator().next() > getBestRUBSELL())
                {
                    ((TextView)findViewById(R.id.textRUBSELLPredict)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }
            }
        }
    }
    public static String getDateFormat()
    {
        return dateFormat;
    }
    public static float getBestUSDBUY() {
        return bestUSDBUY;
    }

    public static void setBestUSDBUY(float bestUSDBUY) {
        BankListActivity.bestUSDBUY = bestUSDBUY;
    }

    public static float getBestUSDSELL() {
        return bestUSDSELL;
    }

    public static void setBestUSDSELL(float bestUSDSELL) {
        BankListActivity.bestUSDSELL = bestUSDSELL;
    }

    public static float getBestEURBUY() {
        return bestEURBUY;
    }

    public static void setBestEURBUY(float bestEURBUY) {
        BankListActivity.bestEURBUY = bestEURBUY;
    }

    public static float getBestEURSELL() {
        return bestEURSELL;
    }

    public static void setBestEURSELL(float bestEURSELL) {
        BankListActivity.bestEURSELL = bestEURSELL;
    }

    public static float getBestRUBBUY() {
        return bestRUBBUY;
    }

    public static void setBestRUBBUY(float bestRUBBUY) {
        BankListActivity.bestRUBBUY = bestRUBBUY;
    }

    public static float getBestRUBSELL() {
        return bestRUBSELL;
    }

    public static void setBestRUBSELL(float bestRUBSELL) {
        BankListActivity.bestRUBSELL = bestRUBSELL;
    }
}
