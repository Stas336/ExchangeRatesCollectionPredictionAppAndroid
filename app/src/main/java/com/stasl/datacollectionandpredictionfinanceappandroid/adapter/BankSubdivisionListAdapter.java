package com.stasl.datacollectionandpredictionfinanceappandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stasl.datacollectionandpredictionfinanceappandroid.R;
import com.stasl.datacollectionandpredictionfinanceappandroid.bank.Subdivision;

import java.util.List;

public class BankSubdivisionListAdapter extends BaseAdapter
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
        TextView bankName = (TextView)view.findViewById(R.id.textName);
        TextView bankUSDBUY = (TextView)view.findViewById(R.id.textUSDBUY);
        TextView bankUSDSELL = (TextView)view.findViewById(R.id.textUSDSELL);
        TextView bankEURBUY = (TextView)view.findViewById(R.id.textEURBUY);
        TextView bankEURSELL = (TextView)view.findViewById(R.id.textEURSELL);
        TextView bankRUBBUY = (TextView)view.findViewById(R.id.textRUBBUY);
        TextView bankRUBSELL = (TextView)view.findViewById(R.id.textRUBSELL);
        bankName.setText(subdivision.getName());
        bankName.setTag(position);
        bankUSDBUY.setText(String.valueOf(subdivision.getUSDBUY()));
        bankUSDSELL.setText(String.valueOf(subdivision.getUSDSELL()));
        bankEURBUY.setText(String.valueOf(subdivision.getEURBUY()));
        bankEURSELL.setText(String.valueOf(subdivision.getEURSELL()));
        bankRUBBUY.setText(String.valueOf(subdivision.getRUBBUY()));
        bankRUBSELL.setText(String.valueOf(subdivision.getRUBSELL()));
        //bankName.setOnClickListener(this);
        return view;
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.textName:
                Log.d("Bank clicked", ((TextView)v.findViewById(R.id.textName)).getText().toString());
                Intent intent = new Intent(context, SubdivisionActivity.class);
                Log.d("id", String.valueOf((v.findViewById(R.id.textName)).getTag()));
                intent.putExtra("bank", banks.get((Integer) ((v.findViewById(R.id.textName)).getTag())));
                context.startActivity(intent);
                break;
        }
    }*/
}
