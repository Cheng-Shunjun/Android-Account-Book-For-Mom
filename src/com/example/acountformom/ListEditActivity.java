package com.example.acountformom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListEditActivity extends ActionBarActivity implements
		DialogInput.OnStringEnteredListener,
		DialogAddPeople.OnPeopleEnteredListener,
		DialogAddWeight.OnWeightEnteredListener,
		DialogEditPeople.OnPeopleEditedListener {
	private String groupDir;
	private String groupName;
	private String listName;
	private String listWeight;
	private String listTime;
	private String fileNameString;
	private Calendar nowCalendar;
	private String yearString;
	private TextView textViewListTitle; // ����

	private TextView textViewPrice; // ����
	private TextView textViewSubWeight; // ����
	private TextView textViewWeightBefore; // ����
	private TextView textViewWeightAfter; // ����
	private TextView textViewMoney; // �ܼ�Ǯ

	private ListView listViewAccount; // ���
	private File fileList;
	private List<PeopleItem> peopleItemList;
	private ListViewAdapter mListViewAdapter;
	private String price = "0.00";
	private String money = "0.00";
	private String sumWeightBefore = "0";
	private String sumWeightAfter = "0";
	private String weightSub = "0";
	private String numOfPeople = "0";
	private int selectedPostion = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_edit);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setDisplayUseLogoEnabled(false);
		Intent intent = getIntent();
		groupDir = intent.getStringExtra("groupDir");
		groupName = intent.getStringExtra("groupName");
		listName = intent.getStringExtra("listName");
		listWeight = intent.getStringExtra("listWeight");
		listTime = intent.getStringExtra("listTime");
		peopleItemList = new ArrayList<PeopleItem>();
		getActionBar().setTitle("����");
		textViewListTitle = (TextView) findViewById(R.id.textview_list_title);
		listViewAccount = (ListView) findViewById(R.id.listview_account);
		textViewPrice = (TextView) findViewById(R.id.textview_price);
		textViewSubWeight = (TextView) findViewById(R.id.textview_sub);
		textViewWeightBefore = (TextView) findViewById(R.id.textview_sum_weight_before);
		textViewWeightAfter = (TextView) findViewById(R.id.textview_sum_weight_after);
		textViewMoney = (TextView) findViewById(R.id.textview_money);

		// textViewListTitle.getBackground().setAlpha(50);
		// textViewPrice.getBackground().setAlpha(100);
		// textViewSubWeight.getBackground().setAlpha(100);
		// textViewWeightBefore.getBackground().setAlpha(100);
		// textViewWeightAfter.getBackground().setAlpha(100);

		fileNameString = groupDir + "/" + listName + "-" + listWeight + "-"
				+ listTime;
		fileList = new File(fileNameString);
		registerForContextMenu(listViewAccount);
		textViewPrice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogInput dialogInputPrice = new DialogInput("�����뵥��", 4);
				dialogInputPrice.show(getFragmentManager(), "price");
			}
		});
		textViewSubWeight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogInput dialogInputPrice = new DialogInput("��������ӽ���", 5);
				dialogInputPrice.show(getFragmentManager(), "price");
			}
		});

		listViewAccount.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				selectedPostion = position;
				DialogAddWeight dialogInputNewData = new DialogAddWeight(
						peopleItemList.get(position).peopleName + "��һ��");
				dialogInputNewData.show(getFragmentManager(), "addWeight");

			}
		});
		readData();
		refreshData();
		// textViewListTitle.getBackground().setAlpha(50);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_people, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// Toast.makeText(getApplicationContext(), "content"+item.getItemId()+
		// " - " + info.position, Toast.LENGTH_LONG).show();
		selectedPostion = info.position;
		switch (item.getItemId()) {
		case R.id.edit_people:
			String tempString = "";
			for (int i = 0; i < peopleItemList.get(info.position).peopleDetail
					.size(); i++) {
				tempString += peopleItemList.get(info.position).peopleDetail
						.get(i);
				tempString += " ";
			}
			DialogEditPeople dialogEditPeople = new DialogEditPeople("�༭"
					+ peopleItemList.get(info.position).peopleName,
					peopleItemList.get(info.position).peopleName,
					peopleItemList.get(info.position).peopleWeight, tempString);
			dialogEditPeople.show(getFragmentManager(), "edit people");
			return true;
		case R.id.delete_people:
			peopleItemList.remove(info.position);
			refreshData();
			WriteData();
		default:
			return super.onContextItemSelected(item);
		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		WriteData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		WriteData();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		WriteData();
	}

	public void readData() {
		try {
			FileReader fr = new FileReader(fileNameString);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String[] tempStrings;
			// List<PeopleItem> tempPeopleItems = new ArrayList<PeopleItem>();
			peopleItemList.clear();
			if (null == (line = br.readLine())) {
				Toast.makeText(getApplicationContext(), "�˵�Ϊ��",
						Toast.LENGTH_SHORT).show();
				br.close();
				fr.close();
				return;
			}
			tempStrings = line.trim().split("\\s");
			if (6 == tempStrings.length) {
				sumWeightBefore = tempStrings[0];
				weightSub = tempStrings[1];
				sumWeightAfter = tempStrings[2];
				price = tempStrings[3];
				money = tempStrings[4];
				numOfPeople = tempStrings[5];
			} else {
				alertFileCodeWrong();
				br.close();
				fr.close();
				return;
			}
			for (int i = 0; i < Integer.parseInt(numOfPeople); i++) {
				String[] tempPeople;
				List<String> tempList = new ArrayList<String>();
				if (null == (line = br.readLine())) {
					alertFileCodeWrong();
					br.close();
					fr.close();
					return;
				}
				tempPeople = line.trim().split("\\s");
				if (tempPeople.length < 4) {
					alertFileCodeWrong();
					br.close();
					fr.close();
					return;
				}
				if (Integer.parseInt(tempPeople[2]) + 4 != tempPeople.length) {
					alertFileCodeWrong(); // ÿһ�е����ݸ�������
					br.close();
					fr.close();
					return;
				}
				for (int j = 0; j < Integer.parseInt(tempPeople[2]); j++) {
					tempList.add(tempPeople[j + 4]);
				}
				PeopleItem tempPepleItem = new PeopleItem(tempPeople[0],
						tempPeople[1], tempPeople[2], tempPeople[3], tempList);
				peopleItemList.add(tempPepleItem);
			}
			mListViewAdapter = new ListViewAdapter(this, peopleItemList);
			br.close();
			fr.close();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("list", "read list errer");
		}
	}

	public void refreshData() {
		int dataWeightBefore = 0;
		int dataWeightAfter = 0;
		double dataMoney = 0;
		int[] dataPeopleSumweight = new int[peopleItemList.size()];// ���������˵ĺ�
		try {

			for (int i = 0; i < peopleItemList.size(); i++) {
				dataPeopleSumweight[i] = 0;
				peopleItemList.get(i).peopleWorkNum = peopleItemList.get(i).peopleDetail
						.size() + "";

				for (int j = 0; j < peopleItemList.get(i).peopleDetail.size(); j++) { // ���������
					dataPeopleSumweight[i] += Integer.parseInt(peopleItemList
							.get(i).peopleDetail.get(j))
							- Integer
									.parseInt(peopleItemList.get(i).peopleWeight);
				}

				peopleItemList.get(i).peopleSumWeight = dataPeopleSumweight[i]
						+ "";
				dataWeightBefore += dataPeopleSumweight[i];// ���أ�û���ƣ�
			}
			dataWeightAfter = dataWeightBefore - Integer.parseInt(weightSub);// ����֮�������
			dataMoney = dataWeightAfter * Double.parseDouble(price);
			BigDecimal doubleTemp = new BigDecimal(dataMoney);
			dataMoney = doubleTemp.setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("error", "data error while caculating");
			Toast.makeText(getApplicationContext(), "�����쳣", Toast.LENGTH_SHORT)
					.show();
		}
		if (dataWeightAfter < 0) {
			dataWeightAfter = 0;
		}
		if (dataWeightBefore < 0) {
			dataWeightBefore = 0;
		}
		if (dataMoney < 0) {
			dataMoney = 0;
		}
		// ת��Ϊ�ַ���
		money = dataMoney + "";
		numOfPeople = peopleItemList.size() + "";

		sumWeightAfter = dataWeightAfter + "";
		sumWeightBefore = dataWeightBefore + "";

		textViewListTitle.setText(groupName + "-" + listName + "("
				+ numOfPeople + "��)");
		textViewPrice.setText("���ۣ�" + price + "Ԫ/��");
		textViewSubWeight.setText("���ƣ�" + weightSub + "��");
		textViewWeightBefore.setText("ë�أ�" + sumWeightBefore + "��");
		textViewWeightAfter.setText("���أ�" + sumWeightAfter + "��");
		textViewMoney.setText(money + "Ԫ");
		mListViewAdapter = new ListViewAdapter(this, peopleItemList);
		listViewAccount.setAdapter(mListViewAdapter);
	}

	public boolean WriteData() {
		String fileData = null;
		fileData = sumWeightBefore + " " + weightSub + " " + sumWeightAfter
				+ " " + price + " " + money + " " + numOfPeople + "\r\n";
		for (int i = 0; i < peopleItemList.size(); i++) {
			fileData += (peopleItemList.get(i).peopleName + " "
					+ peopleItemList.get(i).peopleWeight + " "
					+ peopleItemList.get(i).peopleWorkNum + " "
					+ peopleItemList.get(i).peopleSumWeight + " ");
			for (int j = 0; j < peopleItemList.get(i).peopleDetail.size(); j++) {
				fileData += (peopleItemList.get(i).peopleDetail.get(j) + " ");
			}
			fileData += "\r\n";
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(
					fileNameString, false); // ��׷��ģʽ
			outputStream.write(fileData.getBytes());
			outputStream.flush(); // �������
			outputStream.close(); // �ر������
			Log.d("file", "saved file successfully");
		} catch (Exception e) {
			// Toast.makeText(getApplicationContext(), "save file errer",
			// Toast.LENGTH_SHORT).show();
			Log.e("file", "save file error");
		}

		File file = new File(fileNameString);
		fileNameString = groupDir + "/" + listName + "-" + sumWeightAfter + "-"
				+ listTime;
		File newFile = new File(fileNameString);
		if (file.exists() && file.isFile()) {
			if (true == file.renameTo(newFile)) {
				return true;
			} else {
				Toast.makeText(getApplicationContext(), "�ļ������ɹ�",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	public void alertFileCodeWrong() {
		BasicAlertDialog dialogAlertWrongCode = new BasicAlertDialog("�ļ����ݴ���!");
		dialogAlertWrongCode.show(getFragmentManager(), "file code wrong");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		} else if (id == R.id.action_add_people) {
			DialogAddPeople dialogAddPeople = new DialogAddPeople();
			dialogAddPeople.show(getFragmentManager(), "addPeople");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onStringEntered(String string, int type) {
		if (string.equals("")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"���벻��Ϊ��");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			return;
		}
		if (-1 != string.indexOf("-") || -1 != string.indexOf("+")
				|| -1 != string.indexOf("/") || -1 != string.indexOf("*")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"�����в��ܺ���/*+-�ȷ���");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
		} else {
			if (4 == type) { // ��ӵ���
				if (string.matches("^[0-9]+([.][0-9])?[0-9]?$")) {
					price = string;
					refreshData();
					WriteData();
				} else {
					BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
							"�밴��\"0.85\"�ĸ�ʽ����");
					wrongCodeAlertDialog
							.show(getFragmentManager(), "wrongCode");
				}
			} else if (5 == type) { // ��������
				if (string.matches("^0|([1-9][0-9]*)$")) {
					weightSub = string;
					refreshData();
					WriteData();
				} else {
					BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
							"�밴��\"120\"�ĸ�ʽ����");
					wrongCodeAlertDialog
							.show(getFragmentManager(), "wrongCode");
				}
			}
		}
	}

	public void onPeopleEntered(String name, String weight) {
		if (name.equals("")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"���ز���Ϊ��");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			return;
		}
		if (-1 != name.indexOf("-") || -1 != name.indexOf("+")
				|| -1 != name.indexOf("/") || -1 != name.indexOf("*")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"�����в��ܺ���/*+-�ȷ���");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			return;
		}
		if (checkNameExist(name)) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"�����ظ������������룡");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			return;
		}
		if (false == weight.matches("^[0-9]+$")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"��������ȷ����������");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			return;
		} else {
			PeopleItem peopleItem = new PeopleItem(name, weight, "0", "0",
					new ArrayList<String>());
			peopleItemList.add(peopleItem);
			refreshData();
			WriteData();
		}
	}

	public void onWeightEntered(String string) {
		if (string.equals("")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"���ز���Ϊ��");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			return;
		}
		if (Integer.parseInt(peopleItemList.get(selectedPostion).peopleWeight) > Integer
				.parseInt(string)) {
			BasicAlertDialog wrongWeightAlertDialog = new BasicAlertDialog(
					"�����������أ�");
			wrongWeightAlertDialog.show(getFragmentManager(), "wrongWeight");
		} else {
			peopleItemList.get(selectedPostion).peopleDetail.add(string);
			refreshData();
			WriteData();
		}
	}

	public void onPeopleEdited(String name, String weight, String detail) {
		detail = detail.trim();
		String temp[] = null;
		if (name.equals("")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"��������Ϊ��");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			return;
		}
		if (weight.equals("")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"���ز���Ϊ��");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			return;
		}

		if (!detail.equals("")) {
			temp = detail.trim().split("\\s");
			// BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
			// "��ϸ����Ϊ��");
			// wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			// return;
		}
		if (-1 != name.indexOf("-") || -1 != name.indexOf("+")
				|| -1 != name.indexOf("/") || -1 != name.indexOf("*")) {
			BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
					"�����в��ܺ���/*+-�ȷ���");
			wrongCodeAlertDialog.show(getFragmentManager(), "wrongCode");
			return;
		}
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				if (false == temp[i].matches("^[0-9]*$")) {
					BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
							"�������з����ַ��ţ�");
					wrongCodeAlertDialog
							.show(getFragmentManager(), "wrongCode");
					return;
				}
				try {
					if (temp[i].matches("^[0-9]+$")) {
						if (Integer.parseInt(temp[i]) < Integer
								.parseInt(weight)) {
							BasicAlertDialog wrongCodeAlertDialog = new BasicAlertDialog(
									"������������������أ���Ч��");
							wrongCodeAlertDialog.show(getFragmentManager(),
									"wrongCode");
							return;
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.d("error", "data is valid");
					Toast.makeText(getApplicationContext(), "������������",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		peopleItemList.get(selectedPostion).peopleName = name;
		peopleItemList.get(selectedPostion).peopleWeight = weight;
		peopleItemList.get(selectedPostion).peopleDetail.clear();

		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].matches("^[0-9]+$")) {
					peopleItemList.get(selectedPostion).peopleDetail
							.add(temp[i]);
				}
			}
		}
		refreshData();
		WriteData();
	}

	public boolean checkNameExist(String name) {
		for (int i = 0; i < peopleItemList.size(); i++) {
			if (name.equals(peopleItemList.get(i).peopleName)) {
				return true;
			}
		}
		return false;
	}
}
