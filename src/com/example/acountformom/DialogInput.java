package com.example.acountformom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogInput extends DialogFragment {
	public String titleString;
	public int type; // 0->addGroup 1->addList 2->renameGroup 3->renameList

	// 4->price 5->weight for sub

	public DialogInput(String title, int type) {
		this.titleString = title;
		this.type = type;
	}

	public interface OnStringEnteredListener {
		public void onStringEntered(String string, int type);
	}

	OnStringEnteredListener mStringEnteredListener;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mStringEnteredListener = (OnStringEnteredListener) activity;

		} catch (ClassCastException e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString()
					+ "must implements OnNameEnteredListener!");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(inflater.inflate(R.layout.dialog_input_string, null))
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EditText mEditText = (EditText) getDialog()
								.findViewById(R.id.edittext_string);
						mStringEnteredListener.onStringEntered(mEditText
								.getText().toString(), type);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).setTitle(titleString);

		return builder.create();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// 0->addGroup 1->addList 2->renameGroup 3->renameList
		// 4->price 5->weight for sub
		super.onResume();
		EditText mEditText = (EditText) getDialog().findViewById(
				R.id.edittext_string);
		switch (type) {
		case 0:
			mEditText.setHint("请输入分组名");
			mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 1:
			mEditText.setHint("请输入账单名");
			mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 2:
			mEditText.setHint("请输入新分组名");
			mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 3:
			mEditText.setHint("请输入新账单名");
			mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 4:
			mEditText.setHint("请输入单价如:0.85");
			mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 5:
			mEditText.setHint("请输入除称重量如：85");
			mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
			break;
		default:
			break;
		}
		mEditText.setFocusable(true);
		mEditText.requestFocus();
	}
}