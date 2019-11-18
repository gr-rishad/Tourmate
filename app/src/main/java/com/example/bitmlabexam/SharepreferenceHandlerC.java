package com.example.bitmlabexam;

import android.content.Context;
import android.content.SharedPreferences;

public class SharepreferenceHandlerC {

    private SharedPreferences sp;
    private Context ctx;
    private String userId;
    private String budget;
    private String expense;

    public SharepreferenceHandlerC(Context ctx) {
        this.ctx = ctx;
        sp = ctx.getSharedPreferences("User", Context.MODE_PRIVATE);
    }

    public void saveUserId(String userId) {
        this.userId = userId;
        saveToSp();
    }

    private void saveToSp() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userId", userId);
        editor.apply();
    }

    public String getUserId() {

        return sp.getString("userId", "Not Found");
    }


    //
    public void saveBudget(String budget) {
        this.budget = budget;
        saveToBudget();
    }

    private void saveToBudget() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("budget", budget);
        editor.apply();
    }

    public String getBudget() {

        return sp.getString("budget", "Not Found");
    }

    //
    public void saveExpense(String expense) {
        this.expense = expense;
        saveToExpense();
    }
//
    private void saveToExpense() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("expense", expense);
        editor.apply();
    }

    public String getExpense() {

        return sp.getString("expense", "0");
    }



}
