package org.personal.korail_android.item;

public class NursingRoomItem {

    String id;
    String location;
    String phoneNumber;
    String stationName;

    public NursingRoomItem(String id, String location, String phoneNumber, String stationName) {
        this.id = id;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.stationName = stationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
