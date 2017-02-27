package com.example.acountformom;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class PeopleItem {
	
	public String peopleName; //姓名
	public String peopleWeight; //个人体重
	public String peopleWorkNum; //回数
	public String peopleSumWeight; //个人背的净重
	public List<String> peopleDetail; //明细，毛重
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
