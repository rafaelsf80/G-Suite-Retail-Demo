package com.rafaelsf80.d4w.retail;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimpleArrayAdapter extends RecyclerView.Adapter<SimpleArrayAdapter.ViewHolder> {
	
	private final String TAG = getClass().getSimpleName();
    public Context context;
    public Activity activity;
    private ArrayList<Item> values;
    private String mColor;
   
    public SimpleArrayAdapter(Context context, Activity activity, ArrayList<Item> values, String color) {
    	//super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
        this.activity = activity;
        this.mColor = color;
    }

	@Override
	public int getItemCount() {
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
        final String itemLocation = i.itemLocation;
        final String stockForecast = i.stockForecast;

        if (stockForecast == "true") {
        	rowView.cardView.setBackgroundColor(Color.parseColor(mColor));
        } else {
            rowView.cardView.setBackgroundColor(context.getResources().getColor(R.color.card_nostock));
        }


        // set list menu content to variables
        rowView.tvTitle.setText(itemName);
        rowView.tvDescription.setText(context.getResources().getString(R.string.card_brand) + brand);
        rowView.btInventory.setText(inventoryCount + context.getResources().getString(R.string.card_in_stock_label));
        rowView.btSize.setText(context.getResources().getString(R.string.card_bt_size_label) + size);
        rowView.btPrice.setText(context.getResources().getString(R.string.card_price_label) + itemPrice);

        // download thumbnail
        Picasso.with(context)
            .load(thumbnailURL)
            .into(rowView.imThumbnail);
        
        // clicking the Details button will open a Details page
        rowView.btDetails.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View v) {

        		Log.d(TAG, "onClick btDetails");
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
                intent.putExtra("itemLocation", itemLocation);
                intent.putExtra("stockForecast", stockForecast);

                // move to the details screen
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        	}
        });

        // clicking the demand button will automatically prefill a form and submit a response
        rowView.btDemand.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View v) {

                Log.d(TAG, "onClick btDemand");
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

                // How to Pre-populate form and automatically submit answers:
                // Steps:
                // 1) Settings->Get pre-filled link and submit one answer to obtain the pre-filled URL
                // 2) Replace viewform? with formResponse?ifq&
                // 3) Add &submit=Submit at the end
                // https://docs.google.com/forms/d/17LzKVxCQ68EWiVVkhZ8_Sotn4cI46rk9TZvGG9FwkyY/viewform?entry.744206449=item_2&entry.1979146659=size_2&entry.875592217=brand_2

                RequestQueue mQueue = Volley.newRequestQueue(context);

                // remove spaces for URL
                String regexItemName = itemName.replaceAll("\\s+","");
                String regexSize = size.replaceAll("\\s+","");
                String regexBrand = brand.replaceAll("\\s+","");

                String FormUrl = "https://docs.google.com/forms/d/17LzKVxCQ68EWiVVkhZ8_Sotn4cI46rk9TZvGG9FwkyY/formResponse?ifq&entry.744206449=" + regexItemName + "&entry.1979146659=" + regexSize + "&entry.875592217=" + regexBrand + "&submit=Submit";

                StringRequest mRequest = new StringRequest(Request.Method.GET, FormUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        ///handle response from service
                        Log.d(TAG, "Form Response: " + s);
                        // create new toast to confirm user about demand generation
                        Toast.makeText(context, context.getResources().getString(R.string.demand_generation) + itemName,
                                Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Error sending Demand Info through Form: " + volleyError.toString());
                    }
                });

                mQueue.add(mRequest);
        	}
        });        

        // listener to detect whether a card has been pressed
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

				// move to the details screen
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
        public ImageView imThumbnail;
		public Button btDetails;
		public Button btDemand;
        public Button btInventory;
        public Button btSize;
        public Button btPrice;

		public ViewHolder(View rowView) {
			super(rowView);
		
	        // store UI elements in a variable to be dynamically changed
			cardView = (CardView) rowView.findViewById(R.id.my_card_view);
	        tvTitle = (TextView) rowView.findViewById(R.id.tv_title);
	        tvDescription = (TextView) rowView.findViewById(R.id.tv_description);
            imThumbnail = (ImageView) rowView.findViewById(R.id.im_thumbnail);

	        btDetails = (Button) rowView.findViewById(R.id.bt_card_details);
	        btDemand = (Button) rowView.findViewById(R.id.bt_card_demand);
	        btInventory = (Button) rowView.findViewById(R.id.bt_inventory);
	        btSize = (Button) rowView.findViewById(R.id.bt_size);
	        btPrice = (Button) rowView.findViewById(R.id.bt_price);
		}
	}
}
