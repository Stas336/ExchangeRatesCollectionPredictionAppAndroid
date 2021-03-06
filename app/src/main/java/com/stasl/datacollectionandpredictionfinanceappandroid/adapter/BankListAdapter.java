package com.stasl.datacollectionandpredictionfinanceappandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stasl.datacollectionandpredictionfinanceappandroid.R;
import com.stasl.datacollectionandpredictionfinanceappandroid.activity.SubdivisionListActivity;
import com.stasl.datacollectionandpredictionfinanceappandroid.bank.Bank;

import java.util.List;

public class BankListAdapter extends BaseAdapter implements View.OnClickListener
{
    private Context context;
    private List<Bank> banks;
    private LayoutInflater layoutInflater;

    public BankListAdapter(Context context, List<Bank> banks)
    {
        this.context = context;
        this.banks = banks;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return banks.size();
    }

    @Override
    public Object getItem(int position) {
        return banks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null)
        {
            view = layoutInflater.inflate(R.layout.list_layout, parent, false);
        }
        Bank bank = (Bank)getItem(position);
        TextView bankName = (TextView)view.findViewById(R.id.textName);
        TextView bankUSDBUY = (TextView)view.findViewById(R.id.textUSDBUY);
        TextView bankUSDSELL = (TextView)view.findViewById(R.id.textUSDSELL);
        TextView bankEURBUY = (TextView)view.findViewById(R.id.textEURBUY);
        TextView bankEURSELL = (TextView)view.findViewById(R.id.textEURSELL);
        TextView bankRUBBUY = (TextView)view.findViewById(R.id.textRUBBUY);
        TextView bankRUBSELL = (TextView)view.findViewById(R.id.textRUBSELL);
        bankName.setText(bank.getName());
        bankName.setTag(position);
        bankUSDBUY.setText(String.valueOf(bank.getUSDBUY()));
        if (bank.isUSDBUYBest())
        {
            bankUSDBUY.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            bankUSDBUY.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        bankUSDSELL.setText(String.valueOf(bank.getUSDSELL()));
        if (bank.isUSDSELLBest())
        {
            bankUSDSELL.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            bankUSDSELL.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        bankEURBUY.setText(String.valueOf(bank.getEURBUY()));
        if (bank.isEURBUYBest())
        {
            bankEURBUY.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            bankEURBUY.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        bankEURSELL.setText(String.valueOf(bank.getEURSELL()));
        if (bank.isEURSELLBest())
        {
            bankEURSELL.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            bankEURSELL.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        bankRUBBUY.setText(String.valueOf(bank.getRUBBUY()));
        if (bank.isRUBBUYBest())
        {
            bankRUBBUY.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            bankRUBBUY.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        bankRUBSELL.setText(String.valueOf(bank.getRUBSELL()));
        if (bank.isRUBSELLBest())
        {
            bankRUBSELL.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            bankRUBSELL.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        bankName.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.textName:
                Log.d("Bank clicked", ((TextView)v.findViewById(R.id.textName)).getText().toString());
                Intent intent = new Intent(context, SubdivisionListActivity.class);
                Log.d("id", String.valueOf((v.findViewById(R.id.textName)).getTag()));
                intent.putExtra("bank", banks.get((Integer) ((v.findViewById(R.id.textName)).getTag())));
                context.startActivity(intent);
                break;
        }
    }
}
