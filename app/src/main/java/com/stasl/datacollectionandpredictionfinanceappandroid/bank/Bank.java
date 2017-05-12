package com.stasl.datacollectionandpredictionfinanceappandroid.bank;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bank implements Parcelable
{
    private String name;
    private List<Subdivision> subdivisions;
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

    public Bank(String name)
    {
        this.name = name;
        subdivisions = new ArrayList<>();
    }
    public void addSubdivision(Subdivision subdivision)
    {
        subdivisions.add(subdivision);
    }
    public void deleteSubdivision(int index)
    {
        if (index > subdivisions.size() - 1)
        {
            throw new IndexOutOfBoundsException();
        }
        subdivisions.remove(index);
    }
    public Subdivision getSubdivision(int index)
    {
        if (index > subdivisions.size() - 1)
        {
            throw new IndexOutOfBoundsException();
        }
        return subdivisions.get(index);
    }
    public List<Subdivision> getSubdivisionsUnmodifiable()
    {
        return Collections.unmodifiableList(subdivisions);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeList(subdivisions);
        dest.writeString(name);
    }
    public static final Parcelable.Creator<Bank> CREATOR = new Parcelable.Creator<Bank>() {
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Bank(Parcel in)
    {
        subdivisions = new ArrayList<>();
        in.readList(subdivisions, Subdivision.class.getClassLoader());
        name = in.readString();
    }
}
