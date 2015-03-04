package homework04.homework04;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by Charlie on 3/1/2015.
 * Opens an HTTP client, sends and receives lat/long as JSON, parses JSON, and returns lat lng
 */
public class AsyncGeocodeRequest extends AsyncTask<String, Integer, LatLng> {
  private String zip;
  private String county;

  @Override
  protected LatLng doInBackground(String... params) {
    Log.d("Async", "doInBackground was called");
    Log.d("Async", params[0]);
    LatLng res = null;
    String uri = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    try {
      uri = uri + URLEncoder.encode(params[0], "UTF-8");
    } catch (UnsupportedEncodingException e) {
      Log.d("Async", "NotValid scheme");
    }
    Log.d("Async", uri);
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
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(stringBuilder.toString());

      JSONObject location = ((JSONArray) jsonObject.get("results")) //for lat long
              .getJSONObject(0)
              .getJSONObject("geometry")
              .getJSONObject("location");

      JSONArray address_components = ((JSONArray) jsonObject.get("results")) //for zip county
              .getJSONObject(0)
              .getJSONArray("address_components");

      double lng = location.getDouble("lng");
      double lat = location.getDouble("lat");

      zip = "n/a";
      county = "n/a";
      //looking for zip and county
      for (int i = 0; i < address_components.length(); i++) {
        String type = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                .getJSONArray("address_components").getJSONObject(i)
                .getJSONArray("types").getString(0);
        if (type.equals("postal_code")) {
          zip = address_components.getJSONObject(i).getString("short_name");
        }
        else if (type.equals("administrative_area_level_2")){
          county = address_components.getJSONObject(i).getString("short_name");
        }
      }
      //Set new lat long
      res = new LatLng(lat, lng);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return res;
  }

  @Override
  protected void onPostExecute(LatLng latLng) {
    super.onPostExecute(latLng);

    if (latLng == null) {
      //the request went awry
      Log.d("Async", "The LatLng was invalid in onPostExecute()");
    } else {
      MainActivity.gMap.clear();
      Marker marker = MainActivity.gMap.addMarker(new MarkerOptions()
              .position(latLng));
      MainActivity.gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));

      Log.d("Async",zip); //debug, remove in production
      Log.d("Async", county);
      TextView zc = MainActivity.zipCounty;

      String msg = "Zip: " + zip + " County: " + county;
      zc.setText(msg);
    }
  }
}
