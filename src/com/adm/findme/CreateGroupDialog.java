package com.adm.findme;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroupDialog extends DialogFragment {

	private GroupDAO groupDAO;
	private ContactDAO contactDAO;

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final EditText input = new EditText(getActivity());
		input.setHint(R.string.new_group_hint);
		AlertDialog.Builder dialogName = new AlertDialog.Builder(getActivity());
		dialogName.setTitle(R.string.new_group);
		dialogName.setView(input);
		dialogName.setPositiveButton(R.string.new_group_next, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String name = input.getText().toString();
				if(name.length() > 0)
				{
					insertGroup(name);
					DialogFragment dialog2 = new CreateGroupDialogContacts();
					dialog2.show(getActivity().getSupportFragmentManager(), "CreateGroupDialogContacts");
				}
				else
				{
					Toast.makeText(getActivity(), R.string.new_group_valide, Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialogName.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});

		return dialogName.create();
		
		
	}
	
	public void insertGroup(String name) {
		groupDAO = new GroupDAO(this.getActivity().getApplicationContext());
		groupDAO.open();
		groupDAO.create(name);
		
	}
	
	public String[] getContactsName() {
		List<String> list_contacts_name;
		String[] array_contacts_name = null;
		contactDAO = new ContactDAO(this.getActivity().getApplicationContext());
		contactDAO.open();	
		list_contacts_name = contactDAO.getAllContactsName();
		for (int i = 0; i < list_contacts_name.size(); i++) {
			array_contacts_name[i] = list_contacts_name.get(i);
		}
		return array_contacts_name;
	}
	
}
