package com.adm.findme;

import java.util.ArrayList;
import java.util.List;

import com.adm.findme.ShareDialog.ShareDialogListener;

import android.util.Log;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;


/**
 * This class create the DialogAlert with a list a all the contact in the Database.
 * User needs to select which contact he wants to add in the new group
 * 
 * @author Erwan Thouvenot
 * **/
public class CreateGroupDialogContacts extends DialogFragment{

	ContactDAO contactDAO;
	GroupDAO groupDAO;
	ContactGroupDAO contactgroupDAO;
	
	
	/*public interface CreateGroupContactDialogListener {
        public void onDialogGroupPositiveClick(DialogFragment dialog, boolean update);
        public void onDialogGroupNegativeClick(DialogFragment dialog);
    }
	
	
	CreateGroupContactDialogListener mListener;
    
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ShareDialogListener so we can send events to the host
            mListener = (CreateGroupContactDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CreateGroupContactDialogListener");
        }
    }*/
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//Create AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    
		// Create List of String with contacts name inside
	    List<String> list = new ArrayList<String>();
	    list = getContactsName();
	    
	    // Create a array of boolean of the same size than list with false inside
	    final boolean[] selected = new boolean[list.size()];

	    // Create a CharSequence[] with name of contacts inside
	    final CharSequence[] options = list.toArray(new CharSequence[list.size()]);
	    
	    builder.setTitle(R.string.new_group);
	    builder.setMultiChoiceItems(options, selected, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
			}
		});
	    
	    builder.setNegativeButton(R.string.new_group_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				deleteLastGroup();
				
			}
		});
	    builder.setPositiveButton(R.string.new_group_create, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// When user clicks on Positive Button, we call the insertContactsInGroup method
				boolean update;
				update = insertContacts(options, selected); 
				//mListener.onDialogGroupPositiveClick(CreateGroupDialogContacts.this, update);
			}
		});	
	    return builder.create();
	}

	/**
	 * Method to get the name of all the contacts in the internal Database
	 * @return contact_list_by_name List of all contacts with their names.
	 */	
	public List<String> getContactsName() {
		List<String> contact_list_by_name = new ArrayList<String>();
		contactDAO = new ContactDAO(getActivity());
		contactDAO.open();
		contact_list_by_name = contactDAO.getAllContactsName();
		contactDAO.close();
		return contact_list_by_name;
	}
	
	/**
	 * Method to get the ID of the last group created in the internal database.
	 * @return lastGroupId The id of the last created group in the internal database.
	 */
	public int getLastGroupId() {
		int lastGroupId;
		groupDAO = new GroupDAO(getActivity());
		groupDAO.open();
		lastGroupId = groupDAO.getLastGroupId();
		groupDAO.close();
		return lastGroupId;
	}
	
	
	/**
	 * Method to get the id of a contact depending of it name
	 * @return group_idthe id of the selected group.
	 * @param name The name of the group we want to know the id.
	 */
	public int getGroupId(String name) {
		groupDAO = new GroupDAO(getActivity());
		int group_id;
		groupDAO.open();
		group_id = groupDAO.getGroupId(name);
		groupDAO.close();
		return group_id;
	}
	
	/**
	 * Method to insert contacts in a group.
	 */
	protected boolean insertContacts(CharSequence[] options, boolean[] selected) {
		contactgroupDAO = new ContactGroupDAO(getActivity());
		contactgroupDAO.open();
		for ( int i = 0; i < options.length; i++) {
			if (selected[i] == true ) {
				int contact_id;
				contact_id = getContactId(""+options[i]);
				contactgroupDAO.insertContactIntoGroup(getLastGroupId(), contact_id);
			}
		}
		contactgroupDAO.close();
		Toast.makeText(getActivity(), R.string.group_created, Toast.LENGTH_SHORT).show();
		return true;
	}
	
	/**
	 * Method to delete the last group in the database.
	 */
	public void deleteLastGroup() {
		groupDAO = new GroupDAO(getActivity());
		groupDAO.deleteLastGroup();
		groupDAO.close();
	}
	
	public int getContactId(String name) {
		int contact_id;
		contactDAO = new ContactDAO(getActivity());
		contactDAO.open();
		contact_id = contactDAO.getIdContact(name);
		contactDAO.close();
		return contact_id;
	}
}
