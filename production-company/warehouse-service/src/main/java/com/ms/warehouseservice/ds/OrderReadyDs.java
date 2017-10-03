package com.ms.warehouseservice.ds;

import java.util.Calendar;

public class OrderReadyDs {
        private Calendar eventDateTime;
        protected String priorityRealization;
        protected String recipientName;
        protected String eMail;

    public Calendar getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Calendar eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getPriorityRealization() {
        return priorityRealization;
    }

    public void setPriorityRealization(String priorityRealization) {
        this.priorityRealization = priorityRealization;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
