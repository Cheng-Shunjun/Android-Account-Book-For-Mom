package com.example.acountformom;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private List<PeopleItem> peopleItemList;
	private Context mContext;
	private LayoutInflater mInflater = null;

	public ListViewAdapter(Context c, List<PeopleItem> peopleDetailList) {
		this.mContext = c;
		this.peopleItemList = peopleDetailList;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return peopleItemList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return peopleItemList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = null;
		convertView = mInflater.inflate(R.layout.people_list, null);

		PeoPleViewHolder holder = new PeoPleViewHolder();

		holder.mPeopleName = (TextView) convertView
				.findViewById(R.id.textview_people_name);
		holder.mPeopleName.setText(peopleItemList.get(position).peopleName);

		holder.mPeopleWeight = (TextView) convertView
				.findViewById(R.id.textview_people_weight);
		holder.mPeopleWeight.setText(peopleItemList.get(position).peopleWeight);
	
		holder.mPeopleSumWeight = (TextView) convertView
				.findViewById(R.id.textview_people_sum_weight);
		holder.mPeopleSumWeight.setText(peopleItemList.get(position).peopleSumWeight + "½ï");
		holder.mPeopleWorkNum = (TextView) convertView
				.findViewById(R.id.textview_work_num);
		holder.mPeopleWorkNum.setText(peopleItemList.get(position).peopleWorkNum + "»Ø");
		holder.mDetail = (TextView) convertView
				.findViewById(R.id.textview_detail);
		String tempString = "";
		for(int i = 0; i < peopleItemList.get(position).peopleDetail.size(); i++){	
			tempString += peopleItemList.get(position).peopleDetail.get(i);
			tempString += " ";
		}
		holder.mDetail.setText(tempString);
		
		return convertView;

	}

	private class PeoPleViewHolder {
		TextView mPeopleName;
		TextView mPeopleWeight;
		TextView mPeopleWorkNum;
		TextView mPeopleSumWeight;
		TextView mDetail;
	}

}
