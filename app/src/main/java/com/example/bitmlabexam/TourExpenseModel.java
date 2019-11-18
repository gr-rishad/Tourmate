package com.example.bitmlabexam;

public class TourExpenseModel {

    public String tourBudgetName;
    public double tourBudgetQuantity;
    public String tourExpenseName;
    public double tourExpenseQuantity;

    public TourExpenseModel() {
    }

    public TourExpenseModel(String tourBudgetName, double tourBudgetQuantity) {
        this.tourBudgetName = tourBudgetName;
        this.tourBudgetQuantity = tourBudgetQuantity;
    }

    public TourExpenseModel( double tourExpenseQuantity,String tourExpenseName) {
        this.tourExpenseQuantity = tourExpenseQuantity;
        this.tourExpenseName = tourExpenseName;

    }



}
