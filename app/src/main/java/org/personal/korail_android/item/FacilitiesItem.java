package org.personal.korail_android.item;

public class FacilitiesItem {

    String id;
    String lineName;
    String stationName;
    String floor;
    String location;
    String equipment;

    public FacilitiesItem(String id, String lineName, String stationName, String floor, String location, String equipment) {
        this.id = id;
        this.lineName = lineName;
        this.stationName = stationName;
        this.floor = floor;
        this.location = location;
        this.equipment = equipment;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}