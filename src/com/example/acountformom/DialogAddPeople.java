package com.example.acountformom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class DialogAddPeople extends DialogFragment {
	public interface OnPeopleEnteredListener {
		public void onPeopleEntered(String name,String weight);
	}

	OnPeopleEnteredListener mPeopleEnteredListener;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mPeopleEnteredListener = (OnPeopleEnteredListener) activity;

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
		builder.setView(inflater.inflate(R.layout.dialog_add_people, null))
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EditText mEditTextName = (EditText) getDialog()
								.findViewById(R.id.edittextName);
						EditText mEditTextWeight = (EditText) getDialog()
								.findViewById(R.id.edittextweight);
						mPeopleEnteredListener.onPeopleEntered(mEditTextName
								.getText().toString(), mEditTextWeight.getText().toString());
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).setTitle("添加工人");

		return builder.create();
	}
}