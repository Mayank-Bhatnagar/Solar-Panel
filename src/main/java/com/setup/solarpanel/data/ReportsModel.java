package com.setup.solarpanel.data;

/**
 * Created by Mayank on 9/26/2015.
 */

public class ReportsModel{

    private String date = "";
    private String startTime = "";
    private String endTime = "";
    private String longitude = "";
    private String latitude = "";
    private String directionCaptured = "";
    private String angleCaptured = "";
    private String optimalDirection = "";
    private String optimalAngle = "";
    private String averageLux = "";
    private String luxValue = "";
    private String capturedAt = "";

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDirectionCaptured() {
        return directionCaptured;
    }

    public void setDirectionCaptured(String directionCaptured) {
        this.directionCaptured = directionCaptured;
    }

    public String getAngleCaptured() {
        return angleCaptured;
    }

    public void setAngleCaptured(String angleCaptured) {
        this.angleCaptured = angleCaptured;
    }

    public String getOptimalDirection() {
        return optimalDirection;
    }

    public void setOptimalDirection(String optimalDirection) {
        this.optimalDirection = optimalDirection;
    }

    public String getOptimalAngle() {
        return optimalAngle;
    }

    public void setOptimalAngle(String optimalAngle) {
        this.optimalAngle = optimalAngle;
    }

    public String getAverageLux() {
        return averageLux;
    }

    public void setAverageLux(String averageLux) {
        this.averageLux = averageLux;
    }

    public String getLuxValue() {
        return luxValue;
    }

    public void setLuxValue(String luxValue) {
        this.luxValue = luxValue;
    }

    public String getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(String capturedAt) {
        this.capturedAt = capturedAt;
    }
}

