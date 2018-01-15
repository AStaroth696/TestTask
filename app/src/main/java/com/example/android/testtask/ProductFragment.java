package com.example.android.testtask;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;


    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView productRecycler = (RecyclerView) inflater.inflate(
                R.layout.fragment_product, container, false);

        ProductsDatabaseHelper productsDatabaseHelper = new ProductsDatabaseHelper(getActivity());
        db = productsDatabaseHelper.getReadableDatabase();
        cursor = db.query("PRODUCTS", new String[]{"_id", "NAME", "PRICE"}, null,
                null, null, null, null);
        int length = cursor.getCount();
        Integer[]ids = new Integer[length];
        String[]names = new String[length];
        double[]prices = new double[length];
        int i = 0;
        if (cursor.moveToFirst()){
            do {
                ids[i] = cursor.getInt(0);
                names[i] = cursor.getString(1);
                prices[i] = cursor.getDouble(2);
                i++;
            }while (cursor.moveToNext());
        }
        ProductAdapter adapter = new ProductAdapter(ids, names, prices);
        productRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        productRecycler.setLayoutManager(layoutManager);
        adapter.setListener(new ProductAdapter.Listener() {
            @Override
            public void itemClicked(int position) {
                Intent intent = new Intent(getActivity(), ChangeActivity.class);
                intent.putExtra(ChangeActivity.POSITION, position);
                startActivity(intent);
            }
        });
        return productRecycler;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cursor.close();
        db.close();
    }
}
