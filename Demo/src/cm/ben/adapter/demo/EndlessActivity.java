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

package cm.ben.adapter.demo;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cm.ben.adapter.EndlessAdapter;
import cm.ben.adapter.demo.internal.Data;

public class EndlessActivity extends ListActivity implements EndlessAdapter.EndlessAdapterListener<String[]> {

	private EndlessAdapter<String> mAdapter;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setListAdapter((mAdapter = new EndlessAdapter<String>(Data.DATA, this)));
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, parent, false);

		((TextView) convertView.findViewById(android.R.id.text1)).setText(mAdapter.getItem(position));

		return convertView;
	}

	@Override
	public String[] doInBackground () {
		try {
			Thread.sleep(1000 * 2);
		} catch (Exception e) {
		} finally {
			return Data.getNewData(25, mAdapter.getCount());
		}
	}

	@Override
	public boolean onPostExecute (String[] data) {
		mAdapter.addAll(data);
		return true;
	}

	@Override
	public View getPendingView (ViewGroup parent) {
		// Inflate our pending view
		return LayoutInflater.from(this).inflate(R.layout.pending_view, parent, false);
	}
}