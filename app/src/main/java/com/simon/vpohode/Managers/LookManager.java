package com.simon.vpohode.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.simon.vpohode.Item;
import com.simon.vpohode.Rules;
import com.simon.vpohode.StackLayoutAdapter;
import com.simon.vpohode.database.DatabaseHelper;

import java.util.ArrayList;

public class LookManager {

    public static double getInterval(double temp){
        double result = 0;
        if(temp >= 20){
            result = (Rules.MAX_TEMPER - temp)/(Rules.COEFFICIENT);
        }else if(temp >= 9){
            result = (Rules.MAX_TEMPER - temp)/(Rules.COEFFICIENT*2);
        }else{
            result = (Rules.MAX_TEMPER - temp)/(Rules.COEFFICIENT*3);
        }
        return result;
    }

    public static double getTopIndex(Cursor input, Double currentTemperature){
        double min = Integer.MAX_VALUE;
        double bestTopIndex = 0;
        int layers = Rules.getLayers(currentTemperature);

        if (input.moveToFirst()){
            do {
                double x = Math.abs((Rules.MAX_TEMPER - currentTemperature)/(Rules.COEFFICIENT*layers) - input.getDouble(input.getColumnIndex("termindex")));
                if (min > x) {
                    min = x;
                    bestTopIndex = input.getDouble(input.getColumnIndex("termindex"));
                }
            }
            while (input.moveToNext());
        }
        return bestTopIndex;
    }

    public static double getBotIndex(Cursor input, Double currentTemperature){
        double min = Integer.MAX_VALUE;
        double bestBottomIndex = 0;
        if (input.moveToFirst()){
            do {
                double x = Math.abs((Rules.MAX_TEMPER - currentTemperature)/3 - input.getDouble(input.getColumnIndex("termindex")));
                if (min > x) {
                    min = x;
                    bestBottomIndex = input.getDouble(input.getColumnIndex("termindex"));
                }
            }
            while (input.moveToNext());
        }
        return bestBottomIndex;
    }

