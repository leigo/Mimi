package com.leigo.android.model.domain;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Administrator on 2014/8/22.
 */
public class Country implements Comparable<Country>, Parcelable {

    public static final Country DEFAULT = new Country("+86", "中国", null);
    private String code;
    private String identifier;
    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(identifier);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Country[size];
        }
    };

    private Country(Parcel source) {
        code = source.readString();
        identifier = source.readString();
        name = source.readString();
    }

    @Override
    public int compareTo(Country country) {
        return getIdentifier().compareTo(country.getIdentifier());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country(String code, String name, String identifier) {
        this.code = code;
        this.identifier = identifier;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


}
