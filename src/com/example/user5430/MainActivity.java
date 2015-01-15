package com.example.user5430;

/*import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;*/

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
/*import android.view.View;
import android.widget.ArrayAdapter;*/
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {
	  private ProgressBar spinner;
	  private ListView listview;
	  private LinearLayout linearLayout;
	  private TextView tV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        listview = (ListView) findViewById(R.id.listView1);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        tV = (TextView)findViewById(R.id.textView1);
        if(!isOnline(this)){
        	tV.setText("Internet connection not disponible");
        	spinner.setVisibility(View.GONE);
        }
        else{//isOnline//
        	tV.setText("OK");
        }
        
        /*String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < values.length; ++i) {
              list.add(values[i]);
            }
            final StableArrayAdapter adapter = new StableArrayAdapter(this,
                   R.layout.custom_list_view, list);
                listview.setAdapter(adapter);*/
    }
    /*private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private boolean isOnline(Context context) {
    	ConnectivityManager cm =
    	        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	 
    	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    	boolean isConnected = activeNetwork != null &&
    	                      activeNetwork.isConnected();
    	//Log.d("CON", "active connection: " + isConnected);
    	return isConnected;
    }
    /*public void sends(View view) {
        spinner.setVisibility(View.GONE);
  	  	linearLayout.setVisibility(View.VISIBLE);
    }*/
}
