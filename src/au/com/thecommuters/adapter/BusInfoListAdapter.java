package au.com.thecommuters.adapter;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.ItemizedOverlay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import au.com.thecommuters.R;
import au.com.thecommuters.model.BusDataFromRequest;

public class BusInfoListAdapter extends BaseAdapter {

	private ArrayList<BusDataFromRequest> bus_requests;

	private LayoutInflater itemLayoutInflater;

	private ViewHolder item;

	private Context context;

	public BusInfoListAdapter(Context ctx, ArrayList<BusDataFromRequest> data) {
		context = ctx;

		bus_requests = data;

		itemLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return bus_requests.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bus_requests.get(position);
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

		itemHolder.direction = (TextView) view.findViewById(R.id.directionTxt);
		
		itemHolder.line = (TextView)view.findViewById(R.id.lineTxt);
		
		itemHolder.time = (TextView)view.findViewById(R.id.timeTxt);
		
		BusDataFromRequest data = (BusDataFromRequest)getItem(position);
		if(data!=null){
			itemHolder.direction.setText(data.getDirection());
			itemHolder.time.setText(String.valueOf(data.getAimedArrivalTime()));
			itemHolder.line.setText(data.getLine());
		}

		return view;
	}

	private final class ViewHolder {
		public TextView time;

		public TextView direction;

		public TextView line;
	}
}
