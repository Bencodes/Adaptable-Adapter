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

import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import cm.ben.adapter.Adapter;


public class DemoActivity extends ListActivity implements Adapter.AdapterListener {

	private Adapter<DemoItem> mAdapter;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new Adapter<DemoItem>(this);
		mAdapter.add(new DemoItem("Basic Example", BasicActivity.class));
		mAdapter.add(new DemoItem("Endless Example", EndlessActivity.class));
		super.setListAdapter(mAdapter);
	}

	@Override
	protected void onListItemClick (ListView listView, View view, int position, long id) {
		startActivity(mAdapter.getItem(position).intent);
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(this).inflate(R.layout.simple_list_item_1, parent, false);

		((TextView) convertView.findViewById(R.id.text1)).setText(mAdapter.getItem(position).name);

		return convertView;
	}

	private final class DemoItem {
		public final String name;
		public final Intent intent;

		public DemoItem (String name, Class clss) {
			this.name = name;
			this.intent = new Intent(DemoActivity.this, clss);
		}
	}

}
