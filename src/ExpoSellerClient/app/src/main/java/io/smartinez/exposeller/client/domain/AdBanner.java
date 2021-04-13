package io.smartinez.exposeller.client.domain;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class AdBanner implements Parcelable {
    private String id;
    private String name;
    private Uri photoAd;

    public AdBanner(String id, String name, Uri photoAd) {
        this.id = id;
        this.name = name;
        this.photoAd = photoAd;
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

    public Uri getPhotoAd() {
        return photoAd;
    }

    public void setPhotoAd(Uri photoAd) {
        this.photoAd = photoAd;
    }

    protected AdBanner(Parcel in) {
        id = in.readString();
        name = in.readString();
        photoAd = (Uri) in.readValue(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeValue(photoAd);
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
