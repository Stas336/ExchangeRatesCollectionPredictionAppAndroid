package com.stasl.datacollectionandpredictionfinanceappandroid.activity;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.stasl.datacollectionandpredictionfinanceappandroid.R;
import com.stasl.datacollectionandpredictionfinanceappandroid.adapter.BankListAdapter;
import com.stasl.datacollectionandpredictionfinanceappandroid.api.NBRBAPIExecutioner;
import com.stasl.datacollectionandpredictionfinanceappandroid.api.NBRBAPIParameters;
import com.stasl.datacollectionandpredictionfinanceappandroid.bank.Bank;
import com.stasl.datacollectionandpredictionfinanceappandroid.currency.Currencies;
import com.stasl.datacollectionandpredictionfinanceappandroid.currency.Currency;
import com.stasl.datacollectionandpredictionfinanceappandroid.parser.SelectBYCoursesParser;
import com.stasl.datacollectionandpredictionfinanceappandroid.predict.Predict;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instances;

public class BankListActivity extends AppCompatActivity {
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String predictStartDate = "2016-07-01"; // TOO MANY FOR ANDROID PHONES
    private static final int predictMonthWindow = 1;
    private static final int predictSteps = 1;
    private ListView list;
    private BankListAdapter adapter;
    private static float bestUSDBUY = 0, bestUSDSELL = 0, bestEURBUY = 0, bestEURSELL = 0, bestRUBBUY = 0, bestRUBSELL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        requestExchangeRates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                throw new UnsupportedOperationException();
            case R.id.action_exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void requestExchangeRates()
    {
        List<Bank> banks = null;
        SelectBYCoursesParser selectBYCoursesParser = new SelectBYCoursesParser();
        try {
            banks = selectBYCoursesParser.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (banks != null)
        {
            setUpList(banks);
        }
        else
        {
            Snackbar.make(findViewById(android.R.id.content), R.string.internetWarning, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.snackbar_retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestExchangeRates();
                        }
                    }).setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed))
                    .show();
        }
    }
    private void setUpList(List<Bank> banks)
    {
        adapter = new BankListAdapter(this, banks);
        list = (ListView)findViewById(R.id.List);
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        list.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null, false));
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(df.format(new Date()));
        list.setAdapter(adapter);
        try
        {
            setUpNBRBRates();
            predictExchangeRates();
        }
        catch (Exception e)
        {
            finish();
            e.printStackTrace();
        }
    }
    private void setUpNBRBRates() throws ExecutionException, InterruptedException, ParseException
    {
        SimpleDateFormat date = new SimpleDateFormat(dateFormat);
        for (Currencies currency:Currencies.values())
        {
            NBRBAPIExecutioner executioner = new NBRBAPIExecutioner();
            for (Currency currency1 : executioner.execute(new NBRBAPIParameters(currency.getCode(), date.format(new Date()), date.format(new Date()))).get())
            {
                String value = new BigDecimal(currency1.getCur_OfficialRate()).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
                switch (currency.getCode())
                {
                    case 145: //USD
                        ((TextView) findViewById(R.id.textUSDBUYNBRB)).setText(value);
                        ((TextView) findViewById(R.id.textUSDBUYNBRB)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                        ((TextView) findViewById(R.id.textUSDSELLNBRB)).setText(value);
                        ((TextView) findViewById(R.id.textUSDSELLNBRB)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                        break;
                    case 292: //EUR
                        ((TextView) findViewById(R.id.textEURBUYNBRB)).setText(value);
                        ((TextView) findViewById(R.id.textEURBUYNBRB)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                        ((TextView) findViewById(R.id.textEURSELLNBRB)).setText(value);
                        ((TextView) findViewById(R.id.textEURSELLNBRB)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                        break;
                    case 298: //RUB
                        ((TextView) findViewById(R.id.textRUBBUYNBRB)).setText(value);
                        ((TextView) findViewById(R.id.textRUBBUYNBRB)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                        ((TextView) findViewById(R.id.textRUBSELLNBRB)).setText(value);
                        ((TextView) findViewById(R.id.textRUBSELLNBRB)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                        break;
                }
            }
        }
    }
    private void predictExchangeRates() throws Exception
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
                if (result.values().iterator().next() < getBestUSDBUY())
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
                if (result.values().iterator().next() < getBestEURBUY())
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
                if (result.values().iterator().next() < getBestRUBBUY())
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
