package com.stasl.datacollectionandpredictionfinanceappandroid.parser;


import android.os.AsyncTask;

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
            System.out.println("Bank name " + bank.getName());
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
                System.out.println("Bank subdivision name " + subdivision.getName());
                System.out.println("Bank subdivision address " + subdivision.getAddress());
                System.out.println("USD BUY " + subdivision.getUSDBUY());
                System.out.println("USD SELL " + subdivision.getUSDSELL());
                System.out.println("EUR BUY " + subdivision.getEURBUY());
                System.out.println("EUR SELL " + subdivision.getEURSELL());
                System.out.println("RUB BUY " + subdivision.getRUBBUY());
                System.out.println("RUB SELL " + subdivision.getRUBSELL());
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
        float bestUSDBUY = 0, bestUSDSELL = 0, bestEURBUY = 0, bestEURSELL = 0, bestRUBBUY = 0, bestRUBSELL = 0;
        boolean isFirst = true;
        for (Bank bank:banks)
        {

                if (isFirst)
                {
                    bestUSDBUY = bank.getUSDBUY();
                    bestUSDSELL = bank.getUSDSELL();
                    bestEURBUY = bank.getEURBUY();
                    bestEURSELL = bank.getEURSELL();
                    bestRUBBUY = bank.getRUBBUY();
                    bestRUBSELL = bank.getRUBSELL();
                    isFirst = false;
                }
                if (bestUSDBUY < bank.getUSDBUY())
                {
                    bestUSDBUY = bank.getUSDBUY();
                }
                if (bestUSDSELL > bank.getUSDSELL())
                {
                    bestUSDSELL = bank.getUSDSELL();
                }
                if (bestEURBUY < bank.getEURBUY())
                {
                    bestEURBUY = bank.getEURBUY();
                }
                if (bestEURSELL > bank.getEURSELL())
                {
                    bestEURSELL = bank.getEURSELL();
                }
                if (bestRUBBUY < bank.getRUBBUY())
                {
                    bestRUBBUY = bank.getRUBBUY();
                }
                if (bestRUBSELL > bank.getRUBSELL())
                {
                    bestRUBSELL = bank.getRUBSELL();
                }
        }
        BankListActivity.setBestUSDBUY(bestUSDBUY);
        BankListActivity.setBestUSDSELL(bestUSDSELL);
        BankListActivity.setBestEURBUY(bestEURBUY);
        BankListActivity.setBestEURSELL(bestEURSELL);
        BankListActivity.setBestRUBBUY(bestRUBBUY);
        BankListActivity.setBestRUBSELL(bestRUBSELL);
        for (Bank bank:banks)
        {
            if (bank.getUSDBUY() == bestUSDBUY)
            {
                bank.setUSDBUYBest(true);
            }
            if (bank.getUSDSELL() == bestUSDSELL)
            {
                bank.setUSDSELLBest(true);
            }
            if (bank.getEURBUY() == bestEURBUY)
            {
                bank.setEURBUYBest(true);
            }
            if (bank.getEURSELL() == bestEURSELL)
            {
                bank.setEURSELLBest(true);
            }
            if (bank.getRUBBUY() == bestRUBBUY)
            {
                bank.setRUBBUYBest(true);
            }
            if (bank.getRUBSELL() == bestRUBSELL)
            {
                bank.setRUBSELLBest(true);
            }
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                if (subdivision.getUSDBUY() == bestUSDBUY)
                {
                    subdivision.setUSDBUYBest(true);
                }
                if (subdivision.getUSDSELL() == bestUSDSELL)
                {
                    subdivision.setUSDSELLBest(true);
                }
                if (subdivision.getEURBUY() == bestEURBUY)
                {
                    subdivision.setEURBUYBest(true);
                }
                if (subdivision.getEURSELL() == bestEURSELL)
                {
                    subdivision.setEURSELLBest(true);
                }
                if (subdivision.getRUBBUY() == bestRUBBUY)
                {
                    subdivision.setRUBBUYBest(true);
                }
                if (subdivision.getRUBSELL() == bestRUBSELL)
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
        try {
            return getBanks();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
