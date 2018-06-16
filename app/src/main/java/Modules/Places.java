
        package Modules;

        import android.util.Log;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

        /**
         * Created by nguyenthang on 4/19/18.
         */

        public class Places {
            public List<HashMap<String, String>> parse(JSONObject jsonObject) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray("results");
                    String nextPage1;
                    String nextPage2;
                    JSONObject object1 = null;
                    JSONObject object2 = null;

                    String token1 = getTokenPage(jsonObject);

                    if(token1!=null || token1 == "") {
                        nextPage1 = getNextPage(token1);
                        if(nextPage1 != null) {
                            object1 = new JSONObject(nextPage1);
                            JSONArray jsonArray1 = object1.getJSONArray("results");
                            for(int i=0;i<jsonArray1.length();i++)
                                jsonArray.put(jsonArray1.get(i));
                            String token2 = getTokenPage(object1);
                            if(token2 != null) {
                                nextPage2 = getNextPage(token2);
                                if(nextPage2 != null) {
                                    object2 = new JSONObject(nextPage2);
                                    JSONArray jsonArray2 = object2.getJSONArray("results");
                                    for(int i=0;i<jsonArray2.length();i++)
                                        jsonArray.put(jsonArray2.get(i));
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return getPlaces(jsonArray);
            }

            private String getTokenPage(JSONObject jsonObject) {
                String token = null;
                try {
                    token = jsonObject.getString("next_page_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return token;
            }

            private String getNextPage(String token) {
                http http = new http();
                String result = null;
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + token +"&key=AIzaSyD73ix-2OxsdM03JnoTj5gbxwbPRAJZSiM";
                try {
                    result = http.read(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
                int placesCount = jsonArray.length();
                List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> placeMap = null;

                for (int i = 0; i < placesCount; i++) {
                    try {
                        placeMap = getPlace((JSONObject) jsonArray.get(i));
                        placesList.add(placeMap);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return placesList;
            }
            private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
                HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
                String placeName = "-NA-";
                String vicinity = "-NA-";
                String latitude = "";
                String longitude = "";
                String reference = "";
                String photo ="";
                Double rating = 0.0;
                try {
                    if (!googlePlaceJson.isNull("name")) {
                        placeName = googlePlaceJson.getString("name");
                    }
                    if (!googlePlaceJson.isNull("vicinity")) {
                        vicinity = googlePlaceJson.getString("vicinity");
                    }
                    latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                    longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
                    reference = googlePlaceJson.getString("reference");
                    if (!googlePlaceJson.isNull("photos")){
                        JSONArray photos = googlePlaceJson.getJSONArray("photos");
                        JSONObject pt = photos.getJSONObject(0);
                        photo="https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photoreference="+ pt.getString("photo_reference")
                                +"&sensor=true&key=AIzaSyD73ix-2OxsdM03JnoTj5gbxwbPRAJZSiM";
                    }
                    if (!googlePlaceJson.isNull("rating")) {
                        rating= googlePlaceJson.getDouble("rating");
                    }
                    googlePlaceMap.put("place_name", placeName);
                    googlePlaceMap.put("vicinity", vicinity);
                    googlePlaceMap.put("lat", latitude);
                    googlePlaceMap.put("lng", longitude);
                    googlePlaceMap.put("reference", reference);
                    googlePlaceMap.put("photo",photo);
                    googlePlaceMap.put("rating",Double.toString(rating));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return googlePlaceMap;
            }
        }
