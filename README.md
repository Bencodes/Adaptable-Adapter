Fancy ArrayAdapter
==============================

This project is a simple extension for the [ArrayAdapter](http://developer.android.com/reference/android/widget/ArrayAdapter.html) class Google provides. All it does is pull the getView into an interface so you no longer need to duplicate an Adapter and modify the getView.

Usage
-----------

Include the `ArrayAdapter` class in your project and just simply implement `ArrayAdapter.AdapterListener`. Look at the Demo project to see it in action.

Option 1:

	public void MyActivity extends Activity implements ArrayAdapter.AdapterListener
		
		public void setListAdapter() {
			mListView.setAdapter(new ArrayAdapter<String>(context, data, this));
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Do your work here
		
			return convertView;
		}
		
		// Your Code
		// ...
		

	
Option 2:

		mListView.setAdapter(new ArrayAdapter<String>(this, someArrayList, new AdapterListener() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// Do your work here
				
				return convertView;
			}
		}));