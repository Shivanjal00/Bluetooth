package com.example.kharchapaani.Utils;

import com.example.kharchapaani.R;
import com.example.kharchapaani.models.Category;

import java.util.ArrayList;

public class Constants {
    public static String INCOME ="INCOME";
    public static String EXPENSE = "EXPENSE";
    public static ArrayList<Category> categories;

    public static int SELECTED_TAB = 0;
    public static int DAILY = 0;
    public static int MONTHLY = 1;
    public static int CALENDAR = 2;
    public static int SUMMARY = 3;
    public static int NOTES = 4;

    public static void setCategories(){
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.stats));
        categories.add(new Category("Business",R.drawable.bussiness));
        categories.add(new Category("Investment",R.drawable.investment));
        categories.add(new Category("Loan",R.drawable.loan));
        categories.add(new Category("Rent",R.drawable.rent));
        categories.add(new Category("Other",R.drawable.other));
    }

    public static Category getCategoryDetails(String categoryName){
        for (Category cat:
        categories) {
            if(cat.getCategoryName().equals(categoryName)){
                return cat;
            }
        }
        return null;
    }
}
