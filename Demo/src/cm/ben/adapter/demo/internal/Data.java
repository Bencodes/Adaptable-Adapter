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

package cm.ben.adapter.demo.internal;

import java.util.ArrayList;
import java.util.List;

public final class Data {

	public static final String[] DATA;
	public static final List<String> DATA_ARRAY_LIST;

	public static String[] getNewData (int count, int offset) {
		String[] newData = new String[count];
		for (int x = 0; x < count; x++)
			newData[x] = "Item #" + (x + offset);
		return newData;
	}

	static {
		DATA = new String[25];
		DATA_ARRAY_LIST = new ArrayList<String>() {{
			for (int x = 0; x < 25; x++) {
				add(DATA[x] = "Item #" + x);
			}
		}};
	}

}
