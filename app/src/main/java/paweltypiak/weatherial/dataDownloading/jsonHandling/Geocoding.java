/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.dataDownloading.jsonHandling;

import org.json.JSONObject;

public class Geocoding implements JSONPopulator{

        private String address;

        public String getAddress() {
            return address;
        }

        @Override
        public void populate(JSONObject data) {
            address = data.optString("formatted_address");
        }
}
