package com.example.acountformom;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class PeopleItem {
	
	public String peopleName; //����
	public String peopleWeight; //��������
	public String peopleWorkNum; //����
	public String peopleSumWeight; //���˱��ľ���
	public List<String> peopleDetail; //��ϸ��ë��
	public PeopleItem(String peopleName,
			String peopleWeight,String peopleWorkNum,
			String peopleSumWeight, List<String> peopleDetail) {
		this.peopleName = peopleName;
		this.peopleWeight = peopleWeight;
		this.peopleWorkNum = peopleWorkNum;
		this.peopleDetail = peopleDetail;
		this.peopleSumWeight = peopleSumWeight;
	}
}
