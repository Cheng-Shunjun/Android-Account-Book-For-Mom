package com.example.acountformom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;

public class BasicAlertDialog extends DialogFragment {
	String alertsString;
	public BasicAlertDialog(String alertContent){
		alertsString = alertContent;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(alertsString)
		.setPositiveButton("��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "" + "��", Toast.LENGTH_SHORT).show();;
			}
		})
		.setNegativeButton("ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "" + "��", Toast.LENGTH_SHORT).show();;
			}
		});
		return builder.create();
	}
}