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


/**
 * Main and only Activity that contains all the logic of the APP.
 * @author User5430
 * @version 1.0.0
 */
public class MainActivity extends Activity {
	  private final static String url = "http://www.visual-engin.com/Web";
	  private ProgressBar spinner;
	  private ListView listview;
	  private LinearLayout linearLayout;
	  private TextView textView;
	  /**
	   * flag indicating if the content has been received.
	   */
	  public static boolean isFinished = false;
	  public StableArrayAdapter adapter;
	  public static ArrayList<String> list = new ArrayList<String>();
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        listview = (ListView) findViewById(R.id.listView1);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        textView = (TextView)findViewById(R.id.textView1);
    }
    
    /**
     * When the Activity is resumed shows the result or in 
     * case of not having achieved a connection, tries to 
     * connect again.
     */
    @Override
    public void onResume() {
        super.onResume();
        // try to connect again if it wasn't possible before
        if (!isFinished) {
    	   spinner.setVisibility(View.VISIBLE);
           if(!isOnline(this)){
           		textView.setText("Internet connection not disponible");
           		spinner.setVisibility(View.GONE);
           }
           else{//isOnline//
           		textView.setText("connecting...");
           		( new ParseURL() ).execute(new String[]{url});
           }
        }
        else{
        	adapter = new StableArrayAdapter(getApplicationContext(),
	                  R.layout.custom_list_view, list);
        	listview.setAdapter(adapter);
        	linearLayout.setVisibility(View.VISIBLE);
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
    
   /**
    * Uses a separate thread to connect to the webpage and retrieve
    * the information. Once it has the info, uses 'JSoup' library to 
    * extract the 'a' tagged elements.
    * The results are shown in a listview.
    */
    private class ParseURL extends AsyncTask<String, Void, String> {
    	// public StableArrayAdapter adapter;

    	 /**
    	  * Background process that connect and treats with
    	  * the webpage.
    	  * @param strings	first element of the array is the URL
    	  * @return	empty string
    	  */
  	     @Override
  	     protected String doInBackground(String... strings) {
  	         try {
  	        	 // Connect and download the HTML page
  	             Log.d("STABL", "Connecting to ["+strings[0]+"]");
  	             Document doc  = Jsoup.connect(strings[0]).get();
  	             Log.d("STABL", "Connected to ["+strings[0]+"]");
  	             // Get document (HTML page) title
  	             Log.d("OKtit", "Title ["+doc.title()+"]");
  	             // Get 'a' tagged elements 
  	             Elements links = doc.select("a[href]");
  	             // retrieve the URL from the 'a' elements
  	             for (Element linkElm : links)	{
  	            	 String link = linkElm.attr("href");
  	            	 Log.d("GET", "link: " + link);
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
  	         return "";
  	     }
    	 /**
    	  * Once the webpage has been treated, shows
    	  * the results on screen.
    	  * @param s	empty string
    	  */
  	     @Override
  	     protected void onPostExecute(String s) {
  	         super.onPostExecute(s);
  	         isFinished = true;
  	         textView.setVisibility(View.GONE);
  	         spinner.setVisibility(View.GONE);
  	         linearLayout.setVisibility(View.VISIBLE);
  	     }
    }
    
	/**
	 * Custom adapter for the ListView.
	 * @param strings	first element of the array is the URL
	 * @return	empty string
	 */
    private class StableArrayAdapter extends ArrayAdapter<String> {
	    HashMap<String, Integer> map = new HashMap<String, Integer>();

	   	/**
	   	* Constructor.
	   	* Inserts each list object in a map for an easy access.
	   	* @param context	Activity context
	   	* @param textViewResourceId	Resource ID of the custom adapter
	   	* @param objects	List of objects
	   	*/
	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	    	  map.put(objects.get(i), i);
	      }
	    }
	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return map.get(item);
	    }
	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }
	}

   	/**
   	* Identify if the device is connected to some network.
   	* @param context	Activity context
   	* @return	boolean	true if exists internet connection
   	*/
    public boolean isOnline(Context context) {
    	ConnectivityManager cm =
    	        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	 
    	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    	boolean isConnected = activeNetwork != null &&
    	                      activeNetwork.isConnected();
    	Log.d("CON", "active connection: " + isConnected);
    	return isConnected;
    }
}
