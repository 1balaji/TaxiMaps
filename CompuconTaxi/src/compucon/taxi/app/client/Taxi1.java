package compucon.taxi.app.client;

import com.google.android.gms.maps.GoogleMap;

public class Taxi1 implements Runnable{
	public interface TaxiArrived {
		void OnArrived(String msg);
	}
	
	TaxiArrived ta = null;
	GoogleMap mMap;
	
	public Taxi1(TaxiArrived ta,GoogleMap mMap){
		this.ta = ta;
		this.mMap = mMap;
	}
	
	@Override
	public void run() {
		
	}
}
