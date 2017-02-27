package com.example.acountformom;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.R.string;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		OnChildClickListener, DialogInput.OnStringEnteredListener {
	private ExpandableListView mListView = null;
	private ExpandAdapter mAdapter;
	private TextView textViewTitle = null;
	private Button buttonAddGroup;
	private String yearString;
	private List<String> mGroupName = new ArrayList<String>();
	private List<String> mGroupWeight = new ArrayList<String>();
	private List<List<ListItem>> mData = new ArrayList<List<ListItem>>();
	private int selectedgroupPosition;
	private int selectedchildPosition;
	private Calendar nowCalendar;
	String yearDir;

	List<String> yearArray = new ArrayList<String>();
	ArrayAdapter<String> mSpinnerAdapter;
	NavigationListsener mNavigationListener = new NavigationListsener();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nowCalendar = Calendar.getInstance();
		yearString = nowCalendar.get(Calendar.YEAR) + "";
		yearDir = this.getExternalFilesDir(null).toString() + "/" + yearString;
		File file = new File(yearDir);
		if(!file.exists() || file.isFile()){
			file.mkdirs();		//如果今年的文件不存在，创建今年的文件夹
		}
		mListView = (ExpandableListView) findViewById(R.id.expandableListViewAcount);
		mListView.setGroupIndicator(getResources().getDrawable(
				R.drawable.expander_floder));
		mListView.setOnChildClickListener(this);
		textViewTitle = (TextView) findViewById(R.id.textview_title);
		//textViewTitle.getBackground().setAlpha(100);
		mAdapter = new ExpandAdapter(this, mData, mGroupName, mGroupWeight);
		mListView.setAdapter(mAdapter);
		refreshData();
		registerForContextMenu(mListView);
		buttonAddGroup = (Button) findViewById(R.id.buttonAddGroup);
		buttonAddGroup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogInput mDialogInputGroupName = new DialogInput("添加分组", 0);
				mDialogInputGroupName.show(getFragmentManager(), "addGroup");
			}
		});
		// buttonAddGroup.setVisibility(View.INVISIBLE);
		// 设置导航栏，显示年份
		refreshNavigation();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		// menuinfo该对象提供了选中对象的附加信息

		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);

		int group = ExpandableListView
				.getPackedPositionGroup(info.packedPosition);

		int child = ExpandableListView
				.getPackedPositionChild(info.packedPosition);

		MenuInflater inflater = getMenuInflater();
		if (0 == type) {
			inflater.inflate(R.menu.menu_group, menu);
			menu.setHeaderTitle("请选择");
			try {
				Method m = menu.getClass().getDeclaredMethod(
						"setOptionalIconsVisible", Boolean.TYPE);
				m.setAccessible(true);
				m.invoke(menu, true);
			} catch (Exception e) {
			}
		} else if (1 == type) {
			inflater.inflate(R.menu.menu_list, menu);
			menu.setHeaderTitle("请选择");
			try {
				Method m = menu.getClass().getDeclaredMethod(
						"setOptionalIconsVisible", Boolean.TYPE);
				m.setAccessible(true);
				m.invoke(menu, true);
			} catch (Exception e) {
			}
		}

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item
				.getMenuInfo();
		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);// 0->group，1->child
		int groupPosition = ExpandableListView
				.getPackedPositionGroup(info.packedPosition);
		int childPosition = ExpandableListView
				.getPackedPositionChild(info.packedPosition);
		String groupDir = this.getExternalFilesDir(null).toString() + "/"
				+ yearString + "/" + mGroupName.get(groupPosition) + "-group";
		ListItem item2 = null;
		File flist = null;
		if (1 == type) {

		}
		File fdir = new File(groupDir); // XXX-group

		String[] fStrings = fdir.list();
		selectedgroupPosition = groupPosition;
		selectedchildPosition = childPosition;
		switch (item.getItemId()) {
		// 分组长按事件
		case R.id.delete_group:
			if (fdir.exists() && fdir.isDirectory()) {
				if (0 == fStrings.length) {
					if (true == fdir.delete()) {
						refreshData();
						Toast.makeText(getApplicationContext(), "分组删除成功",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "分组删除失败",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					BasicAlertDialog alertMuchFileExist = new BasicAlertDialog(
							"分组里还有账单存在，不能删除分组！");
					alertMuchFileExist.show(getFragmentManager(),
							"alerMuchFileExist");
				}
			}
			return true;
		case R.id.rename_group:
			DialogInput dialogRenameGroupName = new DialogInput("重命名分组", 2);
			dialogRenameGroupName.show(getFragmentManager(), "renameGroup");
			return true;
		case R.id.add_list:

			DialogInput dialogInputListName = new DialogInput("添加账单", 1);
			dialogInputListName.show(getFragmentManager(), "addList");
			return true;
			// 账单长按事件
		case R.id.rename_list:
			DialogInput dialogRenameListName = new DialogInput("重命名账单", 3);
			dialogRenameListName.show(getFragmentManager(), "renameList");
			return true;
		case R.id.edit_list:
			item2 = mData.get(groupPosition).get(childPosition);
			flist = new File(groupDir + "/" + item2.getName() + "-"
					+ item2.getSumWeight() + "-" + item2.getTime());
			Intent intent = new Intent(MainActivity.this,
					ListEditActivity.class);
			intent.putExtra("groupDir", groupDir);
			intent.putExtra("groupName", mGroupName.get(groupPosition));
			intent.putExtra("listName", item2.getName());
			intent.putExtra("listWeight", item2.getSumWeight());
			intent.putExtra("listTime", item2.getTime());
			startActivity(intent);
			return true;
		case R.id.delete_list:
			item2 = mData.get(groupPosition).get(childPosition);
			flist = new File(groupDir + "/" + item2.getName() + "-"
					+ item2.getSumWeight() + "-" + item2.getTime());
			if (flist.exists() && flist.isFile()) {
				if (true == flist.delete()) {
					refreshData();
					Toast.makeText(getApplicationContext(), "账单删除成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "账单删除失败",
							Toast.LENGTH_SHORT).show();
				}
			}
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		ListItem itemEdit = mData.get(groupPosition).get(childPosition);
		String groupDir1 = this.getExternalFilesDir(null).toString() + "/"
				+ yearString + "/" + mGroupName.get(groupPosition) + "-group";
		Intent intent = new Intent(MainActivity.this, ListEditActivity.class);
		intent.putExtra("groupDir", groupDir1);
		intent.putExtra("groupName", mGroupName.get(groupPosition));
		intent.putExtra("listName", itemEdit.getName());
		intent.putExtra("listWeight", itemEdit.getSumWeight());
		intent.putExtra("listTime", itemEdit.getTime());
		startActivity(intent);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	};

	private void refreshData() {
		int tempWeight = 0;
		mData.clear();
		mGroupName.clear();
		mGroupWeight.clear();

		File fdir = new File(yearDir);
		if (false == fdir.exists()) {
			fdir.mkdirs();
		}
		if (fdir.isDirectory()) { // 判断是否是文件夹
			String s[] = fdir.list();
			if (s.length > 0) { // 是否有账单文件夹存在
				for (int i = 0; i < s.length; i++) {
					File fg = new File(yearDir + "/" + s[i]);
					List<ListItem> list = new ArrayList<ListItem>();
					if (fg.isDirectory()) {
						String[] temp1;
						temp1 = fg.getName().split("-");
						if (2 == temp1.length && null != temp1[0]
								&& null != temp1[1] && temp1[1].equals("group")) { // 判断文件夹名称是否为
																					// XXX-XXX的形式
							mGroupName.add(temp1[0]);
							tempWeight = 0;
							// GroupNum++; // 分组数加一
							String[] fileList = fg.list();
							if (fileList.length > 0) {

								for (int j = 0; j < fileList.length; j++) {
									File fileAccount = new File(yearDir + "/"
											+ s[i] + "/" + fileList[j]);
									if (fileAccount.isFile()) {
										String[] temp2;
										temp2 = fileAccount.getName()
												.split("-");
										if (3 == temp2.length
												&& null != temp2[0]
												&& null != temp2[1]
												&& null != temp2[2]) {

											ListItem item = new ListItem(
													temp2[0], temp2[1],
													temp2[2]);
											list.add(item);
											tempWeight += Integer
													.parseInt(temp2[1]);
										}
									}
								}
							}
							mGroupWeight.add(tempWeight + "");
							mData.add(list); // 添加新分组
						}
					}
				}

			} else {
				Toast.makeText(getApplicationContext(), yearString + "年没有账单哦！",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "未找到文件目录！",
					Toast.LENGTH_SHORT).show();
		}
		// 刷新标题
		mAdapter.notifyDataSetChanged();
		String temp = yearString + "年卷心菜账单";
		if (mGroupName.isEmpty()) {
			temp += "（空）";
		}
		textViewTitle.setText(temp);
	}

	public void refreshNavigation() {
		File file = new File(getExternalFilesDir(null).toString());
		String[] yearArraytemp = file.list();
		String yearsort;
		Arrays.sort(yearArraytemp);
		// 升序排列
		if (yearArraytemp != null) {
			for (int i = 0; i < yearArraytemp.length / 2; i++) {
				yearsort = yearArraytemp[i];
				yearArraytemp[i] = yearArraytemp[yearArraytemp.length - 1 - i];// 降序排列
				yearArraytemp[yearArraytemp.length - 1 - i] = yearsort;
			}
		}
		yearArray.clear();
		for (int i = 0; i < yearArraytemp.length; i++) {
			File filetempFile = new File(getExternalFilesDir(null).toString()
					+ "/" + yearArraytemp[i]);
			if (filetempFile.isDirectory() && filetempFile.exists()) {
				if (yearArraytemp[i].matches("^[0-9]{4}$")
						&& Integer.parseInt(yearArraytemp[i]) >= 2000 // 21世纪以后可用
						&& Integer.parseInt(yearArraytemp[i]) <= nowCalendar// 小于今年才可以加入
								.get(Calendar.YEAR)) {
					yearArray.add(yearArraytemp[i]);
				}
			}
		}

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		mSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.navigation,
				yearArray);
		mSpinnerAdapter.setDropDownViewResource(R.layout.list_year_checked);
		actionBar.setListNavigationCallbacks(mSpinnerAdapter,
				mNavigationListener);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 弹出对话框，要求输入分组名字

		int id = item.getItemId();
		if (id == R.id.action_add_group) { // 添加分组
			DialogInput mDialogInputGroupName = new DialogInput("添加分组", 0);
			mDialogInputGroupName.show(getFragmentManager(), "addGroup");
			return true;
		} else if (id == R.id.action_refresh) { // 刷新
			refreshNavigation();
			refreshData();
		}else if(id == R.id.action_soft_info){
			Intent intent =  new Intent(MainActivity.this, SoftInfoActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshData();
	}

	public void onStringEntered(String string, int type) {
		if (-1 != string.indexOf("-") || -1 != string.indexOf("+")
				|| -1 != string.indexOf("/") || -1 != string.indexOf("*")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"名字中不能含有/*+-等符号");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
		} else {
			if (0 == type) {// 添加分组
				creatDir(string);
			} else if (1 == type) {
				createList(string);
			} else if (2 == type) {
				renameDir(string);
			} else if (3 == type) {
				renameList(string);
			}
		}
	}

	private void creatDir(String dirName) {
		File dir = new File(this.getExternalFilesDir(null).toString() + "/"
				+ yearString + "/" + dirName + "-group");
		// 现在创建目录
		if (dir.exists()) { // 先检查文件夹是否存在
			BasicAlertDialog alertRepeatedGroup = new BasicAlertDialog(
					"分组已经存在，请重新输入！");
			alertRepeatedGroup.show(getFragmentManager(), "repeatedGroup");
		} else {
			try {
				if (true == dir.mkdir()) {
					List<ListItem> list = new ArrayList<ListItem>();
					mGroupName.add(dirName);
					mGroupWeight.add(0 + "");
					// mGroupName[GroupNum] = dir.getName();
					// mGroupWeight[GroupNum] = "0";
					// GroupNum++; // 分组数加一
					mData.add(list);
					mAdapter.setData(mData, mGroupName, mGroupWeight);
					Toast.makeText(getApplicationContext(), "创建分组成功",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("dir", "can't creat direction");
			}
		}
	}

	private void createList(String listName) {
		if (checkFileExist(selectedgroupPosition, listName)) { // 先检查文件夹是否存在
			BasicAlertDialog alertRepeatedGroup = new BasicAlertDialog(
					"账单已经存在，请重新输入！");
			alertRepeatedGroup.show(getFragmentManager(), "repeatedList");
		} else {
			try {
				String nowTimeString = nowCalendar.get(Calendar.YEAR) + "."
						+ (nowCalendar.get(Calendar.MONTH) + 1) + "."
						+ nowCalendar.get(Calendar.DAY_OF_MONTH);
				File flist = new File(this.getExternalFilesDir(null).toString()
						+ "/" + yearString + "/"
						+ mGroupName.get(selectedgroupPosition) + "-group"
						+ "/" + listName + "-" + "0" + "-" + nowTimeString);
				if (true == flist.createNewFile()) {
					refreshData();
					Toast.makeText(getApplicationContext(), "创建账单成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "创建账单失败",
							Toast.LENGTH_SHORT).show();
				}
				// Item item = new Item(name, "0", nowTimeString);
				// mData.get(selectedgroupPosition).add(item);
				// mAdapter.setData(mData, mGroupName, mGroupWeight);

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("dir", "can't creat direction");
			}
		}
	}

	private void renameList(String name) {
		ListItem frontItem = mData.get(selectedgroupPosition).get(
				selectedchildPosition);
		File frontFlist = new File(this.getExternalFilesDir(null).toString()
				+ "/" + yearString + "/"
				+ mGroupName.get(selectedgroupPosition) + "-group" + "/"
				+ frontItem.getName() + "-" + frontItem.getSumWeight() + "-"
				+ frontItem.getTime());
		File renamedFlist = new File(this.getExternalFilesDir(null).toString()
				+ "/" + yearString + "/"
				+ mGroupName.get(selectedgroupPosition) + "-group" + "/" + name
				+ "-" + frontItem.getSumWeight() + "-" + frontItem.getTime());
		if (checkFileExist(selectedgroupPosition, name)) {
			BasicAlertDialog fileExistAlertDialog = new BasicAlertDialog(
					"该账单已经存在！");
			fileExistAlertDialog.show(getFragmentManager(), "fielExist");
		} else {
			if (frontFlist.exists() && frontFlist.isFile()) {
				if (frontFlist.renameTo(renamedFlist)) {
					refreshData();
					Toast.makeText(getApplicationContext(), "账单重命名成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "账单重命名失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private void renameDir(String name) {
		File frontDir = new File(this.getExternalFilesDir(null).toString()
				+ "/" + yearString + "/"
				+ mGroupName.get(selectedgroupPosition) + "-group");
		File renamedDir = new File(this.getExternalFilesDir(null).toString()
				+ "/" + yearString + "/" + name + "-group");
		if (false == renamedDir.exists()) {
			if (frontDir.exists() && frontDir.isDirectory()) {

				if (frontDir.renameTo(renamedDir)) {
					refreshData();
					Toast.makeText(getApplicationContext(), "分组重命名成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "分组重命名失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			BasicAlertDialog fileExistAlertDialog = new BasicAlertDialog(
					"该分组已经存在！");
			fileExistAlertDialog.show(getFragmentManager(), "fielExist");
		}
	}

	private boolean checkFileExist(int groupPosition, String list) {
		for (int i = 0; i < mData.get(groupPosition).size(); i++) {
			if (list.equals(mData.get(groupPosition).get(i).getName())) {
				return true;
			}
		}
		return false;
	}

	private class NavigationListsener implements ActionBar.OnNavigationListener {
		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			yearString = yearArray.get(itemPosition);
			yearDir = MainActivity.this.getExternalFilesDir(null).toString()
					+ "/" + yearString;
			 //Toast.makeText(getApplicationContext(), itemPosition+ "-" +
			// yearString, Toast.LENGTH_SHORT).show();
			refreshData();
			return false;
		}
	}
}
