package com.adm.findme;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class CreateGroupDialogContacts extends DialogFragment{

	ContactDAO contactDAO;
	GroupDAO groupDAO;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    List<String> list = new ArrayList<String>();
	    list = getContactsName();
	    final CharSequence[] charSequenceItems = list.toArray(new CharSequence[list.size()]);
	    
	    builder.setTitle(R.string.new_group);
	    builder.setMultiChoiceItems(charSequenceItems, null, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    builder.setNegativeButton(R.string.new_group_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
	    builder.setPositiveButton(R.string.new_group_create, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});	
	    return builder.create();
	}

	public List getContactsName() {
		List<String> contact_list_by_name = new ArrayList<String>();
		contactDAO = new ContactDAO(getActivity().getApplicationContext());
		contactDAO.open();
		contact_list_by_name = contactDAO.getAllContactsName();
		return contact_list_by_name;
	}
	
	public int getListGroupId() {
		int LastGroupId;
		groupDAO = new GroupDAO(getActivity().getApplicationContext());
		LastGroupId = groupDAO.mDb.rawQuery("SELECT last_insert_rowid()", null).getInt(0);
		return LastGroupId;
	}
	
}
