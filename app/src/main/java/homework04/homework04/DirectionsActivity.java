package homework04.homework04;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;


public class DirectionsActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_directions);
    Intent intent = getIntent();

      AutoCompleteTextView from = (AutoCompleteTextView) findViewById(R.id.from);
      AutoCompleteTextView to = (AutoCompleteTextView) findViewById(R.id.to);
      from.setText("");
      to.setText("");
      from.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
      to.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_directions, menu);
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

  public void dir(View view){
      // Do something in response to button
      EditText editText =(EditText) findViewById(R.id.from);
      String addr1 = editText.getText().toString();
      editText =(EditText) findViewById(R.id.to);
      String addr2 = editText.getText().toString();
      //add some string parse here
      new AsyncDirectionRequest().execute(addr1,addr2);
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      setContentView(R.layout.activity_main);

  }
}
