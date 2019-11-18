package com.example.bitmlabexam;

public class AddTour {

    public String tourName;
    public String tourDescription;
    public String startDate;
    public String endDate;
    public String userId;


    public AddTour() {
    }

    public AddTour(String tourName, String tourDescription, String startDate, String endDate) {
        this.tourName = tourName;
        this.tourDescription = tourDescription;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourDescription() {
        return tourDescription;
    }

    public void setTourDescription(String tourDescription) {
        this.tourDescription = tourDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
