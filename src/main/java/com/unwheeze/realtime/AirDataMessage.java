package com.unwheeze.realtime;

import com.unwheeze.beans.AirData;

public class AirDataMessage {
    private AirData new_val;
    private AirData old_val;
    private boolean isDeleted;

    public AirDataMessage() {
    }

    public AirDataMessage(AirData new_val, AirData old_val) {
        this.new_val = new_val;
        this.old_val = old_val;
    }

    public AirDataMessage(AirData new_val, AirData old_val, boolean isDeleted) {
        this.new_val = new_val;
        this.old_val = old_val;
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public AirData getNew_val() {
        return new_val;
    }

    public void setNew_val(AirData new_val) {
        this.new_val = new_val;
    }

    public AirData getOld_val() {
        return old_val;
    }

    public void setOld_val(AirData old_val) {
        this.old_val = old_val;
    }
}
