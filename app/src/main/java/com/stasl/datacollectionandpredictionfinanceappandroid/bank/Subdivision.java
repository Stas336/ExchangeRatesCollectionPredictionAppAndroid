package com.stasl.datacollectionandpredictionfinanceappandroid.bank;

import java.io.Serializable;

public class Subdivision implements Serializable
{
    private String name;
    private String address;
    private float USDBUY;
    private boolean isUSDBUYBest;
    private float USDSELL;
    private boolean isUSDSELLBest;
    private float EURBUY;
    private boolean isEURBUYBest;
    private float EURSELL;
    private boolean isEURSELLBest;
    private float RUBBUY;
    private boolean isRUBBUYBest;
    private float RUBSELL;
    private boolean isRUBSELLBest;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public float getUSDBUY()
    {
        return USDBUY;
    }

    public void setUSDBUY(float USDBUY)
    {
        this.USDBUY = USDBUY;
    }

    public float getUSDSELL()
    {
        return USDSELL;
    }

    public void setUSDSELL(float USDSELL)
    {
        this.USDSELL = USDSELL;
    }

    public float getEURBUY()
    {
        return EURBUY;
    }

    public void setEURBUY(float EURBUY)
    {
        this.EURBUY = EURBUY;
    }

    public float getEURSELL()
    {
        return EURSELL;
    }

    public void setEURSELL(float EURSELL)
    {
        this.EURSELL = EURSELL;
    }

    public float getRUBBUY()
    {
        return RUBBUY;
    }

    public void setRUBBUY(float RUBBUY)
    {
        this.RUBBUY = RUBBUY;
    }

    public float getRUBSELL()
    {
        return RUBSELL;
    }

    public void setRUBSELL(float RUBSELL)
    {
        this.RUBSELL = RUBSELL;
    }

    public boolean isUSDBUYBest() {
        return isUSDBUYBest;
    }

    public void setUSDBUYBest(boolean USDBUYBest) {
        isUSDBUYBest = USDBUYBest;
    }

    public boolean isUSDSELLBest() {
        return isUSDSELLBest;
    }

    public void setUSDSELLBest(boolean USDSELLBest) {
        isUSDSELLBest = USDSELLBest;
    }

    public boolean isEURBUYBest() {
        return isEURBUYBest;
    }

    public void setEURBUYBest(boolean EURBUYBest) {
        isEURBUYBest = EURBUYBest;
    }

    public boolean isEURSELLBest() {
        return isEURSELLBest;
    }

    public void setEURSELLBest(boolean EURSELLBest) {
        isEURSELLBest = EURSELLBest;
    }

    public boolean isRUBBUYBest() {
        return isRUBBUYBest;
    }

    public void setRUBBUYBest(boolean RUBBUYBest) {
        isRUBBUYBest = RUBBUYBest;
    }

    public boolean isRUBSELLBest() {
        return isRUBSELLBest;
    }

    public void setRUBSELLBest(boolean RUBSELLBest) {
        isRUBSELLBest = RUBSELLBest;
    }
}
