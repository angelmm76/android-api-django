package course.examples.Networking.APIdjango;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Call;
import okhttp3.Callback;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;

public class NetworkingURLActivity extends Activity {
	private TextView mTextView;
	private static final String TAG = "Networking API Django";

	private static final String USER = "lolo";
	private static final String PASS = "123";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		mTextView = (TextView) findViewById(R.id.textView1);

		final Button loadButton1 = (Button) findViewById(R.id.button1);
		loadButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Button 1");
				mTextView.setText("");
				new HttpGetTask1().execute();
			}
		});

		final Button loadButton2 = (Button) findViewById(R.id.button2);
		loadButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Button 2");
				mTextView.setText("");
				new HttpGetTask2().execute();
			}
		});

		final Button loadButton3 = (Button) findViewById(R.id.button3);
		loadButton3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Button 3");
				mTextView.setText("");
				loadGetRunnable();
			}
		});

		final Button loadButton4 = (Button) findViewById(R.id.button4);
		loadButton4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Button 4");
				mTextView.setText("");
				loadAsyncCall();
			}
		});

		final Button loadButton5 = (Button) findViewById(R.id.button5);
		loadButton5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Button 5");
				mTextView.setText("");
				loadAsyncCallGson();
			}
		});
	}

	private class HttpGetTask1 extends AsyncTask<Void, Void, String> {

		private static final String TAG = "HttpGetTask1";

		private static final String USER_PASS = USER + ":" + PASS;
		private static final String URL = "http://10.0.2.2:8000/blog/api/blogposts/5/";
//		private static final String URL = "http://10.0.2.2:8000/blog/17/";

		@Override
		protected String doInBackground(Void... params) {
			String data = "";
			HttpURLConnection httpUrlConnection = null;

			try {
				Log.i(TAG, "httpUrlConnecting...");
				String encodedAuthentication = Base64.encodeToString(USER_PASS.getBytes(), Base64.NO_WRAP);

				httpUrlConnection = (HttpURLConnection) new URL(URL).openConnection();
				httpUrlConnection.setRequestProperty("Authorization", "Basic "+encodedAuthentication);
//				httpUrlConnection.setUseCaches(false);
//				httpUrlConnection.setConnectTimeout(2000);
//				httpUrlConnection.setReadTimeout(2000);

				InputStream in = new BufferedInputStream(
						httpUrlConnection.getInputStream());

				data = readStream(in);
				Log.i(TAG, "Data: " + data);

			} catch (MalformedURLException exception) {
				exception.printStackTrace();
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				exception.printStackTrace();
				Log.e(TAG, "IOException");
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			mTextView.setText(result);
		}

		private String readStream(InputStream in) {
			BufferedReader reader = null;
			StringBuffer data = new StringBuffer("");
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					data.append(line);
				}
			} catch (IOException e) {
				Log.e(TAG, "IOException");
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return data.toString();
		}
	}

	private class HttpGetTask2 extends AsyncTask<Void, Void, String> {

		private static final String TAG = "HttpGetTask2";

		private static final String URL = "http://10.0.2.2:8000/blog/api/blogposts/4/";
//		private static final String URL = "http://10.0.2.2:8000/blog/17/";

		@Override
		protected String doInBackground(Void... params) {
			String data = "";
			OkHttpClient client = new OkHttpClient();

//			Request request = new Request.Builder()
//					.url(URL)
//					.build();

			// Request with parameter
			HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
//			urlBuilder.addQueryParameter("north", NORTH);
//			urlBuilder.addQueryParameter("south", SOUTH);

			String url = urlBuilder.build().toString();
			String credential = Credentials.basic(USER, PASS);

			Request request = new Request.Builder()
					.url(url)
					.header("Authorization", credential)
					.build();

			try {
				Log.i(TAG, "OkHttpClient Connecting...");
				Response response = client.newCall(request).execute();
				data = response.body().string();
				Log.i(TAG, "Data: " + data);
				Headers responseHeaders = response.headers();
				for (int i = 0; i < responseHeaders.size(); i++) {
					Log.d("DEBUG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "IOException, response is not successful");
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			mTextView.setText(result);
		}
	}

	private void loadGetRunnable() {

		final String TAG = "Get Runnable";
		final String URL = "http://10.0.2.2:8000/blog/api/blogposts/8/";

		Runnable getRunnable = new Runnable() {

			@Override
			public void run() {

				String resdata ="";
				OkHttpClient client = new OkHttpClient();

				// Request with parameter
				HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
//			urlBuilder.addQueryParameter("north", NORTH);
//			urlBuilder.addQueryParameter("south", SOUTH);

				String url = urlBuilder.build().toString();
				String credential = Credentials.basic(USER, PASS);

				Request request = new Request.Builder()
						.url(url)
						.header("Authorization", credential)
						.build();

				try {
					Log.i(TAG, "OkHttpClient Connecting...");
					Response response = client.newCall(request).execute();
					resdata = response.body().string();
					Log.i(TAG, "Data: " + resdata);
					Headers responseHeaders = response.headers();
					for (int i = 0; i < responseHeaders.size(); i++) {
						Log.d("DEBUG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
					}
				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "IOException, response is not successful");
				}
				final String data = resdata;
				// Run view-related code back on the main thread
				NetworkingURLActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.i(TAG, "Setting data, main thread");
						mTextView.setText(data);
					}
				});
			}
		};

//			Thread mThread = new Thread(getRunnable);
//			mThread.start();
		new Thread(getRunnable).start();
	}

	private void loadAsyncCall() {

		final String TAG = "LoadAsyncCall3";
		final String URL = "http://10.0.2.2:8000/blog/api/blogposts/2/";

		String data = "";
		OkHttpClient client = new OkHttpClient();

		String credential = Credentials.basic(USER, PASS);

		Request request = new Request.Builder()
				.url(URL)
				.header("Authorization", credential)
				.build();

		Log.i(TAG, "Async OkHttpClient Connecting...");

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				Log.i(TAG, "Failed connection 3");
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, final Response response) throws IOException {
				// ... check for failure using `isSuccessful` before proceeding
				if (!response.isSuccessful()) {
					throw new IOException("Unexpected code " + response);
				}

				// Read data on the worker thread
				final String data = response.body().string();
				Log.i(TAG, "Data: " + data);

				// Run view-related code back on the main thread
				NetworkingURLActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.i(TAG, "Setting data, main thread");
						mTextView.setText(data);
					}
				});
			}
		});
	}

	private static class blogpost {
		String blogpost_title;
		String blogpost_content;
		String pub_date;
		String author;
		String[] comments;

		public String getTitle() {
			return blogpost_title;
		}

		public String getContent() {
			return blogpost_content;
		}

		public String getDate() {
			return pub_date;
		}

		public String getAuthor() {
			return author;
		}

		public String[] getComments() {
			return comments;
		}

		@Override
		public String toString()
		{
			return "Blogpost data [title = "+blogpost_title+", content = "+blogpost_content+", date = "+pub_date+", author = "+author+", comments = "+comments;
		}
	}

	private static class BlogpostData {
//		public blogpost[] blogposts; // Many posts
		public blogpost dBlogpost;
	}

	private void loadAsyncCallGson() {

		final String TAG = "LoadAsyncCallGson";
		final String URL = "http://10.0.2.2:8000/blog/api/blogposts/3/";

		final OkHttpClient client = new OkHttpClient();
		final Gson gson = new Gson();

		String credential = Credentials.basic(USER, PASS);

		Request request = new Request.Builder()
				.url(URL)
				.header("Authorization", credential)
				.build();

		Log.i(TAG, "Async OkHttpClient Gson Connecting...");

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				Log.i(TAG, "Failed connection 4");
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, final Response response) throws IOException {
				// ... check for failure using `isSuccessful` before proceeding
				if (!response.isSuccessful()) {
					throw new IOException("Unexpected code " + response);
				}

				// Read data on the worker thread
				String data = "";
//				final String data = response.body().string();
				blogpost bp = gson.fromJson(response.body().charStream(), blogpost.class);
				data = data + "Blogpost Title: " + bp.getTitle() + "\n" +
						"Content: " + bp.getContent() + "\n" +
						"Datetime: " + bp.getDate()+"\n" +
						"Author: " + bp.getAuthor() + "\n";
				String comments = "";
				for (int i=0; i < bp.getComments().length; i++){
					Log.i(TAG, "Comments");
					comments = comments + "  " + bp.comments[i] +"\n";
				}
//				for(earthquake eq: eqdata.earthquakes) {
//					Log.i(TAG, eq.getDatetime());
//					Log.i(TAG, eq.toString());
//				}

				final String tdata = data + "Comments: " + "\n" + comments;
				Log.i(TAG, "Data: " + tdata);

				// Run view-related code back on the main thread
				NetworkingURLActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.i(TAG, "Setting data, main thread");
						mTextView.setText(tdata);
					}
				});
			}
		});
	}
}
