package io.smartinez.exposeller.client.domain;

import java.util.Date;

public interface IModel {
    String getDocId();
    void setDocId(String docId);

    Integer getFriendlyId();
    void setFriendlyId(Integer friendlyId);

    Date getEventDate();
    void setEventDate(Date eventDate);
}
