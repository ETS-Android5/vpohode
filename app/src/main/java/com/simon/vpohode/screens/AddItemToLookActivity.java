package com.simon.vpohode.screens;

import androidx.appcompat.app.AppCompatActivity;

import com.simon.vpohode.CustomItemsAdapter;
import com.simon.vpohode.Item;
import com.simon.vpohode.R;
import com.simon.vpohode.database.DBFields;
import com.simon.vpohode.database.DatabaseHelper;
import com.simon.vpohode.managers.ListViewManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class AddItemToLookActivity extends AppCompatActivity {
    private Integer[] look;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private ListView listViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_look);
        Bundle extras = getIntent().getExtras();
        look = (Integer[]) extras.get("look");
        listViewItems = findViewById(R.id.list);
        StringBuilder stringBuilder = new StringBuilder();
        if(look != null){
            String[] lookString = new String[look.length];
            for(int i = 0; i < look.length; i++){
                lookString[i] = look[i].toString();
            }

            for (String str : lookString){
                stringBuilder.append(str);
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " + DBFields.ID.toFieldName() + " NOT IN (" + stringBuilder.toString() + ") AND " + DBFields.INWASH.toFieldName() + " = 0", null);
        Toast.makeText(this,"We found " + cursor.getCount() + " items", Toast.LENGTH_SHORT).show();
        CustomItemsAdapter customItemsAdapter = new CustomItemsAdapter(this,cursor);
        listViewItems.setAdapter(customItemsAdapter);
        ListViewManager.optimizeListViewSize(listViewItems);
        listViewItems.setOnItemClickListener((parent, view, position, id) -> {
            int index = AddLookActivity.currentLook;
            Item item = new Item().getItemById((int) id, db);
            Item[] items;
            if(!AddLookActivity.looks.isEmpty()){
                items = new Item[AddLookActivity.looks.get(index).length + 1];
                for(int i=0; i < AddLookActivity.looks.get(index).length; i++){
                    items[i] = AddLookActivity.looks.get(index)[i];
                }
                items[AddLookActivity.looks.get(index).length] = item;
                AddLookActivity.looks.set(index, items);
            }else{
                items = new Item[1];
                items[0] = item;
                AddLookActivity.looks.add(items);
            }
            finish();
        });
    }
    public void goBack(View view) {
        this.finish();
    }
}