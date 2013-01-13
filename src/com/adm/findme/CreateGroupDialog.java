package com.adm.findme;

import java.util.ArrayList;
import java.util.List;

import com.adm.findme.ShareDialog.ShareDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class CreateGroupDialog extends DialogFragment {

	private GroupDAO groupDAO;
	private ContactDAO contactDAO;
	ArrayList mSelectedItems;

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		builder.setTitle(R.string.new_group_name);
		builder.setView(inflater.inflate(R.layout.dialog_creategroup, null));
		builder.setPositiveButton(R.string.new_group_create, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});

		return builder.create();
		
		/*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());		 
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v = inflater.inflate(R.layout.dialog_creategroup, null);
	    final View et = (EditText)v.findViewById(R.id.group_name);
	    
	    builder.setView(v);
	    builder.setTitle(R.string.new_group);
	    builder.setPositiveButton(R.string.new_group_create, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   Contact c = new Contact();
	               }
	           });
	    builder.setNegativeButton(R.string.new_group_cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	               }
	           });      
	    return builder.create();*/
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
