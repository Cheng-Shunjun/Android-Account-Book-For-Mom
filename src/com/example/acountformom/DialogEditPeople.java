package com.example.acountformom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class DialogEditPeople extends DialogFragment {
	private String titleString;
	private String name;
	private String weight;
	private String detail;
	
	public DialogEditPeople(String title, String name, String weight,
			String detail) {
		this.titleString = title;
		this.name = name;
		this.weight = weight;
		this.detail = detail;
	}

	public interface OnPeopleEditedListener {
		public void onPeopleEdited(String name, String weight, String detail);
	}

	OnPeopleEditedListener mOnPeopleEditedListener;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mOnPeopleEditedListener = (OnPeopleEditedListener) activity;

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
		builder.setView(inflater.inflate(R.layout.dialog_edit_people, null));
			
		//mEditTextPeopleName.setText(name);
		//mEditTextPeopleWeight.setText(weight);
		//mEditTextPeopleDetail.setText(detail);	
		
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				EditText mEditTextPeopleName = (EditText) getDialog()
						.findViewById(R.id.edittext_edit_name);
				EditText mEditTextPeopleWeight = (EditText) getDialog()
						.findViewById(R.id.edittext_edit_weight);
				EditText mEditTextPeopleDetail = (EditText) getDialog()
						.findViewById(R.id.edittext_edit_detail);
				mOnPeopleEditedListener
						.onPeopleEdited(mEditTextPeopleName.getText()
								.toString(), mEditTextPeopleWeight.getText()
								.toString(), mEditTextPeopleDetail.getText()
								.toString());
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

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
		super.onResume();
		EditText mEditTextPeopleName = (EditText) getDialog()
				.findViewById(R.id.edittext_edit_name);
		EditText mEditTextPeopleWeight = (EditText) getDialog()
				.findViewById(R.id.edittext_edit_weight);
		EditText mEditTextPeopleDetail = (EditText) getDialog()
				.findViewById(R.id.edittext_edit_detail);
		mEditTextPeopleName.setText(name);
		mEditTextPeopleWeight.setText(weight);
		mEditTextPeopleDetail.setText(detail);
	}
}