package com.example.android.testtask;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
*Активность изменения цены
 * */
public class ChangeActivity extends Activity {

    public static final String POSITION = "position";
    private ProductsDatabaseHelper productsDatabaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        id = (Integer) getIntent().getExtras().get(POSITION);
        productsDatabaseHelper = new ProductsDatabaseHelper(this);
        db = productsDatabaseHelper.getReadableDatabase();
        cursor = db.query("PRODUCTS", null, "_id=?",
                new String[]{String.valueOf(id+1)}, null, null, null);
        TextView name = (TextView)findViewById(R.id.name);
        if (cursor.moveToFirst()) {
            name.setText(cursor.getString(1));
        }
    }

    /**
     * Обработка нажатия кнопки сохранения изменений
     */
    public void onSaveClick(View view){
        EditText priceEdit = (EditText)findViewById(R.id.price);
        if (priceEdit.getText().length()>0) {
            double price = Double.parseDouble(priceEdit.getText().toString());
            db = productsDatabaseHelper.getWritableDatabase();
            productsDatabaseHelper.updateProduct(db, id + 1, price);
            Toast.makeText(ChangeActivity.this, "База данных обновлена", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(ChangeActivity.this, "Введите новую цену", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
