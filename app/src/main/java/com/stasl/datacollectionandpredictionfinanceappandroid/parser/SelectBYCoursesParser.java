package com.stasl.datacollectionandpredictionfinanceappandroid.parser;


import android.os.AsyncTask;
import android.util.Log;

import com.stasl.datacollectionandpredictionfinanceappandroid.activity.BankListActivity;
import com.stasl.datacollectionandpredictionfinanceappandroid.bank.Bank;
import com.stasl.datacollectionandpredictionfinanceappandroid.bank.Subdivision;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectBYCoursesParser extends AsyncTask<Void, Void, List<Bank>>
{
    private static final String baseURL = "http://select.by/kurs/";
    private static final String parseElement = "td";

    public static List<Bank> getBanks() throws IOException
    {
        List<Bank> banks = new ArrayList<>();
        Document html = Jsoup.connect(baseURL).get();
        Elements elements = html.select(parseElement);
        for (int i = 1;i < elements.size();i++)
        {
            Bank bank = new Bank(elements.get(i++).child(0).text());
            Log.d("Parser", "Bank name " + bank.getName());
            bank.setUSDBUY(Float.valueOf(elements.get(i++).text().replace(",",".")));
            bank.setUSDSELL(Float.valueOf(elements.get(i++).text().replace(",",".")));
            bank.setEURBUY(Float.valueOf(elements.get(i++).text().replace(",",".")));
            bank.setEURSELL(Float.valueOf(elements.get(i++).text().replace(",",".")));
            bank.setRUBBUY(Float.valueOf(elements.get(i++).text().replace(",",".")));
            bank.setRUBSELL(Float.valueOf(elements.get(i++).text().replace(",",".")));
            i++;
            while (!elements.get(i).attributes().hasKey("rowspan"))
            {
                Subdivision subdivision = new Subdivision();
                subdivision.setName(elements.get(i).text().substring(2, elements.get(i).text().indexOf(",")));
                subdivision.setAddress(elements.get(i).text().substring(elements.get(i).text().indexOf(",") + 2, elements.get(i).text().length()));
                i++;
                subdivision.setUSDBUY(Float.valueOf(elements.get(i++).text().replace(",",".")));
                subdivision.setUSDSELL(Float.valueOf(elements.get(i++).text().replace(",",".")));
                subdivision.setEURBUY(Float.valueOf(elements.get(i++).text().replace(",",".")));
                subdivision.setEURSELL(Float.valueOf(elements.get(i++).text().replace(",",".")));
                subdivision.setRUBBUY(Float.valueOf(elements.get(i++).text().replace(",",".")));
                subdivision.setRUBSELL(Float.valueOf(elements.get(i++).text().replace(",",".")));
                Log.d("Parser", "Bank subdivision name " + subdivision.getName());
                Log.d("Parser", "Bank subdivision address " + subdivision.getAddress());
                Log.d("Parser", "USD BUY " + subdivision.getUSDBUY());
                Log.d("Parser", "USD SELL " + subdivision.getUSDSELL());
                Log.d("Parser", "EUR BUY " + subdivision.getEURBUY());
                Log.d("Parser", "EUR SELL " + subdivision.getEURSELL());
                Log.d("Parser", "RUB BUY " + subdivision.getRUBBUY());
                Log.d("Parser", "RUB SELL " + subdivision.getRUBSELL());
                bank.addSubdivision(subdivision);
                if (subdivision.getName().equals("ПРК №36")) // BUG ON WEBSITE
                {
                    i += 9;
                }
                /*if (bank.getName().equals("Цептер Банк"))
                {
                    i = elements.size() - 1;
                    break;
                }*/
                if (bank.getName().equals("ТК Банк"))
                {
                    i = elements.size() - 1;
                    break;
                }
                i++;
            }
            banks.add(bank);
        }
        banks = showBestCourses(banks);
        return banks;
    }
    private static List<Bank> showBestCourses(List<Bank> banks)
    {
        boolean isFirst = true;
        for (Bank bank:banks)
        {
                if (isFirst)
                {
                    BankListActivity.setBestUSDBUY(bank.getUSDBUY());
                    BankListActivity.setBestUSDSELL(bank.getUSDSELL());
                    BankListActivity.setBestEURBUY(bank.getEURBUY());
                    BankListActivity.setBestEURSELL(bank.getEURSELL());
                    BankListActivity.setBestRUBBUY(bank.getRUBBUY());
                    BankListActivity.setBestRUBSELL(bank.getRUBSELL());
                    isFirst = false;
                }
                if (BankListActivity.getBestUSDBUY() < bank.getUSDBUY())
                {
                    BankListActivity.setBestUSDBUY(bank.getUSDBUY());
                }
                if (BankListActivity.getBestUSDSELL() > bank.getUSDSELL())
                {
                    BankListActivity.setBestUSDSELL(bank.getUSDSELL());
                }
                if (BankListActivity.getBestEURBUY() < bank.getEURBUY())
                {
                    BankListActivity.setBestEURBUY(bank.getEURBUY());
                }
                if (BankListActivity.getBestEURSELL() > bank.getEURSELL())
                {
                    BankListActivity.setBestEURSELL(bank.getEURSELL());
                }
                if (BankListActivity.getBestRUBBUY() < bank.getRUBBUY())
                {
                    BankListActivity.setBestRUBBUY(bank.getRUBBUY());
                }
                if (BankListActivity.getBestRUBSELL() > bank.getRUBSELL())
                {
                    BankListActivity.setBestRUBSELL(bank.getRUBSELL());
                }
        }
        for (Bank bank:banks)
        {
            if (bank.getUSDBUY() == BankListActivity.getBestUSDBUY())
            {
                bank.setUSDBUYBest(true);
            }
            if (bank.getUSDSELL() == BankListActivity.getBestUSDSELL())
            {
                bank.setUSDSELLBest(true);
            }
            if (bank.getEURBUY() == BankListActivity.getBestEURBUY())
            {
                bank.setEURBUYBest(true);
            }
            if (bank.getEURSELL() == BankListActivity.getBestEURSELL())
            {
                bank.setEURSELLBest(true);
            }
            if (bank.getRUBBUY() == BankListActivity.getBestRUBBUY())
            {
                bank.setRUBBUYBest(true);
            }
            if (bank.getRUBSELL() == BankListActivity.getBestRUBSELL())
            {
                bank.setRUBSELLBest(true);
            }
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                if (subdivision.getUSDBUY() == BankListActivity.getBestUSDBUY())
                {
                    subdivision.setUSDBUYBest(true);
                }
                if (subdivision.getUSDSELL() == BankListActivity.getBestUSDSELL())
                {
                    subdivision.setUSDSELLBest(true);
                }
                if (subdivision.getEURBUY() == BankListActivity.getBestEURBUY())
                {
                    subdivision.setEURBUYBest(true);
                }
                if (subdivision.getEURSELL() == BankListActivity.getBestEURSELL())
                {
                    subdivision.setEURSELLBest(true);
                }
                if (subdivision.getRUBBUY() == BankListActivity.getBestRUBBUY())
                {
                    subdivision.setRUBBUYBest(true);
                }
                if (subdivision.getRUBSELL() == BankListActivity.getBestRUBSELL())
                {
                    subdivision.setRUBSELLBest(true);
                }
            }
        }
        return banks;
    }

    @Override
    protected List<Bank> doInBackground(Void... params)
    {
        try
        {
            return getBanks();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
