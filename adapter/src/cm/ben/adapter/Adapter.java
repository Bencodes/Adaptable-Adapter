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

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.*;

public class Adapter<T> extends BaseAdapter implements Filterable {

	private List<T> mObjects;

	private final Object mLock = new Object();

	private boolean mNotifyOnChange = true;

	private ArrayList<T> mOriginalValues;

	private ArrayFilter mFilter;

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
		public View getView (int position, View convertView, ViewGroup parent);

	}


	/**
	 * Constructor
	 */
	public Adapter () {
		// Empty
		init(null, null);
	}


	/**
	 * Constructor
	 *
	 * @param adapterListener The initial AdapterListener to be used
	 */
	public Adapter (AdapterListener adapterListener) {
		init(null, adapterListener);
	}


	/**
	 * Constructor
	 *
	 * @param data The data to be used in the ListView
	 */
	public Adapter (List<T> data) {
		init(data, null);
	}


	/**
	 * Constructor
	 *
	 * @param data            The data to be used in the ListView
	 * @param adapterListener An instance of AdapterListener that will be used for building list rows
	 */
	public Adapter (List<T> data, AdapterListener adapterListener) {
		init(data, adapterListener);
	}


	/**
	 * Constructor
	 *
	 * @param data The data to be used in the ListView
	 */
	public Adapter (T[] data) {
		init((data != null ? Arrays.asList(data) : null), null);
	}


	/**
	 * Constructor
	 *
	 * @param data            The data to be used in the ListView
	 * @param adapterListener An instance of AdapterListener that will be used for building list rows
	 */
	public Adapter (T[] data, AdapterListener adapterListener) {
		init((data != null ? Arrays.asList(data) : null), adapterListener);
	}


	private void init (List<T> data, AdapterListener adapterListener) {
		mListener = adapterListener;
		mObjects = new ArrayList<T>();
		if (data != null) {
			mObjects.addAll(data);
		}
	}


	/**
	 * Returns the current AdapterListener being used for building list rows
	 *
	 * @return The AdapterListener associated with this adapter
	 */
	public AdapterListener getAdapterListener () {
		return mListener;
	}


	/**
	 * Sets the AdapterListener to be used for building list rows
	 *
	 * @param adapterListener The AdapterListener associated with this adapter
	 */
	public void setAdapterListener (AdapterListener adapterListener) {
		mListener = adapterListener;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		if (mListener == null)
			throw new IllegalStateException("No AdapterListener has been provided!");
		return mListener.getView(position, convertView, parent);
	}


	/**
	 * Adds the specified object at the end of the array.
	 *
	 * @param object The object to add at the end of the array.
	 */
	public void add (T object) {
		synchronized (mLock) {
			(mOriginalValues != null ? mOriginalValues : mObjects).add(object);
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}


	/**
	 * Returns the current object list being used.
	 *
	 * @return The list of objects associated with this adapter
	 */
	public List<T> getObjects () {
		return mOriginalValues != null ? mOriginalValues : mObjects;
	}


	/**
	 * Adds the specified Collection at the end of the array.
	 *
	 * @param collection The Collection to add at the end of the array.
	 */
	public void addAll (Collection<? extends T> collection) {
		synchronized (mLock) {
			(mOriginalValues != null ? mOriginalValues : mObjects).addAll(collection);
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}


	/**
	 * Adds the specified items at the end of the array.
	 *
	 * @param items The items to add at the end of the array.
	 */
	public void addAll (T... items) {
		addAll(Arrays.asList(items));
	}


	/**
	 * Inserts the specified object at the specified index in the array.
	 *
	 * @param object The object to insert into the array.
	 * @param index  The index at which the object must be inserted.
	 */
	public void insert (T object, int index) {
		synchronized (mLock) {
			(mOriginalValues != null ? mOriginalValues : mObjects).add(index, object);
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}


	/**
	 * Removes the specified object from the array.
	 *
	 * @param object The object to remove.
	 */
	public void remove (T object) {
		synchronized (mLock) {
			(mOriginalValues != null ? mOriginalValues : mObjects).remove(object);
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}


	/**
	 * Remove all elements from the list.
	 */
	public void clear () {
		synchronized (mLock) {
			(mOriginalValues != null ? mOriginalValues : mObjects).clear();
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}


	/**
	 * Sorts the content of this adapter using the specified comparator.
	 *
	 * @param comparator The comparator used to sort the objects contained
	 *                   in this adapter.
	 */
	public void sort (Comparator<? super T> comparator) {
		synchronized (mLock) {
			Collections.sort(mOriginalValues != null ? mOriginalValues : mObjects, comparator);
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDataSetChanged () {
		super.notifyDataSetChanged();
		mNotifyOnChange = true;
	}


	/**
	 * Control whether methods that change the list ({@link #add},
	 * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
	 * {@link #notifyDataSetChanged}.  If set to false, caller must
	 * manually call notifyDataSetChanged() to have the changes
	 * reflected in the attached view.
	 * <p/>
	 * The default is true, and calling notifyDataSetChanged()
	 * resets the flag to true.
	 *
	 * @param notifyOnChange if true, modifications to the list will
	 *                       automatically call {@link
	 *                       #notifyDataSetChanged}
	 */
	public void setNotifyOnChange (boolean notifyOnChange) {
		mNotifyOnChange = notifyOnChange;
	}


	/**
	 * {@inheritDoc}
	 */
	public int getCount () {
		return mObjects.size();
	}


	/**
	 * {@inheritDoc}
	 */
	public T getItem (int position) {
		return mObjects.get(position);
	}


	/**
	 * Returns the position of the specified item in the array.
	 *
	 * @param item The item to retrieve the position of.
	 * @return The position of the specified item.
	 */
	public int getPosition (T item) {
		return mObjects.indexOf(item);
	}


	/**
	 * {@inheritDoc}
	 */
	public long getItemId (int position) {
		return position;
	}


	/**
	 * {@inheritDoc}
	 */
	public Filter getFilter () {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}


	/**
	 * <p>An array filter constrains the content of the array adapter with
	 * a prefix. Each item that does not start with the supplied prefix
	 * is removed from the list.</p>
	 */
	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering (CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<T>(mObjects);
				}
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<T> list;
				synchronized (mLock) {
					list = new ArrayList<T>(mOriginalValues);
				}
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<T> values;
				synchronized (mLock) {
					values = new ArrayList<T>(mOriginalValues);
				}

				final int count = values.size();
				final ArrayList<T> newValues = new ArrayList<T>();

				for (int i = 0; i < count; i++) {
					final T value = values.get(i);
					final String valueText = value.toString().toLowerCase();

					// First match against the whole, non-splitted value
					if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;

						// Start at index 0, in case valueText starts with space(s)
						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults (CharSequence constraint, Filter.FilterResults results) {
			//noinspection unchecked
			mObjects = (List<T>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

}
