package io.smartinez.exposeller.client.domain;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class AdBanner implements IModel, Parcelable {
    private String docId;
    private String name;
    private Uri photoAd;
    private Integer friendlyId;
    private Date eventDate;

    public AdBanner(String docId, String name, Uri photoAd, Integer friendlyId, Date eventDate) {
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

    public Uri getPhotoAd() {
        return photoAd;
    }

    public void setPhotoAd(Uri photoAd) {
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

    protected AdBanner(Parcel in) {
        docId = in.readString();
        name = in.readString();
        photoAd = (Uri) in.readValue(Uri.class.getClassLoader());
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
        dest.writeValue(photoAd);
        if (friendlyId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(friendlyId);
        }
        dest.writeLong(eventDate != null ? eventDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AdBanner> CREATOR = new Parcelable.Creator<AdBanner>() {
        @Override
        public AdBanner createFromParcel(Parcel in) {
            return new AdBanner(in);
        }

        @Override
        public AdBanner[] newArray(int size) {
            return new AdBanner[size];
        }
    };
}