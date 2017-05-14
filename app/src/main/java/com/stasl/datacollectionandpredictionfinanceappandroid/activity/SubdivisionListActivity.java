package com.stasl.datacollectionandpredictionfinanceappandroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.stasl.datacollectionandpredictionfinanceappandroid.R;
import com.stasl.datacollectionandpredictionfinanceappandroid.adapter.BankSubdivisionListAdapter;
import com.stasl.datacollectionandpredictionfinanceappandroid.bank.Bank;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SubdivisionListActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        Bank bank = getIntent().getParcelableExtra("bank");
        BankSubdivisionListAdapter adapter = new BankSubdivisionListAdapter(this, bank.getSubdivisionsUnmodifiable());
        ListView list = (ListView)findViewById(R.id.List);
        list.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null, false));
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(BankListActivity.getDateFormat());
        String formattedDate = df.format(c.getTime());
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(formattedDate);
        getSupportActionBar().setTitle(bank.getName());
        list.setAdapter(adapter);
    }
}
