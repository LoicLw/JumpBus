package au.com.thecommuters;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DriverRatingActivity extends Activity implements OnClickListener{
	
	private Button rate;
	
	private Button waitReq;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_driver_rate);
		
		rate = (Button)findViewById(R.id.rateBtn);
		
		rate.setOnClickListener(this);
		
		waitReq = (Button)findViewById(R.id.waitReq);
		
		waitReq.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.waitReq:
			Toast.makeText(this, "Request has been sent to driver, you may have 2 minutes, hurry up!", Toast.LENGTH_LONG).show();
			waitReq.setClickable(false);
			waitReq.setText("No more request");
			break;
		case R.id.rateBtn:
			Toast.makeText(this, "Thank you !", Toast.LENGTH_LONG).show();
			
			finish();
			break;
		}
		
		
	}

}
