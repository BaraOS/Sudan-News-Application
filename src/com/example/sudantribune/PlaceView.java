package com.example.sudantribune;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceView extends ActionBarActivity {

	TextView text;
	ImageView image;
	String url;
	Bitmap bitmap = null;
	ProgressDialog mProgressDialog;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_place_view);
	    
	    
	    //getActionBar().setDisplayHomeAsUpEnabled(true);
	    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
	    setSupportActionBar(toolbar);
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    toolbar.setTitle("News");
	    toolbar.setTitleTextColor(0xFFFFFFFF);
	    
	    
		  Intent in = getIntent();
		 
		  String u = in.getExtras().getString("url name");
		  
		  url = u;
		
		  try
      	{
      	   if (isNetworkAvailable(getApplicationContext()))
      			
      		   new Title().execute();
      		   else 
      			   Toast.makeText(getApplicationContext(), "No Internet Conecction", Toast.LENGTH_LONG).show();
      	}
      	catch (Exception ex)
      	{
      	   // Connection time out
      		
      	}
	
	 } 
	
	
	// Title AsyncTask
			private class Title extends AsyncTask<Void, Void, Void> {
				//String title;
				Elements Title,img,description,Paragraph;
				String test = "";
				//Bitmap bitmap;
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					mProgressDialog = new ProgressDialog(PlaceView.this, R.style.progress_bar_style);
				    mProgressDialog.setCancelable(false);
					mProgressDialog.show();
					
				}
		 
				@Override
				protected Void doInBackground(Void... params) {
					try {
						// Connect to the web site
						Document document = Jsoup.connect(url).get();
						// Get the html document title
						Title = document.select("#titreArticle");
						//News = document.select("#text");
						 Paragraph = document.select("div > div > div > div > div > div > p");
						for(Element p: Paragraph){
							
						
						test = test.concat("\n" + p.text() + "\n");
						
						}
						
						img = document.select("#text > dl > dt:nth-child(1) > img");
						String imgSrc = img.attr("src");
						String newurl = url.substring(0, 28) + imgSrc;
						InputStream input = new java.net.URL(newurl).openStream();
						bitmap = BitmapFactory.decodeStream(input);
						description = document.select("strong");
											
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}
		 
				@Override
				protected void onPostExecute(Void result) {
					// Set title into TextView
					
					TextView title = (TextView) findViewById(R.id.Title);
					title.setText(Title.text());
					
					TextView imgdescription = (TextView) findViewById(R.id.photodescription);
					imgdescription.setText(description.text());
					
					text = (TextView) findViewById(R.id.story);
					
					text.setText(test);
					
					ImageView logoimg = (ImageView) findViewById(R.id.imageView1);
					logoimg.setImageBitmap(bitmap);
					
					if (bitmap == null){
						logoimg.setVisibility(View.GONE);
						imgdescription.setVisibility(View.GONE);
					}
					
					mProgressDialog.dismiss();
				
				}
			}	
			

			public void actionShare() { //method for share button 
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, url);
				startActivity(Intent.createChooser(shareIntent, "Share With..."));
			}
			
		
			
			@Override //Inflates the menu bar items
			public boolean onCreateOptionsMenu(Menu menu) {

			getMenuInflater().inflate(R.menu.main, menu);
			return super.onCreateOptionsMenu(menu);
			}
			
			

			@Override //action bar items 
		    public boolean onOptionsItemSelected(MenuItem item) { 
		        int itemId = item.getItemId();
				if (itemId == android.R.id.home) {
					onBackPressed();
					return true;
				} else if (itemId == R.id.action_share) {
					actionShare();
					return true;
				} else {
					return super.onOptionsItemSelected(item);
				}
		    }

			//
			public static boolean isNetworkAvailable(Context context)
			{
			    ConnectivityManager connectivityManager = 
			        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

			    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
			}
			//
	
}