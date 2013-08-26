Adaptable ArrayAdapter
============

Dead simple list adapters in Andorid.


Download
------------
Download [the latest JAR](https://raw.github.com/Bencodes/Adaptable-Adapter/master/downloads/adapter.jar).



Usage
------------
Basic usage:

```java
public class MyActivity extends ListActivity implements Adapter.AdapterListener {

	private Adapter<String> mAdapter;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new Adapter<String>(new String[]{"1", "2", "3"}, this);
		super.setListAdapter(mAdapter);
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		// Inflate our view here
		return convertView;
	}
}
```

Endless Scrolling Usage:

```java
public class MyActivity extends ListActivity implements EndlessAdapter.EndlessAdapterListener<String[]> {

	private EndlessAdapter<String> mAdapter;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new EndlessAdapter<String>(new String[]{"1", "2", "3"}, this);
		super.setListAdapter(mAdapter);
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		// Inflate our view here
		return convertView;
	}

	@Override
	public String[] doInBackground () {
		// Get more data
		return new String[]{"4", "5", "6"};
	}

	@Override
	public boolean onPostExecute (String[] data) {
		// Append our data
		mAdapter.addAll(data);
		return true;
	}

	@Override
	public View getPendingView (ViewGroup parent) {
		// Inflate our progress view
		ProgressBar progressBar = new ProgressBar(this);
		return progressBar;
	}
}
```


Developed By
--------

* [Ben Lee](mailto:ben@ben.cm)



License
--------
    Copyright 2013 Ben Lee

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.