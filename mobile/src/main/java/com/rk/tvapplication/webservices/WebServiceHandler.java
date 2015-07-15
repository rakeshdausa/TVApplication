package com.rk.tvapplication.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WebServiceHandler extends AsyncTask<Void, Void, String> {

    private Context context;
    private boolean progress = false;
    private ProgressDialog progressDialog = null;
    private WebServiceCallBackListener callBackListener;
    public  String base_url ="http://ganjaapps.altervista.org/forfinland.xml";
	private final String LOG_TAG = "WebServiceHandler";
	private RequestType reqType = RequestType.GET;
	private JSONObject queryData = new JSONObject();
	private HashMap<String, File> imgs;
	private Map<String, String> queryMap;
	public static enum RequestType {
		POST, GET ,MULTIPART
	};


	/**
	 *
	 * @return WebServiceHandler
	 */
	public static WebServiceHandler getInstanceWithProgress(Context context) {
		return new WebServiceHandler(context,true);
	}
    public static WebServiceHandler getInstance() {
        return new WebServiceHandler();
    }

    private WebServiceHandler(){}

    private WebServiceHandler(Context context, boolean progress){
        this.context = context;
        this.progress = progress;
    }
	/**
	 * 
	 * @param methodName
	 * @param callBackListener
	 * @param req
	 * @param queryData
	 */

	public void init(String methodName, WebServiceCallBackListener callBackListener,
			RequestType req, JSONObject queryData) {
		try {
			Log.i("info", "request data :: " + queryData.toString());
            this.callBackListener =callBackListener;
			this.reqType = req;
			this.queryData = queryData;
			base_url += methodName;
			Log.i("info","URL :: "+ base_url);
            this.execute();
		} catch (Exception e) {
			Log.i(LOG_TAG,"Exception in init WebService :: "+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}


	/**
	 *
	 * @param methodName
	 * @param callBackListener
	 * @param req
	 * @param queryData
     *
	 */
	public void init(String methodName,WebServiceCallBackListener callBackListener,
			RequestType req, String queryData) {
		try {
            this.callBackListener =callBackListener;
			this.reqType = req;
			if (queryData.contains("?") || "".equals(queryData)) {
				base_url += methodName + URLEncoder.encode(queryData, "utf-8");
			} else {
				base_url += methodName + "?" + URLEncoder.encode(queryData, "utf-8");
			}
			Log.i("info","URL :: "+ base_url);
			this.execute();
		} catch (Exception e) {
			Log.i(LOG_TAG,
					"Exception in init WebService :: "
							+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * @param methodName
	 * @param callBackListener
	 * @param req
	 * @param queryData
	 */
	public void init(String methodName, WebServiceCallBackListener callBackListener,
			RequestType req, Map<String,String> queryData) {
		try {
            this.callBackListener =callBackListener;
			this.reqType = req;
			base_url += methodName;
			int counter = 0;
			for (Object key : queryData.keySet()) {
				if (counter == 0) {
					base_url +=  "?"+key.toString()+"="+URLEncoder.encode(queryData.get(key), "utf-8");
				} else {
					base_url += "&"+key.toString()+"="+URLEncoder.encode(queryData.get(key), "utf-8");
				}
				counter++;
			}
			Log.i("info","URL :: "+ base_url);
			this.execute();
		} catch (Exception e) {
			Log.i(LOG_TAG,
					"Exception in init WebService :: "
							+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * @param methodName
	 * @param callBackListener
	 * @param req
	 * @param queryData
	 * @param imgs
	 */
	public void init(String methodName, WebServiceCallBackListener callBackListener,
			RequestType req, Map<String,String> queryData,HashMap<String, File> imgs) {
		try {
            this.callBackListener =callBackListener;
			this.reqType = req;
			this.queryMap = queryData;
			base_url += methodName;
			this.imgs = imgs;

			Log.i("info","URL :: "+ base_url);
			this.execute();
		} catch (Exception e) {
			Log.i(LOG_TAG,
					"Exception in init WebService :: "
							+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progress && context!=null){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
    }

    @Override
	protected String doInBackground(Void... arg0) {
		HttpClient httpclient = new DefaultHttpClient();
		switch (reqType) {
		case GET:
			/*try {
				HttpGet httpGet = new HttpGet(base_url);
                httpGet.setHeader("Accept", "application/json; charset=utf-8");
				HttpResponse httpResponse = httpclient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				httpclient.getConnectionManager().shutdown();

				return EntityUtils.toString(httpEntity);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			try {
				return getHttpConnection(base_url);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case POST:
			try {
				/*HttpPost httpPostRequest = new HttpPost(URL);
				StringEntity se = new StringEntity(queryData.toString());
				httpPostRequest.setEntity(se);
				httpPostRequest.setHeader("Accept", "application/json; charset=utf-8");
				httpPostRequest.setHeader("Content-type","application/json");*/
				URL url = new URL(base_url);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("POST");
//				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

				connection.setRequestProperty("charset", "utf-8");
//				connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
				connection.setUseCaches (false);

				DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
//				wr.writeBytes(otherParametersUrServiceNeed);
				wr.writeBytes(queryData.toString());

				wr.flush();
				wr.close();
				try {
					/*HttpResponse response = (HttpResponse) httpclient
							.execute(httpPostRequest);
					HttpEntity entity = response.getEntity();
					InputStream instream = entity.getContent();
					httpclient.getConnectionManager().shutdown();

					return convertStreamToString(instream);*/
					// checks server's status code first
					StringBuilder response = new StringBuilder();
					int status = connection.getResponseCode();
					if (status == HttpURLConnection.HTTP_OK) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(
								connection.getInputStream()));
						String line = null;
						while ((line = reader.readLine()) != null) {
							response.append(line);
						}
						reader.close();
						connection.disconnect();
					} else {
						throw new IOException("Server returned non-OK status: " + status);
					}

					return response.toString();


				} catch (ClientProtocolException e) {
					Log.i("info",
							"Excpetion in :: doInBackground :: "
									+ e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					Log.i("info",
							"Excpetion in :: doInBackground :: "
									+ e.getMessage());
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;


		case MULTIPART:
			try {
				MultipartUtility multipart = new MultipartUtility(base_url, "UTF-8");
				multipart.addHeaderField("Accept", "application/json");
				multipart.addHeaderField("Content-type","multipart/form-data");
				for (Object key : queryMap.keySet()) {
					multipart.addFormField(key.toString(), queryMap.get(key));   	
					try{
						Log.v(key.toString(), queryMap.get(key));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(imgs!=null)
					//multipart.addFilePart("fileUpload", file);
                for(String key:imgs.keySet()){
                    multipart.addFilePart(key, imgs.get(key));
                }

				return multipart.finish();
			}

			catch (ClientProtocolException e) {
				Log.i("info","Excpetion in :: doInBackground :: "+ e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.i("info","Excpetion in :: doInBackground :: "+ e.getMessage());
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		return null;
	}

	/**
	 * @param results
	 */
	protected void onPostExecute(String results) {
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
		//if (results != null) {
			try {
				Log.i("info", "response data :: " + results);
                if(this.callBackListener!=null){
                    this.callBackListener.onWebRequestDone(results);
                }
			} catch (IllegalArgumentException e) {
				Log.i("Info", "IllegalArgumentException");
				e.printStackTrace();
			}
		//}
	}

	/**
	 * 
	 * @param is
	 * @return
	 */
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

    public interface WebServiceCallBackListener{
        public void onWebRequestDone(String response);
//        public void onException(String message);
    }

    public static String readTestData(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }

	// Makes HttpURLConnection and returns InputStream
	private String getHttpConnection(String urlString)
			throws IOException {
		InputStream stream = null;
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		StringBuilder response = new StringBuilder();

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();

			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				stream = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
				httpConnection.disconnect();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response.toString();
	}
}
