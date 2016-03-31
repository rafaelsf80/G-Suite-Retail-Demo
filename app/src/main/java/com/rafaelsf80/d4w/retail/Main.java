package com.rafaelsf80.d4w.retail;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rafaelsf80.d4w.retail.sync.D4WSyncAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class Main extends Activity {

    private final String TAG = getClass().getSimpleName();

    // Instead of Content Providers, we use Singleton classes for storage
    public static ArrayList<Item> items = new ArrayList<Item>();
    public static Config config = new Config();

    // URLs of Spreadsheets: scripts must be on gmail.com account to access anonimously
    public static String ConfigUrl = "https://script.google.com/macros/s/AKfycbyA-FsVadOuM1yxubTnqUOvnXoMUgU6kjY7Uw6C6-LcBuAZWIsX/exec";
    // Japanese config spreadsheet
    // String ConfigURL = "https://script.google.com/macros/s/AKfycby4lAzg-j-FQJ5VBbXuoufW_ZuPF61OvcllRN1cmQkseXTkWWyC/exec";
    public static String DataUrl = "https://script.google.com/macros/s/AKfycbxFAo0wxZ7fcJEyDE0QdfGmBhFiJiA3a8YMpv9KjRGeYZdK2iU/exec";
    // Japanese data spreadsheet
    //String 	URL = "https://script.google.com/macros/s/AKfycbzB40wY1F1wkn-GgJNi5qju4-enksApCbvuJN2Ty0X2IVOM9ko/exec";
    // URL of form: refer to SimpleArrayAdapter.java to modify accordingly
    // https://docs.google.com/forms/d/17LzKVxCQ68EWiVVkhZ8_Sotn4cI46rk9TZvGG9FwkyY/viewform?entry.744206449=item_2&entry.1979146659=size_2&entry.875592217=brand_2

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.rafaelsf80.d4w.retail.sync.D4WSyncAdapter";

    public RecyclerView recyclerView;
    public ActionBar actionBar;

    public ContentObserver mObserver;
    Main mActivity;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        mActivity = this;

        // enable window content transition
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // set the transition
        Transition ts = new Explode();
        ts.setStartDelay(500);
        // set the duration
        ts.setDuration(500);
        getWindow().setEnterTransition(ts);
        // set an exit transition so it is activated when the current activity exits
        getWindow().setExitTransition(ts);

        setContentView(R.layout.main);
        actionBar = getActionBar();
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);

        // Init sync adapter
        D4WSyncAdapter.initializeSyncAdapter(this);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(D4WSyncAdapter.getSyncAccount(this),
                getString(R.string.content_authority), bundle);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // If would like to use a GridLayout, change above line by this one
        // recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        // TODO: Float Button
//    	final int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
//    	final ImageButton imageButton = (ImageButton) findViewById(R.id.fab_1);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//
//    		@Override
//    		public void onClick(View view) {
//    			Toast.makeText(Main.this, "Clicking on Float button",
//    					Toast.LENGTH_LONG).show();
//    		}
//    	});

        // Even if we use a stub (dummy) content provider, we declare an observer
        // to talk to our sync adapter and receive updates
        mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                Log.d(TAG, "ContentObserver on Change()");
                actionBar.setTitle(config.appName);
                actionBar.setSubtitle(config.subTitle);
                Picasso.with(mActivity)
                        .load(config.logo)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Drawable d = new BitmapDrawable(getResources(), bitmap);
                                actionBar.setIcon(d);
                                actionBar.setDisplayShowHomeEnabled(true);
                                //actionBar.setDisplayHomeAsUpEnabled(true);
                                actionBar.setDisplayShowTitleEnabled(true);
                            }
                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });

                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(new SimpleArrayAdapter(mActivity, mActivity, items, config.colorScheme));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        };
        getContentResolver().registerContentObserver(Uri.parse("content://rafa"), true, mObserver);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_feedback) {

            final EditText etCustomerEmail = new EditText(this);
            new AlertDialog.Builder(this).setTitle("Feedback Email")
                    .setMessage("Introduce customer email:")
                    .setView(etCustomerEmail)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            sendEmail( etCustomerEmail.getText().toString() );
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .show();
            return true;
        }
        if (id == R.id.options_refresh_data) {

            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            ContentResolver.requestSync(D4WSyncAdapter.getSyncAccount(this),
                    getString(R.string.content_authority), bundle);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendEmail(String emailAddress) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/html");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{emailAddress});
        i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_feedback_subject));
        i.putExtra(Intent.EXTRA_TEXT   , Html.fromHtml(getResources().getString(R.string.email_feedback_text)));
        try {
            startActivity(Intent.createChooser(i, getResources().getString(R.string.email_feedback_action)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Main.this,  getResources().getString(R.string.email_feedback_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.rafaelsf80.d4w.retail/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.rafaelsf80.d4w.retail/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}