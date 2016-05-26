package com.rafaelsf80.d4w.retail;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

//		//enable window content transition
//		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//
//		//set the transition
//		Transition ts = new Slide();
//		ts.setDuration(500);
//		getWindow().setEnterTransition(ts);
//		getWindow().setExitTransition(ts);

		setContentView(R.layout.itemdetails);

		// create variables to store the item details UI elements
		TextView tvBrandView = (TextView) findViewById(R.id.tv_details_title);
		Button btPdfGuideView = (Button) findViewById(R.id.bt_details_pdf_guide);
		Button btVideoPreviewView = (Button) findViewById(R.id.bt_details_video_preview);
		Button btStockView = (Button) findViewById(R.id.bt_details_instock);
		TextView tvDetailsBy = (TextView) findViewById(R.id.tv_details_by);
		TextView tvSizeView = (TextView) findViewById(R.id.tv_details_size);
		TextView tvPriceView = (TextView) findViewById(R.id.tv_details_item_price);
		TextView tvLocation = (TextView) findViewById(R.id.tv_details_location);
		TextView tvStockView = (TextView) findViewById(R.id.tv_details_instock);

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
		final String itemLocation = fromListItem.getStringExtra("itemLocation");
		String stockForecast = fromListItem.getStringExtra("stockForecast");

		// set UI information to the data which has been parsed through
		setTitle(itemName + getResources().getString(R.string.details_title_by_) + brand);

		tvBrandView.setText(itemName);
		btStockView.setText(inventoryCount + getResources().getString(R.string.details_in_stock_label));
		tvDetailsBy.setText( getResources().getString(R.string.details_by) + brand);
		tvSizeView.setText(getResources().getString(R.string.details_size) + size);
		tvPriceView.setText(getResources().getString(R.string.details_item_price) + itemPrice);
		tvLocation.setText(getResources().getString(R.string.details_item_location) + itemLocation);
		tvStockView.setText(getResources().getString(R.string.details_restock) + stockForecast);


		if (stockForecast.equals("true")) {
			tvStockView.setBackgroundColor(getResources().getColor(R.color.card_stock_alarm));
		} else {
			tvStockView.setBackgroundColor(getResources().getColor(R.color.card_nostock));
		}

		// download thumbnail
		ImageView iconView = (ImageView) findViewById(R.id.mainimage);
		Picasso.with(this)
			.load(mainImage)
			.into(iconView);


		btVideoPreviewView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "onClick btVideoPreview");
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPreview));
				startActivity(intent);

				// create new toast to update the user what video is about to be played
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.playing_video) + itemName,
						Toast.LENGTH_LONG).show();
			}

		});


		btPdfGuideView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "onClick btPdfGuide");
				String googleDocsUrl = "http://docs.google.com/viewer?url=";

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(googleDocsUrl + sizeGuide), "text/html");
				startActivity(intent);

				// create new toast to update the user that pdf is about to be opened
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.opening_pdf_guide),
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
