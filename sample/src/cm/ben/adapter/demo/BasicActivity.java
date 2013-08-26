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
import cm.ben.adapter.Adapter;
import cm.ben.adapter.demo.internal.Data;

public class BasicActivity extends ListActivity implements Adapter.AdapterListener {

	private Adapter<String> mAdapter;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.getListView().setFooterDividersEnabled(true);
		super.setListAdapter((mAdapter = new Adapter<String>(Data.DATA, this)));
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, parent, false);

		((TextView) convertView.findViewById(android.R.id.text1)).setText(mAdapter.getItem(position));

		return convertView;
	}
}



