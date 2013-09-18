package hr.best.slikozid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 //set layout and start new thread
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_splash);
	        Thread timer = new Thread(){
	        	public void run()
	        	{
	        		//run thread
	        		try
	        		{
	        			int timer = 0;
	        			while (timer < 2000)
	        			{
	        				sleep(100);
	        				timer += 100;
	        			}
	        			
	        			//after the counter hits 2000 start new activity
	        			startActivity(new Intent("hr.best.slikozid.MAIN_ACTIVITY"));
	        		} 
	        		catch (InterruptedException e) 
	        		{
						e.printStackTrace();
					}
	        		finally
	        		{
	        			//finish the thread
	        			finish();
	        		}
	        	}
	        };
	        //start the timer
	        timer.start();
	        
	}
}
