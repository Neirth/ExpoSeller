package io.smartinez.exposeller.client.domain;

import java.util.Date;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.firestore.Exclude;

public class Concert implements Parcelable, IModel {
    private String docId;
    private String name;
    private Float cost;
    private Uri photoConcert;
    private String artistName;
    private Date eventDate;
    private Integer friendlyId;

    public Concert(String docId, String name, Float cost, Uri photoConcert, String artistName, Date eventDate, Integer friendlyId) {
        this.docId = docId;
        this.name = name;
        this.cost = cost;
        this.photoConcert = photoConcert;
        this.artistName = artistName;
        this.eventDate = eventDate;
        this.friendlyId = friendlyId;
    }

    @Exclude
    public String getDocId() {
        return docId;
    }

    @Exclude
    public void setDocId(String docId) {
        this.docId = docId;
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

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getFriendlyId() {
        return friendlyId;
    }

    public void setFriendlyId(Integer friendlyId) {
        this.friendlyId = friendlyId;
    }

    protected Concert(Parcel in) {
        docId = in.readString();
        name = in.readString();
        cost = in.readByte() == 0x00 ? null : in.readFloat();
        photoConcert = (Uri) in.readValue(Uri.class.getClassLoader());
        artistName = in.readString();
        long tmpEventDate = in.readLong();
        eventDate = tmpEventDate != -1 ? new Date(tmpEventDate) : null;
        friendlyId = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(docId);
        dest.writeString(name);
        if (cost == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(cost);
        }
        dest.writeValue(photoConcert);
        dest.writeString(artistName);
        dest.writeLong(eventDate != null ? eventDate.getTime() : -1L);
        if (friendlyId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(friendlyId);
        }
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