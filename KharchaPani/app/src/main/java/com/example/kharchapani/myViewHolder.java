package com.example.kharchapani;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        void setType(String type){
            TextView mType = mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        void setNote(String note){
            TextView mNote = mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        void setDate(String date){
            TextView mDate = mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
        void setAmmount(String ammount){
            String stammount =String.valueOf(ammount);
            TextView mAmmount = mView.findViewById(R.id.ammount_txt_income);
            mAmmount.setText(stammount);
        }
    }
