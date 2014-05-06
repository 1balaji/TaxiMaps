package compucon.taxi.app.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.widget.Toast;

public class Hail implements Runnable {

	public interface Requestor {
		void OnHTTPResponse(Hail h, boolean reqOK, String response,
				String errorMsg);
	}

	String answer = "";
	String URL;
	Activity x;
	String callID;
	int call;
	boolean OK = false;
	String pos = "";
	Requestor _req = null;

	public Hail(String URL, final Activity x, Requestor req, int call) {
		this.URL = URL;
		this.x = x;
		this.call = call;
		_req = req;
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(URL);
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			switch (call) {
			case 1:
				String x = br.readLine();
				System.out.println(x);
				String error = "";
				if (x.equals("OK"))
					OK = true;
				else {
					while (br.readLine() != null) {
						error.concat(br.readLine());
					}
				}
				callID = br.readLine();
				_req.OnHTTPResponse(this, OK, callID, error);
				break;
			case 2:
				x = br.readLine();
				error = "";
				if (x.equals("OK"))
					OK = true;	
				else {
					while (br.readLine() != null) {
						error.concat(br.readLine());
					}
				}
				
				answer = br.readLine();
				_req.OnHTTPResponse(this, OK, answer, error);
				break;
			case 3:
				x = br.readLine();
				error = "";
				if (x.equals("OK")){
					OK = true;
					
				}
				else {
					while (br.readLine() != null) {
						error.concat(br.readLine());
					}
				}
				pos = br.readLine();
				_req.OnHTTPResponse(this, OK, pos, error);
				break;
			}

		} catch (Exception e) {
			display(e.toString());
			_req.OnHTTPResponse(this, false, "", e.getMessage());
		}

	}

	private void display(final String string) {
		x.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(x, string, Toast.LENGTH_LONG).show();
			}
		});
	}
}
