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

import com.rafaelsf80.d4w.retail.Item;
import com.rafaelsf80.d4w.retail.Main;
import com.rafaelsf80.d4w.retail.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.


        String result="";




            try {
                HttpClient hc = new DefaultHttpClient();
                // The script must be on gmail.com account to access anonimously
                String 	URL = "https://script.google.com/macros/s/AKfycbyA-FsVadOuM1yxubTnqUOvnXoMUgU6kjY7Uw6C6-LcBuAZWIsX/exec";
                // Japanese spreadsheet
                // String 	URL = "https://script.google.com/macros/s/AKfycby4lAzg-j-FQJ5VBbXuoufW_ZuPF61OvcllRN1cmQkseXTkWWyC/exec";

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
                        Main.config.logo = session.getString("logolink");
                        Main.config.appName = session.getString("appname");
                        Main.config.subTitle = session.getString("subtitle");
                        Main.config.colorScheme = session.getString("colourscheme");
                        Log.d(TAG, Main.config.toString());
                    }
                }

                URL = "https://script.google.com/macros/s/AKfycbxFAo0wxZ7fcJEyDE0QdfGmBhFiJiA3a8YMpv9KjRGeYZdK2iU/exec";
                // Japanese spreadsheet
                //String 	URL = "https://script.google.com/macros/s/AKfycbzB40wY1F1wkn-GgJNi5qju4-enksApCbvuJN2Ty0X2IVOM9ko/exec";

                get = new HttpGet(URL);
                rp = hc.execute(get);

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

                        Main.items.add(item);
                        Log.d(TAG, item.toString());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading Config", e);
                Log.e(TAG, result);
            }

        getContext().getContentResolver().notifyChange(Uri.parse("content://rafa"), null, false);

        Log.d(TAG, "Bye");

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
