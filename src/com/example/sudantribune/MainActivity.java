package com.example.sudantribune;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends ActionBarActivity {

	LayoutInflater inflater;
	ViewGroup container;
	//String url = "http://www.sudantribune.net/spip.php?page=backend"; //arabic 
	//String url = "http://www.sudantribune.com/spip.php?page=backend"; //english
	String url = "";
	int resource = 0;
	//public  List<newsitem> apps = new ArrayList<newsitem>(); 
	GridView list;
	ArrayAdapter<String> adapter;
	String uname;
	ArrayList<String> hrefholder = new ArrayList<String>();
	ArrayList<String> aps = new ArrayList<String>();
	ArrayList<String> articledate = new ArrayList<String>();
	ArrayList<String> imgsrc = new ArrayList<String>();
	//ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
	Bitmap bitmap;
    InputStream input;
	ProgressDialog mProgressDialog;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	Date formatedDate;
	SwipeRefreshLayout mSwipeRefreshLayout; 
	boolean rotate = false;
	
	//
	 @Override
	    protected void onSaveInstanceState(Bundle outState) {
		 	
		 	outState.putStringArrayList("hrefholder", hrefholder);
		 	outState.putStringArrayList("aps", aps);
		 	outState.putStringArrayList("articledate", articledate);
		 	outState.putStringArrayList("imgsrc", imgsrc);
	        super.onSaveInstanceState(outState);
	    }
	 
	//
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
 		
		
		
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		toolbar.setTitle("Sudan Tribune");
		toolbar.setTitleTextColor(0xFFFFFFFF);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout); //new
		
		  adapter = new MyListAdapter();
		 // if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
		  list = (GridView) findViewById(R.id.CommentListView);	
		  list.setDrawSelectorOnTop(true);
		  
		  /* Preference Activity Start */
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			String language = settings.getString("language", "1");
			String feedView = settings.getString("feedView", "1");
			
			if(language.contentEquals("2")){
				url = "http://www.sudantribune.net/spip.php?page=backend";
			} if(language.contentEquals("1")){
				url = "http://www.sudantribune.com/spip.php?page=backend";
			} else {
				url = "http://www.sudantribune.net/spip.php?page=backend";
			}
			
			if(feedView.contentEquals("1")){
				resource = R.layout.activity_news;
				//list.setDividerHeight(1);
			} if (feedView.contentEquals("2")){
				resource = R.layout.cardview;
				//list.setDividerHeight(0);
			} else{
				resource = R.layout.activity_news;
				//list.setDividerHeight(1);
			}
		  /* Preference Activity End */
		
			if(savedInstanceState == null){
			
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
			} else {
				hrefholder = new ArrayList<String>();
				aps = new ArrayList<String>();
				articledate = new ArrayList<String>();
				imgsrc = new ArrayList<String>();
				//ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
			    
				hrefholder = savedInstanceState.getStringArrayList("hrefholder");
				aps = savedInstanceState.getStringArrayList("aps");
				articledate = savedInstanceState.getStringArrayList("articledate");
				imgsrc = savedInstanceState.getStringArrayList("imgsrc");
				//bitmapArray = savedInstanceState.getParcelableArrayList("bitmapArray");
				
				adapter = new MyListAdapter();
				list = (GridView) findViewById(R.id.CommentListView);	
				
				list.setAdapter(adapter);
			}
		 
	//
	        	 mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
	        	      @Override
	        	      public void onRefresh() {
	        	    	  
	        	    	  mSwipeRefreshLayout.setRefreshing(false);
	        	
	        	    	  hrefholder.clear();
	        	    	  articledate.clear();
	        	    	  imgsrc.clear();
	        	    	//  bitmapArray.clear();
	        	    	  aps.clear();
	        	    		
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
	        	 }); 
		  
			
	        	 
	//

	    }

	
	
	
	// Title AsyncTask
			private class Title extends AsyncTask<Void, Void, Void> {

				Elements title, date, img, link;
				
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					mProgressDialog = new ProgressDialog(MainActivity.this, R.style.progress_bar_style);
				    mProgressDialog.setCancelable(false);
					mProgressDialog.show();
					
				}
				
				
				@Override
				protected Void doInBackground(Void... params) {
					
					try {
						// Connect to the web site
						Document document = Jsoup.connect(url).get();
						// Get the html document title
						Document doc;
						
						img = document.select("content|encoded");
						title = document.select("title");
						date = document.select("dc|date");
						link = document.select("link");
						
						for (Element d: date){
							doc = Jsoup.parse(d.text());
							try {
								formatedDate = format.parse(d.text());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							articledate.add(formatedDate.toString().substring(0, 10));
						}
						
						for (Element l: link){
							doc = Jsoup.parse(l.text());
							hrefholder.add(doc.text());
						} hrefholder.remove(0);
						
						for (Element i: img){
				
							doc = Jsoup.parse(i.text());
							if(doc.text().contains(doc.select("img").attr("src"))){
					
							imgsrc.add(null);
					
							}
							else {
								imgsrc.add(doc.select("img").attr("src"));
	
						
							}
							
						}
						for (Element t: title){
							doc = Jsoup.parse(t.text());
							aps.add(doc.text());							
							} aps.remove(0);
				
					
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}
		 
				@Override
				protected void onPostExecute(Void result) {
					// Set title into TextView
					
					mProgressDialog.dismiss();
					list.setAdapter(adapter); //sets listview adapter
					 
				}
			}	
	
			public class MyListAdapter extends ArrayAdapter<String>{
				public MyListAdapter(){
					super(MainActivity.this,resource,aps);
					
				}
			
				@Override
			    public View getView(int position, View convertView, ViewGroup parent) {
			          
					
					LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
					final ViewHolder mHolder;
					
					if(convertView == null){
						
					convertView = inflater.inflate(resource, parent, false);
			        mHolder = new ViewHolder();
			        
			        mHolder.nText = (TextView) convertView.findViewById(R.id.newitem);
			        mHolder.iImg = (ImageView) convertView.findViewById(R.id.imageView1);
			        mHolder.dText = (TextView) convertView.findViewById(R.id.dateitem);
					
			        convertView.setTag(mHolder);
					
			    
					} else {
						mHolder = (ViewHolder) convertView.getTag();
					}
				
					mHolder.nText.setText(aps.get(position)); //sets all the listview items 
					mHolder.dText.setText(articledate.get(position));
				//	mHolder.imgURL = imgsrc.get(position);
					
				
					Picasso.with(MainActivity.this)
	                .load(imgsrc.get(position))
	               // .noFade()
	                .placeholder(R.drawable.placeholder)
	                .into(mHolder.iImg);
					
					
					//manages listview clicks
					list.setOnItemClickListener(new OnItemClickListener() {

			            @Override
			            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			           
			            uname = hrefholder.get(position);
			          
			            	
			            Intent i = new Intent(MainActivity.this, PlaceView.class);
			             
			              i.putExtra("url name", uname); //passes the selected href into the new activity
			              
			              startActivity(i); //starts new activity
			             
			            }
			        }); //end of onitemclick
			       
					
					return convertView;
			        
			            
				}
				
				
							
				};
			
				private class ViewHolder {
					private TextView nText;
					private ImageView iImg;
				//	private String imgURL;
					private TextView dText;
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
				
				
				@Override //Inflates the menu bar items
				public boolean onCreateOptionsMenu(Menu menu) {

				getMenuInflater().inflate(R.menu.menu_main, menu);
				return super.onCreateOptionsMenu(menu);
				}

				@Override //action bar items 
			    public boolean onOptionsItemSelected(MenuItem item) { 
			        int itemId = item.getItemId();
					if (itemId == R.id.action_settings) {
						startActivity(new Intent(MainActivity.this, Preferences.class));
						finish();
						return true;
					} else {
						return super.onOptionsItemSelected(item);
					}
			    }
			
				
				
	}
	
	