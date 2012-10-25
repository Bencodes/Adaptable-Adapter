package com.bencodes.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@inheritDoc}
 */
public class ArrayAdapter<T> extends android.widget.ArrayAdapter<T> {

	/**
	 * Just the local listener, move along!
	 */
	private AdapterListener mListener;

	/**
	 * <p>
	 * An interface used to getView so you can just use the same adapter over
	 * and over again.
	 * </p>
	 */
	public static interface AdapterListener {

		/**
		 * @see <a href=
		 *      "http://developer.android.com/reference/android/widget/Adapter.html#getView(int, android.view.View, android.view.ViewGroup)"
		 *      </a>
		 */
		public View getView(int position, View convertView, ViewGroup parent);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The current context.
	 * @param listener
	 *            A instance of AdapterListener so the getView can call that
	 */
	public ArrayAdapter(Context context, AdapterListener listener) {
		super(context, -1);
		this.mListener = listener;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The current context.
	 * @param data
	 *            The default data for the list.
	 * @param listener
	 *            A instance of AdapterListener so the getView can call that
	 */
	public ArrayAdapter(Context context, List<T> data, AdapterListener listener) {
		super(context, -1, data);
		this.mListener = listener;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The current context.
	 * @param textViewResourceId
	 *            The resource ID for a layout file containing a TextView to use
	 *            when instantiating views.
	 * @param objects
	 *            The objects to represent in the ListView.
	 */
	public ArrayAdapter(Context context, T[] objects, AdapterListener listener) {
		super(context, -1, objects);
		this.mListener = listener;
	}
	
	/**
	 * Sets or updates the GetViewListener
	 * 
	 * @param listener
	 *            A instance of AdapterListener so the getView can call that
	 */
	public void setListener(AdapterListener listener) {
		this.mListener = listener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return (mListener != null) 
				? this.mListener.getView(position, convertView, parent) 
				: super.getView(position, convertView, parent);
	}

}
