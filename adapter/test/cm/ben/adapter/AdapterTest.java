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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.*;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class AdapterTest {

	@Test
	public void testSetAndGetAdapterListener () throws Exception {
		Adapter.AdapterListener adapterListener = new Adapter.AdapterListener() {
			@Override
			public View getView (int position, View convertView, ViewGroup parent) {
				return null;
			}
		};

		Adapter adapter = new Adapter();
		adapter.setAdapterListener(adapterListener);
		assertThat(adapter.getAdapterListener()).isNotNull().isEqualsToByComparingFields(adapterListener).isEqualTo(adapterListener);

		adapter = new Adapter(adapterListener);
		assertThat(adapter.getAdapterListener()).isNotNull().isEqualsToByComparingFields(adapterListener).isEqualTo(adapterListener);


		adapter = new Adapter(new Object[0], adapterListener);
		assertThat(adapter.getAdapterListener()).isNotNull().isEqualsToByComparingFields(adapterListener).isEqualTo(adapterListener);


		adapter = new Adapter(new ArrayList(), adapterListener);
		assertThat(adapter.getAdapterListener()).isNotNull().isEqualsToByComparingFields(adapterListener).isEqualTo(adapterListener);
	}


	@Test
	public void testSetAndGetData () throws Exception {
		final Integer[] dummyData = randomData(25);
		final List<Integer> dummyData2 = Arrays.asList(dummyData);

		Adapter<Integer> adapter;

		// Add by constructor
		adapter = new Adapter(dummyData);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);

		adapter = new Adapter(dummyData2);
		assertThat(adapter.getCount()).isEqualTo(dummyData2.size());

		adapter = new Adapter(dummyData, null);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);

		adapter = new Adapter(dummyData2, null);
		assertThat(adapter.getCount()).isEqualTo(dummyData2.size());


		// Add by addAll
		adapter = new Adapter();
		adapter.addAll(dummyData);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);

		adapter = new Adapter();
		adapter.addAll(dummyData2);
		assertThat(adapter.getCount()).isEqualTo(dummyData2.size());

		adapter = new Adapter();
		adapter.addAll(dummyData);
		adapter.addAll(dummyData2);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length + dummyData2.size());


		// Add by add
		adapter = new Adapter();
		for (int x = 0; x < dummyData.length; x++)
			adapter.add(dummyData[x]);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);


		// Add by insert
		adapter = new Adapter();
		for (int x = 0; x < dummyData.length; x++)
			adapter.insert(dummyData[x], x);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);

		// Test Remove
		adapter = new Adapter(dummyData);
		adapter.remove(dummyData[dummyData.length / 2]);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length - 1);

		// Clear
		adapter = new Adapter();
		assertThat(adapter.getCount()).isEqualTo(0);
		assertThat(adapter.isEmpty()).isTrue();

		adapter.addAll(dummyData);
		assertThat(adapter.getCount()).isEqualTo(dummyData.length);
		assertThat(adapter.isEmpty()).isFalse();

		adapter.clear();
		assertThat(adapter.getCount()).isEqualTo(0);
		assertThat(adapter.isEmpty()).isTrue();
	}


	@Test
	public void testSort () throws Exception {
		Adapter<Integer> adapter;
		List<Integer> dummyData;


		dummyData = Arrays.asList(randomData(25));
		adapter = new Adapter(dummyData);

		final Comparator<Integer> ascending = new Comparator<Integer>() {
			@Override
			public int compare (Integer i1, Integer i2) {
				return i1.compareTo(i2);
			}
		};

		adapter.sort(ascending);
		assertThat(adapter.getObjects()).isSorted();
		assertThat(adapter.getObjects()).isSortedAccordingTo(ascending);

		Collections.sort(dummyData, ascending);
		assertThat(dummyData).isSorted();
		assertThat(dummyData).isSortedAccordingTo(ascending);
		assertThat(dummyData).isEqualTo(adapter.getObjects());


		final Comparator<Integer> descending = new Comparator<Integer>() {
			@Override
			public int compare (Integer i1, Integer i2) {
				return i2.compareTo(i1);
			}
		};

		adapter = new Adapter((dummyData = Arrays.asList(randomData(25))));

		adapter.sort(descending);
		assertThat(adapter.getObjects()).isSortedAccordingTo(descending);

		Collections.sort(dummyData, descending);
		assertThat(dummyData).isSortedAccordingTo(descending);
		assertThat(dummyData).isEqualTo(adapter.getObjects());
	}


	@Test
	public void testGetItem () throws Exception {
		final List<Integer> dummyData = Arrays.asList(randomData(25));
		final Adapter<Integer> adapter = new Adapter(dummyData);
		for (int x = 0; x < dummyData.size(); x++) {
			assertThat(adapter.getItem(x)).isNotNull().isEqualTo(dummyData.get(x));
		}
	}

	@Test
	public void testGetPosition () throws Exception {
		final List<Integer> dummyData = Arrays.asList(randomData(25));
		final Adapter<Integer> adapter = new Adapter(dummyData);
		for (int x = 0; x < dummyData.size(); x++) {
			assertThat(adapter.getPosition(dummyData.get(x))).isNotNull().isEqualTo(x);
		}
	}

	@Ignore
	public static final Integer[] randomData (int count) {
		Random random = new Random();
		Integer[] d = new Integer[count];
		for (int x = 0; x < count; x++) {
			int i = (random.nextInt((Integer.MAX_VALUE - 1) + 1) + 1);
			d[x] = i;
		}

		return d;
	}
}
