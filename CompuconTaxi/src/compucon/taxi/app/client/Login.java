package compucon.taxi.app.client;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Login extends Activity{


	private WebView webView;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://192.168.1.28:81");
		webView.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		    	System.out.println(url);
		    	if(url.equals("http://0.0.0.1/"))
		    		//MainActivity.LoginActivityShow = true;
		    	if (url.equals("http://0.0.0.2/"))
		    		//MainActivity.ResgistrationActivityShow = true;
		    	
		        finish();
		        return false; // then it is not handled by default action
		   }
		});
	}
	@Override
	protected void onResume() {
		
		super.onResume();
	}
	
	
}
