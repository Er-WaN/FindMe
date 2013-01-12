package com.adm.findme;

import com.adm.findme.ShareDialog.ShareDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class CreateGroupDialog extends DialogFragment {

	public interface CreateGroupDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	CreateGroupDialogListener mListener;
	
	/*public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ShareDialogListener so we can send events to the host
            mListener = (CreateGroupDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CreateGroupDialogListener");
        }
    }*/

	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		return super.onCreateDialog(savedInstanceState);
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 
		// Get the layout inflater
		    LayoutInflater inflater = getActivity().getLayoutInflater();
		   
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.dialog_creategroup, null))
		    		.setTitle(R.string.new_group)
		    // Add action buttons
		           .setPositiveButton(R.string.new_group_create, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                   // TODO que hacer en caso de compartir localizacion
		            	   mListener.onDialogPositiveClick(CreateGroupDialog.this);
		               }
		           })
		           .setNegativeButton(R.string.new_group_cancel, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
//		                   LoginDialogFragment.this.getDialog().cancel();
		            	   mListener.onDialogNegativeClick(CreateGroupDialog.this);
		            	   CreateGroupDialog.this.getDialog().cancel();
		               }
		           });      
		    return builder.create();
	}
}
