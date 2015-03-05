package homework04.homework04;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 3/4/2015.
 */
public class AsyncDirectionRequest extends AsyncTask<String, Integer, List<LatLng>> {
    private List<LatLng> stepSt;


    @Override
    protected List<LatLng> doInBackground(String... params) {

        List<Polyline> path = new ArrayList<Polyline>();
        String uri = "https://maps.googleapis.com/maps/api/directions/json?";
        try {
            uri = uri + "origin=" + URLEncoder.encode(params[0], "UTF-8") + "&destination=" + URLEncoder.encode(params[1], "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d("Async", "NotValid scheme");
        }

        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();
        stepSt = new ArrayList<LatLng>();

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

            JSONObject legs = ((JSONArray) jsonObject.get("routes")) //for lat long
                    .getJSONObject(0)
                    .getJSONObject("overview_polyline");
                    //.getJSONArray("steps");
            Log.d("Async", legs.toString());
            String s = legs.getString("points");
            stepSt = decodePoly(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stepSt;
    }
    @Override
    protected void onPostExecute(List<LatLng> latLng) {
        super.onPostExecute(latLng);

        if(latLng.size() == 0){

        }else {
            MainActivity.steps = stepSt;
            MainActivity.gMap.clear();
            /*for(int i = 0; i < latLng.size()-1; i++){
                MainActivity.gMap.addPolyline(new PolylineOptions().add(latLng.get(i),latLng.get(i+1)).width(5f).color(Color.BLUE));
            }*/
            MainActivity.gMap.addPolyline(new PolylineOptions().addAll(stepSt).width(5f).color(Color.BLUE));
                    MainActivity.gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng.get(0), 10f));
        }
    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

}
