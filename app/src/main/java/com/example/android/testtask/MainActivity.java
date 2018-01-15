package com.example.android.testtask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends Activity {
    private String url = "http://ainsoft.pro/test/test.xml";
    private SQLiteDatabase db;
    private Cursor cursor;
    private ProductsDatabaseHelper productsDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productsDatabaseHelper = new ProductsDatabaseHelper(this);
    }

    /**
     * Загрузка файла и заполнение базы данных при нажатии на кнопку
     */
    public void onClickDownload(View view){
        new XmlLoader().execute(url);
    }

    private class XmlLoader extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection;
            InputStream inputStream;
            byte[] buffer;
            int bufferLength;
            FileOutputStream fos;

            db = productsDatabaseHelper.getReadableDatabase();
            cursor = db.query("PRODUCTS", null, null, null,
                    null, null, null);
            if (cursor.getCount() == 0){
            /**
             * Загрузка файла
             */
                try {
                    url = new URL(params[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    fos = openFileOutput("products.xml", MODE_PRIVATE);
                    inputStream = urlConnection.getInputStream();

                    buffer = new byte[2048];

                    while ((bufferLength = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, bufferLength);
                    }
                    fos.close();
                    inputStream.close();

                    /**
                     * Вызов парсера
                     */
                    parseXmlToDatabase();
                    return true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success){
                Toast.makeText(MainActivity.this, "Загрузка завершена",
                        Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "Не удалось скачать файл или файл уже скачан",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Перенаправляение на активность списка товаров
     */
    public void onClickForward(View view){
        Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);
    }

    /**
     * Парсер xml
     */
    public void parseXmlToDatabase(){
        try {
            ProductsDatabaseHelper productsDatabaseHelper = new ProductsDatabaseHelper(this);
            db = productsDatabaseHelper.getWritableDatabase();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            FileInputStream fileInputStream = openFileInput("products.xml");
            Document document = builder.parse(fileInputStream);
            NodeList productList = document.getElementsByTagName("product");
            for (int i = 0; i < productList.getLength(); i++) {
                String name = "";
                double price = 0;
                Node product = productList.item(i);
                if (product.getNodeType() == Node.ELEMENT_NODE){
                    Element p = (Element) product;
                    NodeList params = p.getChildNodes();
                    for (int j = 0; j < params.getLength(); j++) {
                        Node param = params.item(j);
                        if (param.getNodeType() == Node.ELEMENT_NODE){
                            Element n = (Element) param;
                            switch (n.getTagName()){
                                case "name":
                                    name = n.getTextContent();
                                    break;
                                case "price":
                                    price = new Double(n.getTextContent());
                                    break;
                            }
                        }
                    }
                }
                productsDatabaseHelper.insertProduct(db, name, price);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