    public static ArrayList<Item[]> getLooks(double temp, Context context){

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        ArrayList<Item[]> result = new ArrayList<>();
        Cursor botItems,topItems,topItems2,topItems3;

        int layers = Rules.getLayers(temp);
        double min = getInterval(temp) - Rules.ACCURACY;
        double max = getInterval(temp) + Rules.ACCURACY;
        double top;
        double bot;

        botItems = DatabaseHelper.getCursoreByIsTop(db,0);
        topItems = DatabaseHelper.getCursoreByIsTop(db,1,1);

        switch (layers){
            case 1:
                if(botItems.moveToFirst()){
                    do{
                        if(topItems.moveToFirst()){
                         do{
                             top = topItems.getDouble(topItems.getColumnIndex("termindex"));
                             bot = botItems.getDouble(botItems.getColumnIndex("termindex"));
                             if(top > min &&
                                     top < max &&
                                     bot > min &&
                                     bot < max) {

                                 Item[] look = new Item[layers + 1];
                                 look[0] = new Item(botItems.getInt(botItems.getColumnIndex("_id")),
                                         botItems.getString(botItems.getColumnIndex("name")),
                                         botItems.getString(botItems.getColumnIndex("style")),
                                         botItems.getInt(botItems.getColumnIndex("istop")),
                                         botItems.getDouble(botItems.getColumnIndex("termindex")),
                                         botItems.getInt(botItems.getColumnIndex("layer")),
                                         botItems.getInt(botItems.getColumnIndex("color")),
                                         botItems.getString(botItems.getColumnIndex("foto")));
                                 look[1] = new Item(topItems.getInt(botItems.getColumnIndex("_id")),
                                         topItems.getString(botItems.getColumnIndex("name")),
                                         topItems.getString(botItems.getColumnIndex("style")),
                                         topItems.getInt(botItems.getColumnIndex("istop")),
                                         topItems.getDouble(botItems.getColumnIndex("termindex")),
                                         topItems.getInt(botItems.getColumnIndex("layer")),
                                         topItems.getInt(botItems.getColumnIndex("color")),
                                         topItems.getString(botItems.getColumnIndex("foto")));

                                 result.add(look);
                             }

                         }while (topItems.moveToNext());
                        }
                    }while(botItems.moveToNext());
                }
                break;
            case 2:
                topItems2 = DatabaseHelper.getCursoreByIsTop(db,1,2);
                if(botItems.moveToFirst()){
                    do{
                        if(topItems.moveToFirst()){
                            do{
                                if(topItems2.moveToFirst()){
                                 do{
                                     top = (topItems.getDouble(4) +
                                             topItems2.getDouble(4))/2;
                                     bot = botItems.getDouble(4);
                                     if(bot > min & bot < max & top > min & top < max)
                                     {
                                         Item[] look = new Item[layers + 1];
                                         look[0] = new Item(botItems.getInt(botItems.getColumnIndex("_id")),
                                                 botItems.getString(botItems.getColumnIndex("name")),
                                                 botItems.getString(botItems.getColumnIndex("style")),
                                                 botItems.getInt(botItems.getColumnIndex("istop")),
                                                 botItems.getDouble(botItems.getColumnIndex("termindex")),
                                                 botItems.getInt(botItems.getColumnIndex("layer")),
                                                 botItems.getInt(botItems.getColumnIndex("color")),
                                                 botItems.getString(botItems.getColumnIndex("foto")));
                                         look[1] = new Item(topItems.getInt(topItems.getColumnIndex("_id")),
                                                 topItems.getString(topItems.getColumnIndex("name")),
                                                 topItems.getString(topItems.getColumnIndex("style")),
                                                 topItems.getInt(topItems.getColumnIndex("istop")),
                                                 topItems.getDouble(topItems.getColumnIndex("termindex")),
                                                 topItems.getInt(topItems.getColumnIndex("layer")),
                                                 topItems.getInt(topItems.getColumnIndex("color")),
                                                 topItems.getString(topItems.getColumnIndex("foto")));
                                         look[2] = new Item(topItems2.getInt(topItems2.getColumnIndex("_id")),
                                                 topItems2.getString(topItems2.getColumnIndex("name")),
                                                 topItems2.getString(topItems2.getColumnIndex("style")),
                                                 topItems2.getInt(topItems2.getColumnIndex("istop")),
                                                 topItems2.getDouble(topItems2.getColumnIndex("termindex")),
                                                 topItems2.getInt(topItems2.getColumnIndex("layer")),
                                                 topItems2.getInt(topItems2.getColumnIndex("color")),
                                                 topItems2.getString(topItems2.getColumnIndex("foto")));
                                         result.add(look);
                                     }
                                 }while(topItems2.moveToNext());
                                }
                            }while (topItems.moveToNext());
                        }
                    }while(botItems.moveToNext());
                }
                topItems2.close();
                break;
            case 3:
                topItems2 = DatabaseHelper.getCursoreByIsTop(db,1,2);
                topItems3 = DatabaseHelper.getCursoreByIsTop(db,1,3);
                if(botItems.moveToFirst()){
                    do{
                        if(topItems.moveToFirst()){
                            do{
                                if(topItems2.moveToFirst()){
                                    do{
                                        if(topItems3.moveToFirst()){
                                            do{
                                                bot = botItems.getDouble(botItems.getColumnIndex("termindex"));
                                                top = (topItems.getDouble(topItems.getColumnIndex("termindex")) +
                                                        topItems2.getDouble(topItems2.getColumnIndex("termindex"))+
                                                        topItems3.getDouble(topItems3.getColumnIndex("termindex")))/3;

                                        if(bot > min &&
                                                bot < max &&
                                                top > min &&
                                                top < max){
                                            //check if styles match
                                            if(StyleManager.isLookMatchStyle(new String[]{botItems.getString(botItems.getColumnIndex("style")),
                                                    topItems.getString(topItems.getColumnIndex("style")),
                                                    topItems2.getString(topItems.getColumnIndex("style")),
                                                    topItems3.getString(topItems.getColumnIndex("style"))},prefs)){
                                                //check if color match
                                                if(prefs.getBoolean("sync", false)) {
                                                    if(ColorManager.isLookMatch(new Integer[]{
                                                            botItems.getInt(botItems.getColumnIndex("color")),
                                                            topItems.getInt(topItems.getColumnIndex("color")),
                                                            topItems2.getInt(topItems.getColumnIndex("color")),
                                                            topItems3.getInt(topItems.getColumnIndex("color"))})){
                                                        Item[] look = new Item[layers + 1];

                                                        look[0] = new Item(topItems.getInt(topItems.getColumnIndex("_id")),
                                                                topItems.getString(topItems.getColumnIndex("name")),
                                                                topItems.getString(topItems.getColumnIndex("style")),
                                                                topItems.getInt(topItems.getColumnIndex("istop")),
                                                                topItems.getDouble(topItems.getColumnIndex("termindex")),
                                                                topItems.getInt(topItems.getColumnIndex("layer")),
                                                                topItems.getInt(topItems.getColumnIndex("color")),
                                                                topItems.getString(topItems.getColumnIndex("foto")));
                                                        look[1] = new Item(topItems2.getInt(topItems2.getColumnIndex("_id")),
                                                                topItems2.getString(topItems2.getColumnIndex("name")),
                                                                topItems2.getString(topItems2.getColumnIndex("style")),
                                                                topItems2.getInt(topItems2.getColumnIndex("istop")),
                                                                topItems2.getDouble(topItems2.getColumnIndex("termindex")),
                                                                topItems2.getInt(topItems2.getColumnIndex("layer")),
                                                                topItems2.getInt(topItems2.getColumnIndex("color")),
                                                                topItems2.getString(topItems2.getColumnIndex("foto")));
                                                        look[2] = new Item(topItems3.getInt(topItems3.getColumnIndex("_id")),
                                                                topItems3.getString(topItems3.getColumnIndex("name")),
                                                                topItems3.getString(topItems3.getColumnIndex("style")),
                                                                topItems3.getInt(topItems3.getColumnIndex("istop")),
                                                                topItems3.getDouble(topItems3.getColumnIndex("termindex")),
                                                                topItems3.getInt(topItems3.getColumnIndex("layer")),
                                                                topItems3.getInt(topItems3.getColumnIndex("color")),
                                                                topItems3.getString(topItems3.getColumnIndex("foto")));
                                                        look[3] = new Item(botItems.getInt(botItems.getColumnIndex("_id")),
                                                                botItems.getString(botItems.getColumnIndex("name")),
                                                                botItems.getString(botItems.getColumnIndex("style")),
                                                                botItems.getInt(botItems.getColumnIndex("istop")),
                                                                botItems.getDouble(botItems.getColumnIndex("termindex")),
                                                                botItems.getInt(botItems.getColumnIndex("layer")),
                                                                botItems.getInt(botItems.getColumnIndex("color")),
                                                                botItems.getString(botItems.getColumnIndex("foto")));

                                                        result.add(look);
                                                    }
                                                }else{
                                                    Item[] look = new Item[layers + 1];

                                                    look[0] = new Item(topItems.getInt(topItems.getColumnIndex("_id")),
                                                            topItems.getString(topItems.getColumnIndex("name")),
                                                            topItems.getString(topItems.getColumnIndex("style")),
                                                            topItems.getInt(topItems.getColumnIndex("istop")),
                                                            topItems.getDouble(topItems.getColumnIndex("termindex")),
                                                            topItems.getInt(topItems.getColumnIndex("layer")),
                                                            topItems.getInt(topItems.getColumnIndex("color")),
                                                            topItems.getString(topItems.getColumnIndex("foto")));
                                                    look[1] = new Item(topItems2.getInt(topItems2.getColumnIndex("_id")),
                                                            topItems2.getString(topItems2.getColumnIndex("name")),
                                                            topItems2.getString(topItems2.getColumnIndex("style")),
                                                            topItems2.getInt(topItems2.getColumnIndex("istop")),
                                                            topItems2.getDouble(topItems2.getColumnIndex("termindex")),
                                                            topItems2.getInt(topItems2.getColumnIndex("layer")),
                                                            topItems2.getInt(topItems2.getColumnIndex("color")),
                                                            topItems2.getString(topItems2.getColumnIndex("foto")));
                                                    look[2] = new Item(topItems3.getInt(topItems3.getColumnIndex("_id")),
                                                            topItems3.getString(topItems3.getColumnIndex("name")),
                                                            topItems3.getString(topItems3.getColumnIndex("style")),
                                                            topItems3.getInt(topItems3.getColumnIndex("istop")),
                                                            topItems3.getDouble(topItems3.getColumnIndex("termindex")),
                                                            topItems3.getInt(topItems3.getColumnIndex("layer")),
                                                            topItems3.getInt(topItems3.getColumnIndex("color")),
                                                            topItems3.getString(topItems3.getColumnIndex("foto")));
                                                    look[3] = new Item(botItems.getInt(botItems.getColumnIndex("_id")),
                                                            botItems.getString(botItems.getColumnIndex("name")),
                                                            botItems.getString(botItems.getColumnIndex("style")),
                                                            botItems.getInt(botItems.getColumnIndex("istop")),
                                                            botItems.getDouble(botItems.getColumnIndex("termindex")),
                                                            botItems.getInt(botItems.getColumnIndex("layer")),
                                                            botItems.getInt(botItems.getColumnIndex("color")),
                                                            botItems.getString(botItems.getColumnIndex("foto")));

                                                    result.add(look);
                                                }
                                            }
                                        }
                                            }while (topItems3.moveToNext());

                                        }
                                    }while(topItems2.moveToNext());
                                }
                            }while (topItems.moveToNext());
                        }
                    }while(botItems.moveToNext());
                }
                topItems2.close();
                topItems3.close();
                break;
        }
        botItems.close();
        topItems.close();
        db.close();
        return result;
    }

}