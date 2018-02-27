package com.unwheeze.beans;

public class RequestWeb {

    private AirData airData;
    private int duration;
    private int distance;

    public RequestWeb(AirData airData, int duration, int distance) {
        this.airData = airData;
        this.duration = duration;
        this.distance = distance;
    }

    public RequestWeb() {
    }

    public AirData getAirData() { return airData; }

    public int getDuration() { return duration; }

    public int getDistance() { return distance; }

    public void setAirData(AirData airData) { this.airData = airData; }

    public void setDuration(int duration) { this.duration = duration; }

    public void setDistance(int distance) { this.distance = distance; }
}
