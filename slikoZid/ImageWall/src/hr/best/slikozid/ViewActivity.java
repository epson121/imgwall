package hr.best.slikozid;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ViewActivity extends Activity 
{
	private Button btnSaveImage;
	private Button btnBackToMain;
	private ImageView imageView;
	private int ACTIVITY_SELECT_IMAGE = 100;
	private byte[] imageBytes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// set layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		//reference buttons and imageview
		btnSaveImage = (Button) findViewById(R.id.btn_upload);
		btnBackToMain = (Button) findViewById(R.id.btn_back_to_main);
		this.imageView = (ImageView) this.findViewById(R.id.taken_picture);
		
		//create new intent for selecting image from gallery
		Intent i = new Intent(Intent.ACTION_PICK,
	               android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		//start the activity
		startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
		
		//setup onclick listener for button
		btnSaveImage.setOnClickListener(new View.OnClickListener() 
        {
			
			public void onClick(View v) 
			{
				try
				{
					//if clicked, create new intent, put image as "extra" and start the activity for upload
					Intent uploadIntent = new Intent("hr.best.slikozid.UPLOAD_ACTIVITY");
					uploadIntent.putExtra("img", imageBytes);
					startActivity(uploadIntent);
				}
				//catch exception
				catch (NullPointerException e)
				{
					Toast.makeText(getApplicationContext(), "No image selected..", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		//on click listener for back button
		btnBackToMain.setOnClickListener(new View.OnClickListener() 
		{
			
			public void onClick(View v) 
			{
				//go to main menu
				startActivity(new Intent("hr.best.slikozid.MAIN_ACTIVITY"));
			}
		});
	}
	
	//on activity result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// if activity is the one we want (select image from gallery)
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK)  
		{
			//get images URI
            Uri selectedImage = data.getData();
            
            //get images path column
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            
            //use cursor to get the images path
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            
            //save image to bitmap
            Bitmap myImage = BitmapFactory.decodeFile(filePath);
            
            //scale it down so it can be shown on screen
            Bitmap smallerImage = scaleDownBitmap(myImage, 100, getApplicationContext());
            
            //set image to imageView
            imageView.setImageBitmap(smallerImage);
            
            //create byte[] from image
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			myImage.compress(Bitmap.CompressFormat.JPEG, 55, stream);
			imageBytes = stream.toByteArray();
		}
	}
	
	//method for scaling image
	public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {
		 
		//get displays density
		 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        
		 
		 //get new height and new width
		 int h = (int) (newHeight*densityMultiplier);
		 int w = (int) (h * photo.getWidth()/((double) photo.getHeight()));
		 
		 //save it to the photo bitmap
		 photo = Bitmap.createScaledBitmap(photo, w, h, true);
		 
		 //return bitmap
		 return photo;
		 }
}
