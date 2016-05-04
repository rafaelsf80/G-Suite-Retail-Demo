package com.rafaelsf80.d4w.retail.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rafaelsf80.d4w.retail.Item;
import com.rafaelsf80.d4w.retail.Main;
import com.rafaelsf80.d4w.retail.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class D4WSyncAdapter extends AbstractThreadedSyncAdapter {

    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;

    private final String TAG = getClass().getSimpleName();

    public D4WSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.d(TAG, "Starting sync");
        String result="";
        Main.items.clear();

        RequestQueue mQueue = Volley.newRequestQueue(getContext());

        // FIRST HTTP REQUEST: grab JSON from the URL above and store it in the Main.config object
        StringRequest mRequest = new StringRequest(Request.Method.GET, Main.ConfigUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ///handle response from service
                Log.d(TAG, "Response: " + s);

                try {
                    JSONArray objects = new JSONArray(s);
                    for (int i = 0; i < objects.length(); i++) {
                        JSONObject session = objects.getJSONObject(i);
                        Main.config.logo = session.getString("logolink");
                        Main.config.appName = session.getString("appname");
                        Main.config.subTitle = session.getString("subtitle");
                        Main.config.colorScheme = session.getString("colourscheme");
                        Log.d(TAG, Main.config.toString());
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Error loading Config: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Response: " + volleyError.toString());
            }
        });

        mQueue.add(mRequest);

        // SECOND HTTP REQUEST: grab JSON from the URL above and store it in the items class
        mRequest = new StringRequest(Request.Method.GET, Main.DataUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ///handle response from service
                Log.d(TAG, "Response: " + s);

                try {
                    JSONArray objects = new JSONArray(s);

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
                        item.itemLocation = session.getString("location");
                        item.stockForecast = session.getString("forecast");

                        Main.items.add(item);
                        Log.d(TAG, item.toString());
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Error loading Config: " + e.toString());
                }
                getContext().getContentResolver().notifyChange(Uri.parse("content://rafa"), null, false);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Response: " + volleyError.toString());
            }
        });

        mQueue.add(mRequest);
        return;
    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {

        Account account = getSyncAccount(context);
//        String authority = context.getString(R.string.content_authority);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // we can enable inexact timers in our periodic sync
//            SyncRequest request = new SyncRequest.Builder().
//                    syncPeriodic(syncInterval, flexTime).
//                    setSyncAdapter(account, authority).
//                    setExtras(new Bundle()).build();
//            ContentResolver.requestSync(request);
//        } else {
//            ContentResolver.addPeriodicSync(account,
//                    authority, new Bundle(), syncInterval);
//        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        Log.d("HOLA", "getSyncAccount()");
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        //D4WSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        //ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
