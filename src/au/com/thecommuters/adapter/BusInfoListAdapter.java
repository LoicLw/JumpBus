package au.com.thecommuters.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import au.com.thecommuters.MainActivity;
import au.com.thecommuters.R;
import au.com.thecommuters.SolutionActivity;
import au.com.thecommuters.model.BusDataFromRequest;
import au.com.thecommuters.model.Constant;

public class BusInfoListAdapter extends BaseAdapter {

	private Set<BusDataFromRequest> bus_requests;

	private LayoutInflater itemLayoutInflater;

	private ViewHolder item;

	private Context context;
	
	private Object[] iterator;

	public BusInfoListAdapter(Context ctx, final Set<BusDataFromRequest> data) {
		context = ctx;

		bus_requests = data;

		itemLayoutInflater = LayoutInflater.from(context);
		
		//iterator = bus_requests.iterator();
		
		iterator = data.toArray();
	}

	@Override
	public int getCount() {

		return iterator.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return iterator[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = itemLayoutInflater.inflate(R.layout.bus_info_item, null);

		ViewHolder itemHolder = new ViewHolder();

		LinearLayout itemLayout = (LinearLayout)view.findViewById(R.id.itemLayout);
		
		itemHolder.line = (TextView)view.findViewById(R.id.lineTxt);
		
		itemHolder.time = (TextView)view.findViewById(R.id.busStatus);
		
		itemHolder.leaveTime = (TextView)view.findViewById(R.id.leaveTime);
		
		itemHolder.aimedTime = (TextView)view.findViewById(R.id.aimedTime);
		
		itemHolder.expArriveTime = (TextView)view.findViewById(R.id.expectArriveTime);
		
		itemHolder.busStar = (ImageView)view.findViewById(R.id.imageView1);
		
		
		BusDataFromRequest data = (BusDataFromRequest)getItem(position);
		if(data!=null){
			long delay = data.getLatestExpArrivalTime() - data.getAimedArrivalTime();
			if(delay > 0){
				itemHolder.time.setText("It is delayed for "+(((delay/1000)/60)==0?" ":((delay/1000)/60)+" minutes ")+(((delay/1000)%60)==0?" ":((delay/1000)%60)+" seconds"));
				itemLayout.setBackgroundColor(Color.rgb(243, 105, 123));
			}
			
			
			else if(delay == 0){
				itemHolder.time.setText("It is on time");
				itemLayout.setBackgroundColor(Color.rgb(131, 177, 150));
			}
				//Toast.makeText(SolutionActivity.this, "Bus "+info.getLine()+" is on time", Toast.LENGTH_SHORT).show();
			else{
				itemHolder.time.setText("It is "+((0-(delay/1000)/60)==0?" ":(0-(delay/1000)/60)+" minutes ")+((0-(delay/1000)%60)==0?" ":(0-(delay/1000)%60)+" seconds")+" ahead of schedule");
				itemLayout.setBackgroundColor(Color.rgb(97,118,130));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.UK);
			String leaveTime = sdf.format(new Date(data.getLatestExpArrivalTime()-Constant.BUS_DURATION_TO_KLEMZIG));
			//Toast.makeText(SolutionActivity.this, "Bus "+info.getLine()+ " is " + +" minutes ahead of schdule", Toast.LENGTH_SHORT).show();
			itemHolder.leaveTime.setText("The time by which you should leave: "+leaveTime);
			itemHolder.line.setText("Bus "+data.getLine());
			itemHolder.aimedTime.setText("Aimed arrival time: "+sdf.format(new Date(data.getAimedArrivalTime())));
			itemHolder.expArriveTime.setText("Expected arrival time: "+ sdf.format(new Date(data.getLatestExpArrivalTime())));
			
			System.out.println("Bus from google "+ MainActivity.getBus_name_from_google() );
			System.out.println("Bus "+ data.getLine());
			if (!MainActivity.getBus_name_from_google().contains(data.getLine())){
				itemHolder.busStar.setVisibility(View.GONE);
			}
			
			
		}

		return view;
	}

	private final class ViewHolder {
		public TextView time;

		public TextView leaveTime;

		public TextView line;
		
		public TextView aimedTime;
		
		public TextView expArriveTime;
		
		public ImageView busStar;
	}
}
