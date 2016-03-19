package com.rafaelsf80.d4w.retail;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


// ArrayAdapter is a special kind of ListAdapter which supplies data to ListView
public class SimpleArrayAdapter extends RecyclerView.Adapter<SimpleArrayAdapter.ViewHolder> {
	
	private final String TAG = getClass().getSimpleName();
    public static Context context;
    public Activity activity;
    private ArrayList<Item> values;
    private Config currentConfig = null;
   
    public SimpleArrayAdapter(Context context, Activity activity, ArrayList<Item> values, Config object) {
    	//super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
        this.activity = activity;

        currentConfig = object;      
    }

	public Config getCurrentConfig() {
		
		return currentConfig;
	}


	public void setCurrentConfig(Config currentConfig) {
		this.currentConfig = currentConfig;
	}

	public ArrayList<Item> getValues() {
		return values;
	}

	
	public void setValues(ArrayList<Item> values) {
		this.values = values;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return values.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder rowView, int position) {
		// get the item which has been pressed and store current item data into variables
        Item i = values.get(position);

        final String itemName = i.itemName;
        final String brand = i.brand;
        final String size = i.size;
        final String thumbnailURL = i.imageURL;
        final String sizeGuide = i.sizeGuideURL;
        final String videoPreview = i.videoPreviewURL;
        final String itemPrice = i.itemPrice;
        final String inventoryCount = i.inventoryCount;
        final String stockForecast = i.stockForecast;


        if (stockForecast == "true") {
        	rowView.cardView.setBackgroundColor(Color.parseColor("#f9320e"));
        }

        // set list menu content to variables
        rowView.tvTitle.setText(itemName);
        rowView.tvDescription.setText(context.getResources().getString(R.string.brand) + brand);
        rowView.btInventory.setText(inventoryCount + context.getResources().getString(R.string.in_stock));
        rowView.btSize.setText(context.getResources().getString(R.string.bt_sizeguide) + size);
        rowView.btPrice.setText(context.getResources().getString(R.string.price) + itemPrice);

        // download thumbnail
        Picasso.with(context)
        .load(thumbnailURL)
        .into(rowView.imThumbnail);
        
        // listener to detect whether the watch video item has been pressed
        rowView.btVideoPreview.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View v) {

        		Log.d(TAG, "onClick btVideoPreview");
        		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPreview));
        		context.startActivity(intent);

        		// create new toast to update the user what video is about to be played
        		Toast.makeText(context, "Playing " + itemName + " Preview" ,
        				Toast.LENGTH_LONG).show();

        	}
        });

        // listener to detect whether the watch video item has been pressed
        rowView.btSizeGuide.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View v) {
        		Log.d(TAG, "onClick btSizeGuide");
        		String googleDocsUrl = "http://docs.google.com/viewer?url=";
        		Intent intent = new Intent(Intent.ACTION_VIEW);

        		intent.setDataAndType(Uri.parse(googleDocsUrl + sizeGuide), "text/html");
        		context.startActivity(intent);

        		// create new toast to update the user what video is about to be played
        		Toast.makeText(context, "Opening Size Guide",
        				Toast.LENGTH_LONG).show();

        	}
        });        

        // listener to detect whether a list item has been pressed
        rowView.tvTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	
				Log.d(TAG, "onClick tvTitle");
		
        		Intent intent = new Intent(v.getContext(), ItemDetails.class);

        		// when a list item has been pressed move to the item details screen,  passing the following data				
        		intent.putExtra("itemName", itemName);			
        		intent.putExtra("mainImage", thumbnailURL);
        		intent.putExtra("brand", brand);
        		intent.putExtra("size", size);
        		intent.putExtra("sizeGuide", sizeGuide);
        		intent.putExtra("videoPreview", videoPreview);
        		intent.putExtra("itemPrice", itemPrice);
        		intent.putExtra("inventoryCount", inventoryCount);
        		intent.putExtra("stockForecast", stockForecast);
        		// move to the item details screen	
        	
        		context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        	}
        });	     
	}
	

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
		ViewHolder viewHolder = new ViewHolder(itemLayoutView);
		
		itemLayoutView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "onClick itemLayoutView");
				
			}
		});
		return viewHolder;
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder{
		
		public CardView cardView;
		public TextView tvTitle;
		public TextView tvDescription;
		public Button btInventory;
		public Button btSize;
		public Button btPrice;
		public Button btVideoPreview;;
		public Button btSizeGuide;
		public ImageView imThumbnail;
			
		public ViewHolder(View rowView) {
			super(rowView);
		
	        // store UI elements in a variable to be dynamically changed
			cardView = (CardView) rowView.findViewById(R.id.my_card_view);
	        tvTitle = (TextView) rowView.findViewById(R.id.tv_title);
	        tvDescription = (TextView) rowView.findViewById(R.id.tv_description);
	        btVideoPreview = (Button) rowView.findViewById(R.id.bt_video_preview);
	        btSizeGuide = (Button) rowView.findViewById(R.id.bt_size_guide);
	        btInventory = (Button) rowView.findViewById(R.id.bt_inventory);
	        btSize = (Button) rowView.findViewById(R.id.bt_size);
	        btPrice = (Button) rowView.findViewById(R.id.bt_price);
	        imThumbnail = (ImageView) rowView.findViewById(R.id.im_thumbnail);		
        
		}
	}
}
