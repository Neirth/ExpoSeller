/*
 * MIT License
 *
 * Copyright (c) 2021 Sergio Martinez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.smartinez.exposeller.client.domain;

import java.util.Date;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.firestore.Exclude;

public class Concert implements IModel, Parcelable {
    private String docId;
    private String name;
    private Float cost;
    private String photoConcert;
    private String artistName;
    private Date eventDate;
    private Integer friendlyId;
    private String organizationName;

    public Concert() {
    }

    public Concert(String docId, String name, Float cost, String photoConcert, String artistName, Date eventDate, Integer friendlyId, String organizationName) {
        this.docId = docId;
        this.name = name;
        this.cost = cost;
        this.photoConcert = photoConcert;
        this.artistName = artistName;
        this.eventDate = eventDate;
        this.friendlyId = friendlyId;
        this.organizationName = organizationName;
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

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String getPhotoConcert() {
        return photoConcert;
    }

    public void setPhotoConcert(String photoConcert) {
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

    @Override
    public Integer getFriendlyId() {
        return friendlyId;
    }

    @Override
    public void setFriendlyId(Integer friendlyId) {
        this.friendlyId = friendlyId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    protected Concert(Parcel in) {
        docId = in.readString();
        name = in.readString();
        cost = in.readByte() == 0x00 ? null : in.readFloat();
        photoConcert = in.readString();
        artistName = in.readString();
        long tmpEventDate = in.readLong();
        eventDate = tmpEventDate != -1 ? new Date(tmpEventDate) : null;
        friendlyId = in.readByte() == 0x00 ? null : in.readInt();
        organizationName = in.readString();
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
        dest.writeString(photoConcert);
        dest.writeString(artistName);
        dest.writeLong(eventDate != null ? eventDate.getTime() : -1L);
        if (friendlyId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(friendlyId);
        }
        dest.writeString(organizationName);
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