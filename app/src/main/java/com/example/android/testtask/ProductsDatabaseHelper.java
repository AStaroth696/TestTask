package com.example.android.testtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Помощник SQLite для работы с базой данных
 */

public class ProductsDatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "products";
    private static final int DB_VERSION = 1;

    ProductsDatabaseHelper (Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateDatabase(sqLiteDatabase, i, i1);
    }

    private void updateDatabase (SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 1){
            db.execSQL("CREATE TABLE PRODUCTS("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "NAME TEXT, "
            + "PRICE REAL);");
        }
    }

    /**
     *Заполнение базы данных
     */
    public void insertProduct(SQLiteDatabase db, String name, double price){
        ContentValues productValues = new ContentValues();
        productValues.put("NAME", name);
        productValues.put("PRICE", price);
        db.insert("PRODUCTS", null, productValues);
    }

    /**
     *Обновление цены
     */
    public void updateProduct(SQLiteDatabase db, Integer id, double price){
        ContentValues productValues = new ContentValues();
        productValues.put("PRICE", price);
        db.update("PRODUCTS", productValues, "_id=?",
                new String[]{String.valueOf(id)});
    }
}
