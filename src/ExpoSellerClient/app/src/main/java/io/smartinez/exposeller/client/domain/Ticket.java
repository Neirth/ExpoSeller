package io.smartinez.exposeller.client.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Ticket implements IModel, Parcelable {
    private String docId;
    private String concertId;
    private Integer friendlyId;
    private Boolean isUsed;
    private Date generatedDate;

    public Ticket(String docId, String concertId, Integer friendlyId, Boolean isUsed, Date generatedDate) {
        this.docId = docId;
        this.concertId = concertId;
        this.friendlyId = friendlyId;
        this.isUsed = isUsed;
        this.generatedDate = generatedDate;
    }

    @Override
    public String getDocId() {
        return docId;
    }

    @Override
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    @Override
    public Integer getFriendlyId() {
        return friendlyId;
    }

    @Override
    public void setFriendlyId(Integer friendlyId) {
        this.friendlyId = friendlyId;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean used) {
        isUsed = used;
    }

    public Date getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(Date generatedDate) {
        this.generatedDate = generatedDate;
    }

    protected Ticket(Parcel in) {
        docId = in.readString();
        concertId = in.readString();
        friendlyId = in.readByte() == 0x00 ? null : in.readInt();
        byte isUsedVal = in.readByte();
        isUsed = isUsedVal == 0x02 ? null : isUsedVal != 0x00;
        long tmpGeneratedDate = in.readLong();
        generatedDate = tmpGeneratedDate != -1 ? new Date(tmpGeneratedDate) : null;
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
        dest.writeLong(generatedDate != null ? generatedDate.getTime() : -1L);
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