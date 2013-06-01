package au.com.thecommuters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button saveBtn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		saveBtn = (Button) findViewById(R.id.saveProfileBtn);

		saveBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		
		
		Intent i = new Intent(this,SolutionActivity.class);
		
		startActivity(i);
		
	}
	
	

}
