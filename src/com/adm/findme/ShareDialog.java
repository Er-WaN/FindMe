package com.adm.findme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

/**
 * @author Carlos Ruiz
 * Dialogo de Share que se abre en ventana principal y muestra la interfaz al usuario de compartir localizacion
 * **/
public class ShareDialog extends DialogFragment{

	 /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ShareDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	 // Use this instance of the interface to deliver action events
    ShareDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ShareDialogListener so we can send events to the host
            mListener = (ShareDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ShareDialogListener");
        }
    }
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		return super.onCreateDialog(savedInstanceState);
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 
		// Get the layout inflater
		    LayoutInflater inflater = getActivity().getLayoutInflater();
		   
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.dialog_share, null))
		    		.setTitle(R.string.shareTitle)
		    // Add action buttons
		           .setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                   // TODO que hacer en caso de compartir localizacion
		            	   mListener.onDialogPositiveClick(ShareDialog.this);
		               }
		           })
		           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
//		                   LoginDialogFragment.this.getDialog().cancel();
		            	   mListener.onDialogNegativeClick(ShareDialog.this);
		                   ShareDialog.this.getDialog().cancel();
		               }
		           });      
		    return builder.create();
	}


}
