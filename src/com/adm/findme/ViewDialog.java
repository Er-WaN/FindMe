package com.adm.findme;

import java.util.ArrayList;
import java.util.List;

import com.adm.findme.ShareDialog.ShareDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Descripcion de Dialogo de Group View
 * 
 * @author Carlos Ruiz
 * **/
public class ViewDialog extends DialogFragment {

	// TODO Cambiar el String del grupo al elelemento para trabajar con el en
	// Main Activity
	/** selectedGroup es un string con el nombre del grupo seleccionado **/
	String selectedGroup = null;

	GroupDAO groupDAO;

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	/**
	 * Interfaz de metodos que debe implementar la ventana principal que usara
	 * para comunicarse con el dialogo
	 * **/
	public interface ViewDialogListener {
		public void onViewDialogGroupSelected(DialogFragment dialog,
				String selectedGroup);
	}

	// Use this instance of the interface to deliver action events
	ViewDialogListener mListener;

	// Override the Fragment.onAttach() method to instantiate the
	// NoticeDialogListener
	/**
	 * Metodo onAttach es llamado por la ventana principal antes de llamar al
	 * dialogo se usa para establecer un listener entre las dos actividades y
	 * poder comunicarse entre ellas
	 * **/
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the ViewDialogListener so we can send events to the
			// host
			mListener = (ViewDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement ViewDialogListener");
		}
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    	    
	    builder.setTitle(R.string.viewTitle);
	    builder.setMultiChoiceItems(getGroupsNameByCursor(), "Group_block", "Group_name", new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
	    builder.setPositiveButton(R.string.show, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});	
	    return builder.create();
	}

	public Cursor getGroupsNameByCursor() {
		groupDAO = new GroupDAO(getActivity().getApplicationContext());
		groupDAO.open();
		Cursor cursor = groupDAO.getAllGroupsNameCursor();
		return cursor;
	}
}
