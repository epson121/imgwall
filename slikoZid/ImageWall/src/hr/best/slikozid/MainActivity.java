package hr.best.slikozid;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import android.widget.Button;

public class MainActivity extends Activity {
	//set variables
	Button btnUseCamera;
	Button btnUpFromFile;
	String encodedImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//set layout view
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//reference buttons
		btnUseCamera = (Button) findViewById(R.id.btn_use_camera);
		btnUpFromFile = (Button) findViewById(R.id.btn_up_from_file);
		
		//set a listener for button
		btnUseCamera.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent("hr.best.slikozid.CAMERA_ACTIVITY"));
			}
		});
		
		//set a listener for button
		btnUpFromFile.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) 
			{
				Intent uploadIntent = new Intent("hr.best.slikozid.VIEW_ACTIVITY");
				startActivity(uploadIntent);
			}
		});
	}
	

}
