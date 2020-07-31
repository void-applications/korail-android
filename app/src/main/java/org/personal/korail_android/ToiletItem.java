package org.personal.korail_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ToiletItem {

    String location;
    String manWomen;
    String insideOrOut;
    String id;

    public ToiletItem(String location, String manWomen, String insideOrOut, String id) {
        this.location = location;
        this.manWomen = manWomen;
        this.insideOrOut = insideOrOut;
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getManWomen() {
        return manWomen;
    }

    public void setManWomen(String manWomen) {
        this.manWomen = manWomen;
    }

    public String getInsideOrOut() {
        return insideOrOut;
    }

    public void setInsideOrOut(String insideOrOut) {
        this.insideOrOut = insideOrOut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}