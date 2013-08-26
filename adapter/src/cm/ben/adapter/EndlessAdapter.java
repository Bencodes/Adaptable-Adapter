/*
 * Copyright 2013 Benjamin Lee
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package cm.ben.adapter;

import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class EndlessAdapter<T> extends Adapter<T> {

	private boolean mKeepAppending;

	private View mPendingView;

	private Task mTask;

	private EndlessAdapterListener mEndlessAdapter;

	/**
	 * An interface used to pull {@link #getView(int, android.view.View, android.view.ViewGroup)} out of an adapter class and
	 * to asynchronously load new data into a ListView.
	 */
	public static interface EndlessAdapterListener<Result> extends AdapterListener {

		/**
		 * Performs an operation on a background thread and then passes the results into {@link #onPostExecute(Object)} for handling.
		 *
		 * @return The results found during the operation.
		 */
		public Result doInBackground ();

		/**
		 * Handles the results found by {@link #doInBackground()} on the UI thread. {@link #notifyDataSetChanged()} is automatically called, use
		 * {@link #setNotifyOnChange(boolean)} to disable/enable this.
		 *
		 * @param result The returned results from {@link #doInBackground()}
		 * @return True if no other data is to be loaded, false otherwise.
		 */
		public boolean onPostExecute (Result result);

		/**
		 * Get a pending view that is displayed when the ListView has reached it's end.
		 *
		 * @param parent The parent that this view will be attached to.
		 * @return The view to be used as the pending view.
		 */
		public View getPendingView (ViewGroup parent);

	}


	/**
	 * Constructor
	 */
	public EndlessAdapter () {
		// Empty
		init(null);
	}


	/**
	 * Constructor
	 *
	 * @param adapterListener The initial EndlessAdapterListener to be used
	 */
	public EndlessAdapter (EndlessAdapterListener adapterListener) {
		super(adapterListener);
		init(adapterListener);
	}


	/**
	 * Constructor
	 *
	 * @param data The data to be used in the ListView
	 */
	public EndlessAdapter (List<T> data) {
		super(data);
		init(null);
	}


	/**
	 * Constructor
	 *
	 * @param data            The data to be used in the ListView
	 * @param adapterListener An instance of EndlessAdapterListener that will be used for building list rows
	 */
	public EndlessAdapter (List<T> data, EndlessAdapterListener adapterListener) {
		super(data, adapterListener);
		init(adapterListener);
	}


	/**
	 * Constructor
	 *
	 * @param data The data to be used in the ListView
	 */
	public EndlessAdapter (T[] data) {
		super(data);
		init(null);
	}


	/**
	 * Constructor
	 *
	 * @param data            The data to be used in the ListView
	 * @param adapterListener An instance of EndlessAdapterListener that will be used for building list rows
	 */
	public EndlessAdapter (T[] data, EndlessAdapterListener adapterListener) {
		super(data, adapterListener);
		init(adapterListener);
	}


	private void init (EndlessAdapterListener adapterListener) {
		mEndlessAdapter = adapterListener;
		mKeepAppending = (mEndlessAdapter = adapterListener) != null;
	}


	private static class Task extends AsyncTask<EndlessAdapter, Void, Object> {

		private EndlessAdapter mAdapter;
		private EndlessAdapterListener mListener;

		@Override
		protected Object doInBackground (EndlessAdapter... params) {
			mAdapter = params[0];
			if (mAdapter != null) {
				mListener = mAdapter.mEndlessAdapter;
				if (mListener != null) {
					// Do some work
					return mListener.doInBackground();
				}
			}
			return null;
		}

		@Override
		public void onPostExecute (Object result) {
			mAdapter.mKeepAppending = mListener.onPostExecute(result);
			mAdapter.mPendingView = null;
			mAdapter.mTask = null;
			mAdapter.notifyDataSetChanged();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public EndlessAdapterListener getAdapterListener () {
		return mEndlessAdapter;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdapterListener (AdapterListener adapterListener) {
		if (!(adapterListener instanceof EndlessAdapterListener)) {
			throw new IllegalArgumentException("Param must be instance of EndlessAdapterListener");
		} else {
			super.setAdapterListener(adapterListener);
			mKeepAppending = (mEndlessAdapter = (EndlessAdapterListener) adapterListener) != null;
		}
	}


	/**
	 * Sets whether or not the pending view will show when you reach the end.
	 *
	 * @param keepAppending If true, the ListView will keep appending data, false otherwise.
	 */
	public void setKeepAppending (boolean keepAppending) {
		mKeepAppending = keepAppending;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount () {
		return (mKeepAppending) ? super.getCount() + 1 : super.getCount();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemViewType (int position) {
		return (position == super.getCount()) ? IGNORE_ITEM_VIEW_TYPE : super.getItemViewType(position);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getViewTypeCount () {
		return (super.getViewTypeCount() + 1);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getItem (int position) {
		return (position >= super.getCount()) ? null : super.getItem(position);

	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean areAllItemsEnabled () {
		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled (int position) {
		return (position >= super.getCount()) ? false : super.isEnabled(position);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		if (position == super.getCount() && mKeepAppending) {
			if (mPendingView == null) {
				if (mEndlessAdapter == null)
					throw new IllegalStateException("No AdapterListener has been provided!");

				mPendingView = mEndlessAdapter.getPendingView(parent);

				if (mTask != null)
					mTask.cancel(true);
				mTask = new Task();
				mTask.execute(this);
			}

			return mPendingView;
		} else {
			return super.getView(position, convertView, parent);
		}
	}

}
