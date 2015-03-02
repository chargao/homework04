package homework04.homework04;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
* Created by Charlie on 3/1/2015.
* Opens an HTTP client, sends and recieves lat/long as JSON, parses JSON, and returns latlng
*/
public class AsyncGeocodeRequest extends AsyncTask<String, Integer, LatLng> {
	@Override
	protected LatLng doInBackground(String... params) {
		Log.d("Async","doInBackground was called");
		LatLng res=null;
		String uri = "https://maps.googleapis.com/maps/api/geocode/json?address="+params[0];
		HttpGet httpGet = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		}
    catch (ClientProtocolException e) {
			e.printStackTrace();
		}
    catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(stringBuilder.toString());

			double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
			.getJSONObject("geometry").getJSONObject("location")
			.getDouble("lng");

			double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
			.getJSONObject("geometry").getJSONObject("location")
			.getDouble("lat");

			//Set new lat long
			res = new LatLng(lat, lng);
		}
    catch (JSONException e) {
      e.printStackTrace();
    }
		return res;
	}

	@Override
	protected void onPostExecute(LatLng latLng) {
		super.onPostExecute(latLng);

    if (latLng==null){
      //the request went awry
    }
    else {
      Marker marker = MainActivity.gMap.addMarker(new MarkerOptions()
              .position(latLng));
    }
	}
}
