package com.example.demo;

import java.util.ArrayList;

import com.bencodes.adapter.ArrayAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			this.addFragment(new DemoListFragment(), DemoListFragment.TAG, true);
		}
	}

	public void addFragment(Fragment fragment, String tag, boolean clearStack) {
		final FragmentManager fm = super.getSupportFragmentManager();
		final FragmentTransaction transaction = fm.beginTransaction();

		// Clear Stack
		if (clearStack) {
			for (int x = 0; x < fm.getBackStackEntryCount(); x++) {
				fm.popBackStack();
			}
		}

		if (fragment != null) {
			transaction.replace(R.id.container, fragment, tag);
			if (clearStack == false) {
				transaction.addToBackStack(null);
				transaction
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			}

			transaction.commit();
		}
	}

	public static final class DemoListFragment extends ListFragment implements
			ArrayAdapter.AdapterListener {

		public static final String TAG = "DemoListAdapter";

		private ArrayList<String> mData;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			super.getListView().setFastScrollEnabled(true);

			this.mData = this.getDummyData(150);
			super.setListAdapter(new ArrayAdapter<String>(getActivity(),
					this.mData, this));
		}

		public ArrayList<String> getDummyData(int size) {
			final ArrayList<String> data = new ArrayList<String>();
			for (int x = 0; x < size; x++) {
				data.add("List Item " + x);
			}
			return data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Recycle View
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.row_item, parent, false);
			}

			// Set Text
			((TextView) convertView.findViewById(R.id.text)).setText(this.mData
					.get(position));

			return convertView;
		}

	}

}
