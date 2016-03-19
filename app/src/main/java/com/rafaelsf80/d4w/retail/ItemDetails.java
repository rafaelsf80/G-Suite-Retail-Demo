package com.rafaelsf80.d4w.retail;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ItemDetails extends Activity {

	private final String TAG = getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//enable window content transition
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		//set the transition
		Transition ts = new Slide();  
		ts.setDuration(500);
		getWindow().setEnterTransition(ts);
		getWindow().setExitTransition(ts);

		setContentView(R.layout.itemdetails);

		// create variables to store the item details UI elements
		TextView itemDetailsTitle = (TextView) findViewById(R.id.title);
		TextView brandTextView = (TextView) findViewById(R.id.brand);
		TextView sizeView = (TextView) findViewById(R.id.size);
		Button sizeGuideView = (Button) findViewById(R.id.sizeGuide);
		Button videoPreviewView = (Button) findViewById(R.id.videoPreview);
		Button inventoryCountView = (Button) findViewById(R.id.inventoryCount);
		TextView itemPriceView = (TextView) findViewById(R.id.itemPrice);
		TextView stockForecastView = (TextView) findViewById(R.id.stockForecast);

		// gather data which was passed from the selected list item
		Intent fromListItem = getIntent();

		final String itemName = fromListItem.getStringExtra("itemName");
		String mainImage = fromListItem.getStringExtra("mainImage");
		final String brand = fromListItem.getStringExtra("brand");
		String size = fromListItem.getStringExtra("size");
		final String sizeGuide = fromListItem.getStringExtra("sizeGuide");
		final String videoPreview = fromListItem.getStringExtra("videoPreview");
		final String itemPrice = fromListItem.getStringExtra("itemPrice");
		final String inventoryCount = fromListItem.getStringExtra("inventoryCount");
		String stockForecast = fromListItem.getStringExtra("stockForecast");

		// set UI information to the data whic	h has been parsed through

		setTitle(itemName + getResources().getString(R.string._by_) + brand);
		itemDetailsTitle.setText(itemName);
		brandTextView.setText( getResources().getString(R.string.by_details) + brand);
		sizeView.setText(getResources().getString(R.string.size_details) + size);
		inventoryCountView.setText(inventoryCount + getResources().getString(R.string.in_stock));
		itemPriceView.setText(getResources().getString(R.string.item_price) + itemPrice);


		if (stockForecast.equals("true")) {
			stockForecastView.setBackgroundColor(Color.RED);
		} else {
			stockForecastView.setBackgroundColor(Color.GREEN);
		}

		stockForecastView.setText(getResources().getString(R.string.restock) + stockForecast);


		// download thumbnail
		ImageView iconView = (ImageView) findViewById(R.id.mainimage);
		Picasso.with(this)
		.load(mainImage)
		.into(iconView	);


		videoPreviewView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPreview));
				startActivity(intent);

				// create new toast to update the user what video is about to be played
				Toast.makeText(getApplicationContext(), "Playing " + itemName + " Preview",
						Toast.LENGTH_LONG).show();
			}

		});


		sizeGuideView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String googleDocsUrl = "http://docs.google.com/viewer?url=";

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(googleDocsUrl + sizeGuide), "text/html");
				startActivity(intent);

				// create new toast to update the user what video is about to be played
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.opening_size_guide),
						Toast.LENGTH_LONG).show();

			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.itemdetails, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
