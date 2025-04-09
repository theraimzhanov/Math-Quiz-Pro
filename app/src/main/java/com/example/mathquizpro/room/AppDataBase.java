package com.example.mathquizpro.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Attempt.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase dataBase;
    public abstract AttemptDao dao();

    public static  synchronized AppDataBase getInstance(Context context){
        if (dataBase== null){
            dataBase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,"quiz_database").allowMainThreadQueries().build();
        }
        return dataBase;
    }
}
