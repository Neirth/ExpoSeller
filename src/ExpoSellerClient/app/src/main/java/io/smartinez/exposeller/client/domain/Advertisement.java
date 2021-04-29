package io.smartinez.exposeller.client.domain;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class Advertisement implements IModel, Parcelable {
    private String docId;
    private String name;
    private String photoAd;
    private Integer friendlyId;
    private Date eventDate;

    public Advertisement() {
    }

    public Advertisement(String docId, String name, String photoAd, Integer friendlyId, Date eventDate) {
        this.docId = docId;
        this.name = name;
        this.photoAd = photoAd;
        this.friendlyId = friendlyId;
        this.eventDate = eventDate;
    }

    @Exclude
    @Override
    public String getDocId() {
        return docId;
    }

    @Exclude
    @Override
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoAd() {
        return photoAd;
    }

    public void setPhotoAd(String photoAd) {
        this.photoAd = photoAd;
    }

    @Override
    public Integer getFriendlyId() {
        return friendlyId;
    }

    @Override
    public void setFriendlyId(Integer friendlyId) {
        this.friendlyId = friendlyId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    protected Advertisement(Parcel in) {
        docId = in.readString();
        name = in.readString();
        photoAd = in.readString();
        friendlyId = in.readByte() == 0x00 ? null : in.readInt();
        long tmpEventDate = in.readLong();
        eventDate = tmpEventDate != -1 ? new Date(tmpEventDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(docId);
        dest.writeString(name);
        dest.writeString(photoAd);
        if (friendlyId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(friendlyId);
        }
        dest.writeLong(eventDate != null ? eventDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Advertisement> CREATOR = new Parcelable.Creator<Advertisement>() {
        @Override
        public Advertisement createFromParcel(Parcel in) {
            return new Advertisement(in);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }
    };
}