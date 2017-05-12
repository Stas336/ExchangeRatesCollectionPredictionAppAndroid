package com.stasl.datacollectionandpredictionfinanceappandroid.parser;


import android.os.AsyncTask;

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

    public static void main(String[] args)
    {
        try
        {
            getBestCourses(getBanks());
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
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
                if (bank.getName().equals("Цептер Банк"))
                {
                    i = elements.size() - 1;
                    break;
                }
                i++;
            }
            banks.add(bank);
        }
        return banks;
    }
    public static void getBestCourses(List<Bank> banks)
    {
        float bestUSDBUY = 0, bestUSDSELL = 0, bestEURBUY = 0, bestEURSELL = 0, bestRUBBUY = 0, bestRUBSELL = 0;
        List<Bank> bestBanksUSDBUY = null, bestBanksUSDSELL = null, bestBanksEURBUY = null, bestBanksEURSELL = null, bestBanksRUBBUY = null, bestBanksRUBSELL = null;
        boolean isFirst = true;
        for (Bank bank:banks)
        {
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                if (isFirst)
                {
                    bestUSDBUY = subdivision.getUSDBUY();
                    bestUSDSELL = subdivision.getUSDSELL();
                    bestEURBUY = subdivision.getEURBUY();
                    bestEURSELL = subdivision.getEURSELL();
                    bestRUBBUY = subdivision.getRUBBUY();
                    bestRUBSELL = subdivision.getRUBSELL();
                    isFirst = false;
                }
                if (bestUSDBUY > subdivision.getUSDBUY())
                {
                    bestUSDBUY = subdivision.getUSDBUY();
                }
                if (bestUSDSELL < subdivision.getUSDSELL())
                {
                    bestUSDSELL = subdivision.getUSDSELL();
                }
                if (bestEURBUY > subdivision.getEURBUY())
                {
                    bestEURBUY = subdivision.getEURBUY();
                }
                if (bestEURSELL < subdivision.getEURSELL())
                {
                    bestEURSELL = subdivision.getEURSELL();
                }
                if (bestRUBBUY > subdivision.getRUBBUY())
                {
                    bestRUBBUY = subdivision.getRUBBUY();
                }
                if (bestRUBSELL < subdivision.getRUBSELL())
                {
                    bestRUBSELL = subdivision.getRUBSELL();
                }
            }
        }
        isFirst = true;
        for (Bank bank:banks)
        {
            Bank bankTempUSDBUY = new Bank(bank.getName());
            Bank bankTempUSDSELL = new Bank(bank.getName());
            Bank bankTempEURBUY = new Bank(bank.getName());
            Bank bankTempEURSELL = new Bank(bank.getName());
            Bank bankTempRUBBUY = new Bank(bank.getName());
            Bank bankTempRUBSELL = new Bank(bank.getName());
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                if (isFirst)
                {
                    bestBanksUSDBUY = new ArrayList<>();
                    bestBanksUSDSELL = new ArrayList<>();
                    bestBanksEURBUY = new ArrayList<>();
                    bestBanksEURSELL = new ArrayList<>();
                    bestBanksRUBBUY = new ArrayList<>();
                    bestBanksRUBSELL = new ArrayList<>();
                    isFirst = false;
                }
                if (subdivision.getUSDBUY() == bestUSDBUY)
                {
                    bankTempUSDBUY.addSubdivision(subdivision);
                }
                if (subdivision.getUSDSELL() == bestUSDSELL)
                {
                    bankTempUSDSELL.addSubdivision(subdivision);
                }
                if (subdivision.getEURBUY() == bestEURBUY)
                {
                    bankTempEURBUY.addSubdivision(subdivision);
                }
                if (subdivision.getEURSELL() == bestEURSELL)
                {
                    bankTempEURSELL.addSubdivision(subdivision);
                }
                if (subdivision.getRUBBUY() == bestRUBBUY)
                {
                    bankTempRUBBUY.addSubdivision(subdivision);
                }
                if (subdivision.getRUBSELL() == bestRUBSELL)
                {
                    bankTempRUBSELL.addSubdivision(subdivision);
                }
            }
            if (bankTempUSDBUY.getSubdivisionsUnmodifiable().size() > 0)
            {
                bestBanksUSDBUY.add(bankTempUSDBUY);
            }
            if (bankTempUSDSELL.getSubdivisionsUnmodifiable().size() > 0)
            {
                bestBanksUSDSELL.add(bankTempUSDSELL);
            }
            if (bankTempEURBUY.getSubdivisionsUnmodifiable().size() > 0)
            {
                bestBanksEURBUY.add(bankTempEURBUY);
            }
            if (bankTempEURSELL.getSubdivisionsUnmodifiable().size() > 0)
            {
                bestBanksEURSELL.add(bankTempEURSELL);
            }
            if (bankTempRUBBUY.getSubdivisionsUnmodifiable().size() > 0)
            {
                bestBanksRUBBUY.add(bankTempRUBBUY);
            }
            if (bankTempRUBSELL.getSubdivisionsUnmodifiable().size() > 0)
            {
                bestBanksRUBSELL.add(bankTempRUBSELL);
            }
        }
        System.out.printf("Best USD BUY - %f\n", bestUSDBUY);
        for (Bank bank:bestBanksUSDBUY)
        {
            System.out.printf("%s\n\n", bank.getName());
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                System.out.printf("Name: %s, Address: %s\n", subdivision.getName(), subdivision.getAddress());
            }
        }
        System.out.printf("Best USD SELL - %f\n", bestUSDSELL);
        for (Bank bank:bestBanksUSDSELL)
        {
            System.out.printf("%s\n\n", bank.getName());
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                System.out.printf("Name: %s, Address: %s\n", subdivision.getName(), subdivision.getAddress());
            }
        }
        System.out.printf("Best EUR BUY - %f\n", bestEURBUY);
        for (Bank bank:bestBanksEURBUY)
        {
            System.out.printf("%s\n\n", bank.getName());
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                System.out.printf("Name: %s, Address: %s\n", subdivision.getName(), subdivision.getAddress());
            }
        }
        System.out.printf("Best EUR SELL - %f\n", bestEURSELL);
        for (Bank bank:bestBanksEURSELL)
        {
            System.out.printf("%s\n\n", bank.getName());
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                System.out.printf("Name: %s, Address: %s\n", subdivision.getName(), subdivision.getAddress());
            }
        }
        System.out.printf("Best RUB BUY - %f\n", bestRUBBUY);
        for (Bank bank:bestBanksRUBBUY)
        {
            System.out.printf("%s\n\n", bank.getName());
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                System.out.printf("Name: %s, Address: %s\n", subdivision.getName(), subdivision.getAddress());
            }
        }
        System.out.printf("Best RUB SELL - %f\n", bestRUBSELL);
        for (Bank bank:bestBanksRUBSELL)
        {
            System.out.printf("%s\n\n", bank.getName());
            for (Subdivision subdivision:bank.getSubdivisionsUnmodifiable())
            {
                System.out.printf("Name: %s, Address: %s\n", subdivision.getName(), subdivision.getAddress());
            }
        }
    }

    @Override
    protected List<Bank> doInBackground(Void... params) {
        try {
            return getBanks();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
