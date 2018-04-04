package com.unwheeze.beans;

public class RadiusRequest {

    private AirData airData;
    private int duration;
    private int distance;

    public RadiusRequest(AirData airData, int duration, int distance) {
        this.airData = airData;
        this.duration = duration;
        this.distance = distance;
    }

    public RadiusRequest() {
    }

    public AirData getAirData() { return airData; }

    public int getDuration() { return duration; }

    public int getDistance() { return distance; }

    public void setAirData(AirData airData) { this.airData = airData; }

    public void setDuration(int duration) { this.duration = duration; }

    public void setDistance(int distance) { this.distance = distance; }
}
