package com.example.kharchapaani.viewmodels;

import static java.security.AccessController.getContext;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kharchapaani.Utils.Constants;
import com.example.kharchapaani.Utils.Helper;
import com.example.kharchapaani.models.Transaction;

import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();
    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();
    Realm realm;
    Calendar calendar;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setupDatabse();
    }

    public void getTransaction(Calendar calendar){
        this.calendar = calendar;

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        double income =0;
        double expense =0;
        double total = 0;
        RealmResults<Transaction> newTransactions = null;

        if(Constants.SELECTED_TAB == Constants.DAILY)
        {
            newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + 24 * 60 * 60 * 1000))
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + 24 * 60 * 60 * 1000))
                    .equalTo("type", Constants.INCOME)
                    .sum("amount").doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + 24 * 60 * 60 * 1000))
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount").doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", calendar.getTime())
                    .lessThan("date", new Date(calendar.getTime().getTime() + 24 * 60 * 60 * 1000))
                    .sum("amount").doubleValue();

        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH, 0);

            Date startTime = calendar.getTime();

            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

           newTransactions = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .findAll();

            income = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.INCOME)
                    .sum("amount").doubleValue();

            expense = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .equalTo("type", Constants.EXPENSE)
                    .sum("amount").doubleValue();

            total = realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date", startTime)
                    .lessThan("date", endTime)
                    .sum("amount").doubleValue();
        }
//        else if (Constants.SELECTED_TAB == Constants.CALENDAR) {
//
//            Function onCalender(v: View) {
//                val timeSetListener = TimePickerDialog.OnTimeSetListener { _,_, _ ->
//                    // Some Implementation
//                }
//                TimePickerDialog(
//                        v.context,          // <-- Context from view on which was clicked
//                        timeSetListener,
//                        0,
//                        0,
//                        true
//                ).show()
//            }
//        }

        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);
        transactions.setValue(newTransactions);
    }

    public  void addTransaction(Transaction transaction){
        realm.beginTransaction();

        realm.copyToRealmOrUpdate(transaction);

        realm.commitTransaction();

    }
    public void deleteTransaction(Transaction transaction){
        realm.beginTransaction();
        transaction.deleteFromRealm();
        realm.commitTransaction();
        getTransaction(calendar);
    }
    void setupDatabse(){
        realm = Realm.getDefaultInstance();
    }
}
