package hr.best.slikozid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class CameraActivity extends Activity 
{
	//set needed variables
	private int CAMERA_PIC_REQUEST = 5;
	private Button btnSaveImage;
	private Button btnBackToMain;
	private ImageView imageView;
	private byte[] imageBytes = null;
	String  PICTURES_DIR = "Pictures/";
	
	//get external card path
	String path = Environment.getExternalStorageDirectory() + "/";
	UUID image_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//set a layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		//reference buttons and imagevire 
		btnSaveImage = (Button) findViewById(R.id.btn_upload);
		btnBackToMain = (Button) findViewById(R.id.btn_back_to_main);
		this.imageView = (ImageView) this.findViewById(R.id.taken_picture);
		
		//create camera intent
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		
		//create image URI to be used to referencec an image
		image_id = UUID.randomUUID();
		Uri uriSavedImage=Uri.fromFile(new File(path + image_id));
		
		//set URI as "extra" to camera intent
		cameraIntent.putExtra("output", uriSavedImage);
		
		//start camera activity
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        
        //setup on click listener for button
        btnSaveImage.setOnClickListener(new View.OnClickListener() 
        {
			
			public void onClick(View v) 
			{
				Log.d("tag", imageBytes.toString());
				//if picture is taken with camera
				if (imageBytes != null)
				{
					//create intent for upload
					Intent uploadIntent = new Intent("hr.best.slikozid.UPLOAD_ACTIVITY");
					
					//put image byte[] as "extra"
					uploadIntent.putExtra("img", imageBytes);
					
					//Start the activity
					startActivity(uploadIntent);
				}
				else
				{
					//return to the main menu
					Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
					startActivity(new Intent("hr.best.slikozid.MAIN_ACTIVITY"));
				}
			}
		});
		
		btnBackToMain.setOnClickListener(new View.OnClickListener() 
		{
			
			public void onClick(View v) 
			{
				//return to the main menu
				startActivity(new Intent("hr.best.slikozid.MAIN_ACTIVITY"));
			}
		});
        
	}
	
	//when an activity ends
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		//if activity is camera activity and code is "OK"
		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) 
		{
			//get photo into Bitmap variable
			Bitmap photo = BitmapFactory.decodeFile(path + image_id);
			
			//set image to imageView
			imageView.setImageBitmap(photo);
			
			//compress picture and create byte array 
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		    imageBytes = stream.toByteArray();
		}
		//if activity is camera activity and code is "CANCELED"
		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_CANCELED)
		{
			//return to main menu
			startActivity(new Intent("hr.best.slikozid.MAIN_ACTIVITY"));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

}
