package io.smartinez.exposeller.client.domain;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.firestore.Exclude;

public class Ticket implements Parcelable, IModel {
    private String docId;
    private String concertId;
    private Integer friendlyId;
    private Boolean isUsed;

    public Ticket(String docId, String concertId, Integer friendlyId) {
        this.docId = docId;
        this.concertId = concertId;
        this.friendlyId = friendlyId;
        this.isUsed = false;
    }

    @Exclude
    public String getDocId() {
        return docId;
    }

    @Exclude
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    public Integer getFriendlyId() {
        return friendlyId;
    }

    public void setFriendlyId(Integer friendlyId) {
        this.friendlyId = friendlyId;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    protected Ticket(Parcel in) {
        docId = in.readString();
        concertId = in.readString();
        friendlyId = in.readByte() == 0x00 ? null : in.readInt();
        byte isUsedVal = in.readByte();
        isUsed = isUsedVal == 0x02 ? null : isUsedVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(docId);
        dest.writeString(concertId);
        if (friendlyId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(friendlyId);
        }
        if (isUsed == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isUsed ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ticket> CREATOR = new Parcelable.Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };
}