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
import com.stasl.datacollectionandpredictionfinanceappandroid.activity.SubdivisionInfoActivity;
import com.stasl.datacollectionandpredictionfinanceappandroid.bank.Subdivision;

import java.util.List;

public class BankSubdivisionListAdapter extends BaseAdapter implements View.OnClickListener
{
    private Context context;
    private List<Subdivision> subdivisions;
    private LayoutInflater layoutInflater;

    public BankSubdivisionListAdapter(Context context, List<Subdivision> subdivisions)
    {
        this.context = context;
        this.subdivisions = subdivisions;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subdivisions.size();
    }

    @Override
    public Object getItem(int position) {
        return subdivisions.get(position);
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
        Subdivision subdivision = (Subdivision) getItem(position);
        TextView subdivisionName = (TextView)view.findViewById(R.id.textName);
        TextView subdivisionUSDBUY = (TextView)view.findViewById(R.id.textUSDBUY);
        TextView subdivisionUSDSELL = (TextView)view.findViewById(R.id.textUSDSELL);
        TextView subdivisionEURBUY = (TextView)view.findViewById(R.id.textEURBUY);
        TextView subdivisionEURSELL = (TextView)view.findViewById(R.id.textEURSELL);
        TextView subdivisionRUBBUY = (TextView)view.findViewById(R.id.textRUBBUY);
        TextView subdivisionRUBSELL = (TextView)view.findViewById(R.id.textRUBSELL);
        subdivisionName.setText(subdivision.getName());
        subdivisionName.setTag(position);
        subdivisionUSDBUY.setText(String.valueOf(subdivision.getUSDBUY()));
        if (subdivision.isUSDBUYBest())
        {
            subdivisionUSDBUY.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            subdivisionUSDBUY.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        subdivisionUSDSELL.setText(String.valueOf(subdivision.getUSDSELL()));
        if (subdivision.isUSDSELLBest())
        {
            subdivisionUSDSELL.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            subdivisionUSDSELL.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        subdivisionEURBUY.setText(String.valueOf(subdivision.getEURBUY()));
        if (subdivision.isEURBUYBest())
        {
            subdivisionEURBUY.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            subdivisionEURBUY.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        subdivisionEURSELL.setText(String.valueOf(subdivision.getEURSELL()));
        if (subdivision.isEURSELLBest())
        {
            subdivisionEURSELL.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            subdivisionEURSELL.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        subdivisionRUBBUY.setText(String.valueOf(subdivision.getRUBBUY()));
        if (subdivision.isRUBBUYBest())
        {
            subdivisionRUBBUY.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            subdivisionRUBBUY.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        subdivisionRUBSELL.setText(String.valueOf(subdivision.getRUBSELL()));
        if (subdivision.isRUBSELLBest())
        {
            subdivisionRUBSELL.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else
        {
            subdivisionRUBSELL.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }
        subdivisionName.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.textName:
                Log.d("Bank clicked", ((TextView)v.findViewById(R.id.textName)).getText().toString());
                Intent intent = new Intent(context, SubdivisionInfoActivity.class);
                Log.d("id", String.valueOf((v.findViewById(R.id.textName)).getTag()));
                intent.putExtra("name", subdivisions.get((Integer) ((v.findViewById(R.id.textName)).getTag())).getName());
                intent.putExtra("address", subdivisions.get((Integer) ((v.findViewById(R.id.textName)).getTag())).getAddress());
                context.startActivity(intent);
                break;
        }
    }
}
