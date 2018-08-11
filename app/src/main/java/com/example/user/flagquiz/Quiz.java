package com.example.user.flagquiz;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

    Cursor cursor;
    private Context context;

    public Quiz(Context context){
        this.context = context;
    }
    private FlagDBHelper flagDBHelper= new FlagDBHelper(context);

    public List<FlagModel> getDataFromDb(){
    cursor = flagDBHelper.queryFlags();

    List<FlagModel> flagList = new ArrayList<>();

    if(cursor!=null && cursor.moveToFirst()){
        do{
           FlagModel flag = new FlagModel(cursor.getString(cursor.getColumnIndex(FlagDBHelper.COLUMN_CODE)), cursor.getString(cursor.getColumnIndex(FlagDBHelper.COLUMN_NAME)));
           flagList.add(flag);
        }while(cursor.moveToNext());
        }
    return flagList;
    }

    //create 20 question quiz

    public List<FlagModel> getQuestions(List<FlagModel> flagModel){

        int flagSize = flagModel.size();
        //generate random numbers


        return null;
    }
}
