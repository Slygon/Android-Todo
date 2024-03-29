package com.slygon.todoapp;
//package com.hmkcode.android;

//import android.app.*;
//import android.os.*;
//import android.widget.*;

import android.app.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.loopj.android.http.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.json.*;

public class MainActivity extends Activity
{
	EditText etResponse;
	TextView tvIsConnected;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get reference to the views
		etResponse = (EditText) findViewById(R.id.etResponse);
		tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

		// check if you are connected or not
		if (isConnected())
		{
			tvIsConnected.setBackgroundColor(0xFF00CC00);
			tvIsConnected.setText("You are conncted");
		}
		else
		{
			tvIsConnected.setText("You are NOT conncted");
		}

		// call AsynTask to perform network operation on separate thread
//		new HttpAsyncTask().execute("http://hmkcode.appspot.com/rest/controller/get.json");
		//new HttpAsyncTask().execute("http://elad-site.herokuapp.com/api/todos");
		
		
	}

	public static String GET(String url)
	{
		InputStream inputStream = null;
		String result = "";
		try
		{

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		}
		catch (Exception e)
		{
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public boolean isConnected()
	{
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
			return true;
		else
			return false;   
	}
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... urls)
		{

			return GET(urls[0]);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result)
		{
			Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
			
			try
			{
				//etResponse.setText(result);
				JSONObject json = new JSONObject(result); // convert String to JSONObject
				
				//JSONArray g = new JSONArray(result);
				etResponse.setText(json.toString(1));
				
				
//				JSONArray articles = json.getJSONArray("articleList"); // get articles array
//				articles.length(); // --> 2
//				articles.getJSONObject(0); // get first article in the array
//				articles.getJSONObject(0).names(); // get first article keys [title,url,categories,tags]
//				articles.getJSONObject(0).getString("url"); // return an article url
			}
			catch (JSONException e)
			{
				etResponse.setText(result);
			}
		}
		
		
	}
}

class TodoClientUsage {
    public void getPublicTimeline() throws JSONException {
        TodoClient.get("statuses/public_timeline.json", null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					// If the response is JSONObject instead of expected JSONArray
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
					// Pull out the first event on the public timeline
					try
					{
						JSONObject firstEvent = (JSONObject) timeline.get(0);
					}
					catch (JSONException e)
					{}
					String tweetText =(String) firstEvent.getString("text").;

					// Do something with the response
					System.out.println(tweetText);
				}
			});
    }
}
