package com.example.acountformom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

public class DialogAddWeight extends DialogFragment {
	public String titleString;
	public int position; //0->addGroup  1->addList  2->renameGroup 3->renameList

	public DialogAddWeight(String title) {
		this.titleString = title;
	}

	public interface OnWeightEnteredListener {
		public void onWeightEntered(String string);
	}

	OnWeightEnteredListener mStringEnteredListener;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mStringEnteredListener = (OnWeightEnteredListener) activity;

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
		builder.setView(inflater.inflate(R.layout.dialog_add_weight, null))
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EditText mEditText = (EditText) getDialog()
								.findViewById(R.id.edittext_add_weight);
						mStringEnteredListener.onWeightEntered(mEditText
								.getText().toString());
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
}