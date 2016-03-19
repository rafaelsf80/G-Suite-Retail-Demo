package com.rafaelsf80.d4w.retail;


import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataTask extends AsyncTask<Void, Void, Void> {
    private final String TAG = getClass().getSimpleName();

    protected Main mActivity;

    public DataTask(Main activity) {
        this.mActivity = activity;
        Log.d(TAG, "DataTask constructor");
    }

    ArrayList<Item> items = new ArrayList<Item>();

    @Override
    protected Void doInBackground(Void... params) {
        String result = null;
        try {
            HttpClient hc = new DefaultHttpClient();
            // The script must be on gmail.com account to access anonimously
            String 	URL = "https://script.google.com/macros/s/AKfycbxFAo0wxZ7fcJEyDE0QdfGmBhFiJiA3a8YMpv9KjRGeYZdK2iU/exec";
            // Japanese spreadsheet
            //String 	URL = "https://script.google.com/macros/s/AKfycbzB40wY1F1wkn-GgJNi5qju4-enksApCbvuJN2Ty0X2IVOM9ko/exec";

            HttpGet get = new HttpGet(URL);
            HttpResponse rp = hc.execute(get);

            // grab JSON from the URL above and store it in the items class
            Log.d(TAG, "Status Code " + Integer.toString(rp.getStatusLine().getStatusCode()));
            if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                result = EntityUtils.toString(rp.getEntity());
                JSONArray objects = new JSONArray(result);

                for (int i = 0; i < objects.length(); i++) {
                    JSONObject session = objects.getJSONObject(i);
                    Item item = new Item();
                    item.itemName = session.getString("itemname");
                    item.brand = session.getString("brand");
                    item.size = session.getString("size");
                    item.imageURL = session.getString("imagelink");
                    item.sizeGuideURL = session.getString("sizeguide");
                    item.videoPreviewURL = session.getString("videopreview");
                    item.itemPrice = session.getString("price");
                    item.inventoryCount = session.getString("inventorycount");
                    item.stockForecast = session.getString("forecast");
                    items.add(item);
                    Log.d(TAG, item.toString());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading JSON", e);
            Log.e(TAG, result);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        
        mActivity.configs = mActivity.store.getData();

        
        mActivity.actionBar.setTitle(mActivity.configs.appName);
        mActivity.actionBar.setSubtitle(mActivity.configs.subTitle);
        
        mActivity.recyclerView.setHasFixedSize(true);
        mActivity.recyclerView.setAdapter(new SimpleArrayAdapter(mActivity, mActivity, items, mActivity.configs));
        mActivity.recyclerView.setItemAnimator(new DefaultItemAnimator()); 
    }
}
