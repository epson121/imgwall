package hr.best.slikozid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.commons.httpclient.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadActivity extends Activity implements LocationListener 
{
	//set up needed variables
	
	private Button doneButton;
	//url to send a GET request
	private String GETUrl = "";
	//url to send a POST request
	private String POSTUrl = "http://10.0.2.2:3000/imagewall";
	//create array list to fill list view
	private ArrayList<String> tagList = new ArrayList<String>();
	private Bundle b; 
	private String base64Image = "";
	private LocationManager locationManager;
	private String provider;
	private Communicator c;
	private byte[] image;

	//latitude and longitude strings
	String s_lat = "";
	String s_lng = "";
	
	//not used :)
	DecimalFormat df = new DecimalFormat("#.######");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//set a layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		
		//try to get image from intent to bundle and encode it as base64
		try{
			b = getIntent().getExtras();
			image = b.getByteArray("img");
			base64Image = Base64.encodeBytes(image);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "An error occured..", Toast.LENGTH_SHORT);
		}
		//if there is no image, return to main menu
		if (base64Image.equals(""))
		{
			startActivity(new Intent("hr.best.slikozid.MAIN_ACTIVITY"));
		}
		
		//create location manager instance
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//set request to location updates either from network provider or gps
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1*1000, 0.00001f, this);
		
		//instantiate criteria
		Criteria criteria = new Criteria();
		
		//get best provider
	    provider = locationManager.getBestProvider(criteria, false);
	    
	    //get location and save it to location variable
	    Location location = locationManager.getLastKnownLocation(provider);
    	if (location != null)
    	{
    		//save location as string
			s_lat = "" + location.getLatitude();
	        s_lng = "" + location.getLongitude();
	        
	        //update GETUrl string
		    GETUrl = "http://10.0.2.2:3000/imagewall.json?lat=" + s_lat + "&lng=" + s_lng;
		} 
		else 
		{
			//if there is no location, no TAGs will be shown
    		Toast.makeText(getApplicationContext(), "No GPS found.", Toast.LENGTH_SHORT).show();
	        GETUrl = "http://10.0.2.2:3000/imagewall.json";
		}
    	
    	//instantiate Communicator class
		c = new Communicator();
		Log.d("URL", GETUrl);
		try 
		{
			//try to send GET request
			c.executeHttpGet(GETUrl);
		} 
		catch (JSONException e) 
		{
			//exception handling
			Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		//reference button and set listener
		doneButton = (Button) findViewById(R.id.ul_button);
		doneButton.setOnClickListener(new View.OnClickListener() 
		{
			
			public void onClick(View v)
			{
				//create new alert dialog
				AlertDialog.Builder addTagDialog = new AlertDialog.Builder(UploadActivity.this);
				
				//create edit text to hold a new tag
				final EditText txtTag = new EditText(getApplicationContext());
				txtTag.setSingleLine(true);
				
				//change few properties of dialog
				addTagDialog.setTitle("Enter new TAG:");
				addTagDialog.setView(txtTag);
				
				//add positive button
				addTagDialog.setPositiveButton("Add", new OnClickListener() 
				{
					
					public void onClick(DialogInterface dialog, int which) 
					{
						//get inserted text
						String newTag = txtTag.getText().toString();
						Log.e("GPS", newTag);
						
						//if text is not empty
						if (!newTag.equals(""))
						{
							try 
							{
								//try to execute POST method
								c.executeHttpPost(POSTUrl, newTag);
							} 
							//exception handling
							catch (HttpException e) 
							{
								e.printStackTrace();
								Toast.makeText(getApplicationContext(), "An error occured. Check your connection or GPS.", Toast.LENGTH_LONG).show();
							} 
							catch (IOException e) 
							{
								e.printStackTrace();
								Toast.makeText(getApplicationContext(), "An error occured. Check your connection or GPS.", Toast.LENGTH_LONG).show();
							} 
						}
						else
						{
							//warn user about character count
							Toast.makeText(getApplicationContext(), "Please enter at least one character.", Toast.LENGTH_SHORT).show();
						}
						//return to main after upload
						startActivity(new Intent("hr.best.slikozid.MAIN_ACTIVITY"));
						
					}
				});
				//show the dialog
				addTagDialog.show();
			}
		});
		
		//reference listview
		ListView listView = (ListView) findViewById(R.id.list);
		
		//fill listview from arraylist
        ArrayAdapter<String> arrayAdapter =      
        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagList);
        if (tagList.size() == 0)
        {
        	Toast.makeText(getApplicationContext(), "There are no TAGs near you.", Toast.LENGTH_SHORT).show();
        }
        
        //set onclick listener for listview items
        listView.setOnItemClickListener(new OnItemClickListener() 
        {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				try 
				{
					//on item click execute POST request with the current TAG
					c.executeHttpPost(POSTUrl, (String) ((TextView) arg1).getText());
				} 
				catch (HttpException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				} 
				
				//return to main menu
				startActivity(new Intent("hr.best.slikozid.MAIN_ACTIVITY"));
			}
			
		});
        
        //fill the list
        listView.setAdapter(arrayAdapter);
	}
	
	//overrided GPS methods
	@Override
	  protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	  }
	
	@Override
	  protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this);
	  }
	
	//subclass that handles GET and POST requests
	private class Communicator
	{
		//few variables
		BufferedReader reader;
		JSONArray finalResult;

		//method that axecutes GET method
		protected int executeHttpGet(String url) throws JSONException 
		{
			//create HTTP client
			DefaultHttpClient client = new DefaultHttpClient();
			
			//create getMethod instance with url from param
			HttpGet get = new HttpGet(url);
			try 
			{
				//execute GET request
		        HttpResponse resp = client.execute(get);
		        
		        //read the response
		        reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()), 8192);
		        
		        //check status line and status code (not really needed)
		        StatusLine statusLine = resp.getStatusLine();
		        int statusCode = statusLine.getStatusCode();
		        Log.i("Statuscode", "statusCode"+statusCode);
		        
		        //read lines from response and save them to JSONArray
		        String line = reader.readLine();
		        JSONTokener tokener = new JSONTokener(line);
				finalResult = new JSONArray(tokener);
		        reader.close();
		        
		        //parse JSON and fil the ArrayList
		        for (int i = 0; i < finalResult.length(); i++) 
		        {
		            JSONObject node = finalResult.getJSONObject(i);
		            String tag = node.getString("tag");
		            tagList.add(tag);
		        }
		        
		    } 
			//exception handling
			catch (IOException e1) 
		    {
		        e1.printStackTrace();
		    }
			return 0;
		}
		
		//method that executes POST
		protected int executeHttpPost(String url, String tag) throws HttpException, IOException
		{
			//create htto client instance
			HttpClient client = new HttpClient();
			
			//Create POST method
			PostMethod method = new PostMethod(url);
						
			// Set parameters on POST    
            method.setParameter("tag", tag );
            method.setParameter("lat", s_lat);
            method.setParameter("lng", s_lng);
            method.addParameter("user", "Android" );
            method.addParameter("img", base64Image);
			
            // Execute and print response
			client.executeMethod( method );
			String response = method.getResponseBodyAsString();
			
			//check response and Toast the result
			if (response.equals("1"))
			{
				Toast.makeText(getApplicationContext(), "Image has been successfully uploaded.", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "This image has been uploaded recentlky... *recently", Toast.LENGTH_LONG).show();
			}
			
			//finish
			method.releaseConnection();
			
			return 0;
		}
	}

	//GPS stuff
	public void onLocationChanged(Location location) 
	{
		s_lat = "" +  location.getLatitude();
	    s_lng = "" +  location.getLongitude();
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
