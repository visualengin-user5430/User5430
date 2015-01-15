package com.example.user5430;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {
	  private final String url = "http://www.visual-engin.com/Web";
	  private ProgressBar spinner;
	  private ListView listview;
	  private LinearLayout linearLayout;
	  private TextView textView;
	  private boolean finished;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        listview = (ListView) findViewById(R.id.listView1);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        textView = (TextView)findViewById(R.id.textView1);
        if(!isOnline(this)){
        	textView.setText("Internet connection not disponible");
        	spinner.setVisibility(View.GONE);
        }
        else{//isOnline//
        	textView.setText("connecting...");
        	finished = true;
        	( new ParseURL() ).execute(new String[]{url});
        }
        
    }
    
    @Override
    public void onResume() {
        super.onResume();

        // try to connect again if it wasn't possible before
       if (!finished) {
    	   spinner.setVisibility(View.VISIBLE);
           if(!isOnline(this)){
           	textView.setText("Internet connection not disponible");
           	spinner.setVisibility(View.GONE);
           }
           else{//isOnline//
           	textView.setText("connecting...");
           	finished = true;
           	( new ParseURL() ).execute(new String[]{url});
           }
        }
    }


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
    
    private class ParseURL extends AsyncTask<String, Void, String> {
    	 StableArrayAdapter adapter;
  	     @Override
  	     protected String doInBackground(String... strings) {
  	         String s ="";
  	         try {
  	        	 // Connect and download the HTML page
  	             Log.d("STABL", "Connecting to ["+strings[0]+"]");
  	             Document doc  = Jsoup.connect(strings[0]).get();
  	             Log.d("STABL", "Connected to ["+strings[0]+"]");
  	             // Get document (HTML page) title
  	             String title = doc.title();
  	             Log.d("OKtit", "Title ["+title+"]");
  	             // Get 'a' tagged elements 
  	             Elements links = doc.select("a[href]");
  	             ArrayList<String> list = new ArrayList<String>();
  	             // retrieve the URL from the 'a' elements
  	             for (Element linkElm : links)	{
  	            	 Log.d("GEt", "reading link");
  	            	 String link = linkElm.attr("href");
  	            	 list.add(link);
  	             }
  	             // prepares the listview with a custom layout to present the results
  	              adapter = new StableArrayAdapter(getApplicationContext(),
  	                  R.layout.custom_list_view, list);
  	              listview.setAdapter(adapter);
  	         }
  	         catch(Throwable t) {
  	             t.printStackTrace();
  	         }
  	         return s;
  	     }

  	     @Override
  	     protected void onPostExecute(String s) {
  	         super.onPostExecute(s);
  	         textView.setVisibility(View.INVISIBLE);
  	         spinner.setVisibility(View.INVISIBLE);
  	         linearLayout.setVisibility(View.VISIBLE);
  	     }
    }
    
    private class StableArrayAdapter extends ArrayAdapter<String> {

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
	  }

    
    private boolean isOnline(Context context) {
    	ConnectivityManager cm =
    	        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	 
    	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    	boolean isConnected = activeNetwork != null &&
    	                      activeNetwork.isConnected();
    	Log.d("CON", "active connection: " + isConnected);
    	return isConnected;
    }
}
