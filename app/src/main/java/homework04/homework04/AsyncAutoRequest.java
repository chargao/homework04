package homework04.homework04;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Daniel on 3/5/2015.
 */
public class AsyncAutoRequest extends AsyncTask<String, Integer, ArrayList<String>> {


    @Override
    protected ArrayList<String> doInBackground(String... params) {
        String uri = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
        try {
            uri = uri + URLEncoder.encode(params[0], "UTF-8")+ "&key=AIzaSyBFc5ENf3y1A7MIsjD_GKm3Iwu9cAEVkp0";
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
        //Log.d("Async", stringBuilder.toString());
        ArrayList<String> res = new ArrayList<String>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            JSONArray pred = ((JSONArray) jsonObject.get("predictions")); //for lat long

            for(int i = 0; i < pred.length(); i++){
                res.add(pred.getJSONObject(i).getString("description"));
            }
                    //.getJSONObject("overview_polyline");
            //.getJSONArray("steps");
            Log.d("Async", "Async" +res.toString());
            //String s = legs.getString("points");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    protected void onPostExecute(ArrayList<String> latLng) {
        super.onPostExecute(latLng);

        if(latLng.size() == 0){

        }else {
            PlacesAutoCompleteAdapter.res = latLng;
        }
    }

}
