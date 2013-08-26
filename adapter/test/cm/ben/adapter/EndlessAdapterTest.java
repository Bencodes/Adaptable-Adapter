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
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EndlessAdapterTest {

	@Test
	public void testGetAndSetAdapterListener () throws Exception {
		EndlessAdapter.EndlessAdapterListener endlessAdapterListener = new EndlessAdapter.EndlessAdapterListener() {
			@Override
			public Object doInBackground () {
				return null;
			}

			@Override
			public boolean onPostExecute (Object o) {
				return false;
			}

			@Override
			public View getPendingView (ViewGroup parent) {
				return null;
			}

			@Override
			public View getView (int position, View convertView, ViewGroup parent) {
				return null;
			}
		};

		EndlessAdapter endlessAdapter = new EndlessAdapter();
		endlessAdapter.setAdapterListener(endlessAdapterListener);
		assertThat(endlessAdapter.getAdapterListener()).isNotNull().isEqualsToByComparingFields(endlessAdapterListener).isEqualTo(endlessAdapterListener);

		endlessAdapter = new EndlessAdapter(endlessAdapterListener);
		assertThat(endlessAdapter.getAdapterListener()).isNotNull().isEqualsToByComparingFields(endlessAdapterListener).isEqualTo(endlessAdapterListener);

		endlessAdapter = new EndlessAdapter(new Object[0], endlessAdapterListener);
		assertThat(endlessAdapter.getAdapterListener()).isNotNull().isEqualsToByComparingFields(endlessAdapterListener).isEqualTo(endlessAdapterListener);

		endlessAdapter = new EndlessAdapter(new ArrayList(), endlessAdapterListener);
		assertThat(endlessAdapter.getAdapterListener()).isNotNull().isEqualsToByComparingFields(endlessAdapterListener).isEqualTo(endlessAdapterListener);

		try {
			// Test an error first
			endlessAdapter = new EndlessAdapter<Integer>();
			endlessAdapter.setAdapterListener(new Adapter.AdapterListener() {
				@Override
				public View getView (int position, View convertView, ViewGroup parent) {
					return null;
				}
			});

			failBecauseExceptionWasNotThrown(NullPointerException.class);
		} catch (Exception e) {
			assertThat(e).isNotNull().isInstanceOf(IllegalArgumentException.class).hasMessage("Param must be instance of EndlessAdapterListener");
		}
	}

	@Test
	public void testSetKeepAppending () throws Exception {
		EndlessAdapter adapter;
		Integer[] dummyData = AdapterTest.randomData(25);
		List<Integer> dummyData2 = Arrays.asList(dummyData);
		EndlessAdapter.EndlessAdapterListener endlessAdapterListener = new EndlessAdapter.EndlessAdapterListener() {
			@Override
			public Object doInBackground () {
				return null;
			}

			@Override
			public boolean onPostExecute (Object o) {
				return false;
			}

			@Override
			public View getPendingView (ViewGroup parent) {
				return null;
			}

			@Override
			public View getView (int position, View convertView, ViewGroup parent) {
				return null;
			}
		};

		adapter = new EndlessAdapter();
		adapter.addAll(dummyData);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);

		adapter = new EndlessAdapter();
		adapter.addAll(dummyData2);
		assertThat(adapter.getCount()).isEqualTo(dummyData2.size());

		adapter = new EndlessAdapter(dummyData);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);

		adapter = new EndlessAdapter(dummyData, endlessAdapterListener);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length + 1);

		adapter = new EndlessAdapter(dummyData2);
		assertThat(adapter.getCount()).isEqualTo(dummyData2.size());

		adapter = new EndlessAdapter(dummyData2, endlessAdapterListener);
		assertThat(adapter.getCount()).isEqualTo(dummyData2.size() + 1);


		adapter = new EndlessAdapter(dummyData);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);
		adapter.setAdapterListener(endlessAdapterListener);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length + 1);
		adapter.setKeepAppending(false);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);
		adapter.setKeepAppending(true);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length + 1);


		adapter = new EndlessAdapter(dummyData, endlessAdapterListener);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length + 1);
		adapter.setKeepAppending(false);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);
		adapter.setKeepAppending(true);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length + 1);
	}

}
