package paweltypiak.matweather.jsonHandling;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pawcioch on 30.07.2016.
 */
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
