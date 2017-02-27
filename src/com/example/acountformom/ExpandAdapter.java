package com.example.acountformom;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null;
	private List<String> mGroupName = null;
	private List<List<ListItem>> mData = null;
	private List<String> mWeight;

	public ExpandAdapter(Context ctx, List<List<ListItem>> data,
			List<String> groupName, List<String> groupWeight) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGroupName = groupName;
		mData = data;
		mWeight = groupWeight;
	}

	public void setData(List<List<ListItem>> list, List<String> groupName, List<String> groupWeight) {
		mGroupName = groupName;
		mData = list;
		mWeight = groupWeight;	
	}
	

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).size();
	}

	@Override
	public List<ListItem> getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition);
	}

	@Override
	public ListItem getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.group_item_layout, null);
		}
		GroupViewHolder holder = new GroupViewHolder();

		holder.mGroupName = (TextView) convertView
				.findViewById(R.id.group_name);
		holder.mGroupName.setText(mGroupName.get(groupPosition));
		//holder.mGroupName.setText("name");
		
		holder.mGroupWeight = (TextView) convertView
				.findViewById(R.id.group_weight);
		holder.mGroupWeight.setText("(�ϼ�" + mWeight.get(groupPosition) + "��)");
		//holder.mGroupWeight.setText("(123540)");

		holder.mItemCount = (TextView) convertView
				.findViewById(R.id.group_count);
		holder.mItemCount.setText(mData.get(groupPosition).size() + "");
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.child_item_layout, null);
		}
		ChildViewHolder holder = new ChildViewHolder();

		holder.mItemName = (TextView) convertView.findViewById(R.id.item_name);
		holder.mItemName.setText(getChild(groupPosition, childPosition)
				.getName());

		holder.mSumWeight = (TextView) convertView
				.findViewById(R.id.item_weight);
		holder.mSumWeight.setText("(�ϼ�"
				+ getChild(groupPosition, childPosition).getSumWeight() + "��)");

		holder.mTime = (TextView) convertView.findViewById(R.id.item_time);
		holder.mTime.setText(getChild(groupPosition, childPosition).getTime());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		/* 很重要：实现ChildView点击事件，必须返回true */
		return true;
	}

	private class GroupViewHolder {
		TextView mGroupName;
		TextView mGroupWeight;
		TextView mItemCount;
	}

	private class ChildViewHolder {
		TextView mItemName;
		TextView mSumWeight;
		TextView mTime;
	}

}
