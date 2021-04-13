package io.smartinez.exposeller.client.domain;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.Date;

public class Concert implements Parcelable {
    private String id;
    private String name;
    private Float cost;
    private Uri photoConcert;
    private String artistName;
    private Date eventDate;

    public Concert(String id, String name, Float cost, Uri photoConcert, String artistName, Date eventDate) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.photoConcert = photoConcert;
        this.artistName = artistName;
        this.eventDate = eventDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    public Uri getPhotoConcert() {
        return photoConcert;
    }

    public void setPhotoConcert(Uri photoConcert) {
        this.photoConcert = photoConcert;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    protected Concert(Parcel in) {
        id = in.readString();
        name = in.readString();
        cost = in.readByte() == 0x00 ? null : in.readFloat();
        photoConcert = (Uri) in.readValue(Uri.class.getClassLoader());
        artistName = in.readString();
        eventDate = (Date) in.readValue(Date.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        if (cost == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(cost);
        }
        dest.writeValue(photoConcert);
        dest.writeString(artistName);
        dest.writeValue(eventDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Concert> CREATOR = new Parcelable.Creator<Concert>() {
        @Override
        public Concert createFromParcel(Parcel in) {
            return new Concert(in);
        }

        @Override
        public Concert[] newArray(int size) {
            return new Concert[size];
        }
    };
}