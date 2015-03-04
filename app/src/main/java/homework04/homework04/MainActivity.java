package homework04.homework04;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity implements OnMapReadyCallback{
  protected static TextView zipCounty;
  protected static GoogleMap gMap;
  MapFragment mapFrag;
  static final LatLng HAMBURG = new LatLng(53.558, 9.927);
  //static final LatLng KIEL = new LatLng(53.551, 9.993);

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    mapFrag.getMapAsync(this);
    gMap = mapFrag.getMap();
    zipCounty = (TextView) findViewById(R.id.zip_county);
  }

  @Override
  public void onMapReady(GoogleMap map) {
    gMap=map;
    gMap.addMarker(new MarkerOptions()
            .position(HAMBURG)
            .title("Marker"));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /** Called when the user clicks the Search button */
  public void search(View view){
    // Do something in response to button
    EditText editText =(EditText) findViewById(R.id.edit_message);
    String addr = editText.getText().toString();
    //add some string parse here
    new AsyncGeocodeRequest().execute(addr);
  }
}