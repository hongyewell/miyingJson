package com.getjson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class JsonActivity extends Activity {
	private TextView tx;
	
	Handler hd = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
				case 0: //子线程访问网络完成后更新UI
					Bundle getbd = msg.getData();
					String getRS = getbd.getString("RS");
//					String getRS = (String) msg.obj;
					tx = (TextView) findViewById(R.id.result);
					tx.setText(getRS);
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json);
		
		new Thread(new Runnable() {			
			@Override
			public void run() {						
						String result = "";
						Connect con = new Connect();
						result=con.sendGET();
						Message msg = new Message();
						msg.what=0;
						Bundle bd = new Bundle();
						bd.putString("RS", result);
						msg.setData(bd);
//						msg.obj = result;
						hd.sendMessage(msg);
			}
		}).start();
	}
	
}

//访问网络的工具类
class Connect {	
	
	//方法1
	public String sendGET(){
		//boolean pan=false;
		String res="";		
		try{
			String url = "http://miying.sinaapp.com/menu/";
			HttpGet request = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);			
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String result= EntityUtils.toString(response.getEntity());
				//Log.i("Title", result);	
				JSONObject jsonobj = new JSONObject(result);
				JSONObject jsonobj2 = jsonobj.getJSONObject("AnHui");
				JSONArray jay = jsonobj2.getJSONArray("BengBu");
			//	Log.i("Title", "result:["+jsonobj.getString("AnHui")+"]");	
				Log.i("Title", "result:["+jay.get(1)+"]");	
				res = jay.getString(1);
//				res=jsonobj.getString("BookName");
				//pan = true;						
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
}

