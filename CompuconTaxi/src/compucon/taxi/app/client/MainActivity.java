package compucon.taxi.app.client;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This shows how to create a simple activity with a map and a marker on the
 * map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is
 * not installed/enabled/updated on a user's device.
 */
public class MainActivity extends android.support.v4.app.FragmentActivity
		implements LocationListener, Hail.Requestor {
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */

	private Locale locale = null;
	private GoogleMap mMap;
	private Geocoder geocoder;
	private EditText searchEditText;
	private LocationManager locationManager;
	private String provider;
	Marker clickMarker = null, locationMarker = null, searchMarker = null;
	Marker taxiMarker1 = null, taxiMarker2 = null, taxiMarker3 = null,
			taxiMarker4 = null, taxiMarker5 = null;
	LatLng p, taxi_p = null;
	int PATH = R.drawable.taxi0;
	String fname = "", lname = "", city = "", address = "", address_num = "",
			municipality = "", number = "", lang = "";
	String taxiNum = "1";
	String taxiID = null;
	Button account, my_loc, hail, call, settings, cancel;
	String message = "...";
	String resp = "Error";
	String answer = "Error";
	String position = "Error";
	String xxx = "";
	int times = 1;
	double x = 0, y = 0;
	boolean same = false;
	double tempx = 0, tempy = 0;
	int trr = 0;

	ProgressDialog progressDialog = null;

	String array_spinner[];
	AlertDialog.Builder builder;
	AlertDialog alertDialog;
	AlertDialog.Builder ar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences getData = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		fname = getData.getString("fname", fname);
		lname = getData.getString("lname", lname);
		city = getData.getString("city", city);
		address = getData.getString("address", address);
		address_num = getData.getString("address_num", address_num);
		municipality = getData.getString("municipality", municipality);
		number = getData.getString("number", number);
		lang = getData.getString("lang", lang);
		String languageToLoad = "en";
		if (!lang.equals("")) {
			if (lang.equals("1"))
				languageToLoad = "el";
			if (lang.equals("2"))
				languageToLoad = "en";
		}
		
		if (fname.equals(""))
			startActivity(new Intent("compucon.taxi.app.Account"));
		
		xxx = "FAIL";
		
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, null);

		setContentView(R.layout.activity_main);
		// updateLanguage(MainActivity.this,lang);

		setUpMapIfNeeded();

		cancel = (Button) findViewById(R.id.cancel_btn);
		account = (Button) findViewById(R.id.account_btn);
		my_loc = (Button) findViewById(R.id.my_loc_btn);
		hail = (Button) findViewById(R.id.hail_btn);
		call = (Button) findViewById(R.id.call_btn);
		settings = (Button) findViewById(R.id.settings_btn);
		searchEditText = (EditText) findViewById(R.id.searchEditText);

		ar = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater inflater = MainActivity.this.getLayoutInflater();

		ar.setView(inflater.inflate(R.layout.dialog_arrived, null))
				.setPositiveButton(getResources().getString(R.string.THANKS),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		account.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					account.setBackgroundResource(R.drawable.account_on);
				if (event.getAction() == MotionEvent.ACTION_UP)
					account.setBackgroundResource(R.drawable.account);
				return false;
			}
		});

		my_loc.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					my_loc.setBackgroundResource(R.drawable.point_on);
				if (event.getAction() == MotionEvent.ACTION_UP)
					my_loc.setBackgroundResource(R.drawable.point);
				return false;
			}
		});

		hail.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					hail.setBackgroundResource(R.drawable.hail_on);
				if (event.getAction() == MotionEvent.ACTION_UP)
					hail.setBackgroundResource(R.drawable.hail);
				return false;
			}
		});

		call.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					call.setBackgroundResource(R.drawable.call_on);
				if (event.getAction() == MotionEvent.ACTION_UP)
					call.setBackgroundResource(R.drawable.call);
				return false;
			}
		});

		settings.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					settings.setBackgroundResource(R.drawable.settings_on);
				if (event.getAction() == MotionEvent.ACTION_UP)
					settings.setBackgroundResource(R.drawable.settings);
				return false;
			}
		});

		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				if (!(locationMarker == null))
					locationMarker.remove();

				if (!(searchMarker == null))
					searchMarker.remove();

				if (!(clickMarker == null)) {
					clickMarker.remove();
					clickMarker = null;
				} else {
					p = point;
					xxx = "FAIL";
					try {
						xxx = getAddressFromPoint(point, clickMarker);
					} catch (IOException e) {
						e.printStackTrace();
					}
					clickMarker = mMap.addMarker(new MarkerOptions()
							.title(xxx)
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
							.position(point).draggable(true));
				}

			}
		});

		mMap.setOnMarkerDragListener(new OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker marker) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				xxx = "FAIL";
				p = marker.getPosition();

				try {
					xxx = getAddressFromPoint(marker.getPosition(), clickMarker);
				} catch (IOException e) {
					e.printStackTrace();
				}
				marker.setTitle(xxx);
				marker.showInfoWindow();
			}

			@Override
			public void onMarkerDrag(Marker marker) {
				// TODO Auto-generated method stub

			}
		});
		//
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				searchEditText.setText("");
				CancelHail();

			}
		});
		//
		account.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent("compucon.taxi.app.Account"));

			}
		});

		settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent("compucon.taxi.app.Settings"));

			}
		});
		
		hail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("XXX = "+ xxx);
				if(!xxx.equals("FAIL")){
					PickTaxiNum();
					alertDialog.show();
				}

			}
		});

		call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				call.setBackgroundResource(R.drawable.call_on);
				try {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:000"));
					startActivity(callIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(MainActivity.this, "Error! " + e,
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		my_loc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!(clickMarker == null))
					clickMarker.remove();

				if (!(searchMarker == null))
					searchMarker.remove();

				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				provider = locationManager.getBestProvider(criteria, false);
				Location location = locationManager
						.getLastKnownLocation(provider);
				if (location != null) {
					System.out.println("Provider " + provider
							+ " has been selected.");
					onLocationChanged(location);
					xxx="x";
				}

			}
		});

		geocoder = new Geocoder(this.getApplicationContext());

		searchEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {

				if (arg1 == EditorInfo.IME_ACTION_GO) {
					searchByAddress(arg0.getText().toString());
				}
				return false;
			}
		});
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

				if (arg0.toString().length() != 0) {
					// TODO Hide keyboard & search results
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}
		});

	}

	protected void CancelHail() {

	}

	public String getAddressFromPoint(LatLng point, Marker marker)
			throws IOException {
		Geocoder geocoder;
		List<Address> addresses;
		geocoder = new Geocoder(this, Locale.getDefault());
		addresses = geocoder
				.getFromLocation(point.latitude, point.longitude, 1);

		String address = addresses.get(0).getAddressLine(0);
		String city = addresses.get(0).getAddressLine(1);
		String country = addresses.get(0).getAddressLine(2);
		return (address + " " + city + " " + country);
	}

	protected void display(String arg) {
		Toast.makeText(this, arg, Toast.LENGTH_SHORT).show();

	}

	protected void searchByAddress(String locationName) {

		try {
			List<Address> results = geocoder.getFromLocationName(locationName,
					10);
			if (results.size() > 0) {
				double t = -100;
				double b = -100;
				double l = -200;
				double r = -200;
				int padding = results.size() == 1 ? 200 : 50;
				for (Address add : results) {
					double x = add.getLongitude();
					double y = add.getLatitude();
					if (t < -90 || t < y)
						t = y;
					if (b < -90 || b > y)
						b = y;
					if (l < -180 || l > x)
						l = x;
					if (r < -180 || r < x)
						r = x;
					if (!(searchMarker == null))
						searchMarker.remove();

					if (!(clickMarker == null))
						clickMarker.remove();

					if (!(locationMarker == null))
						locationMarker.remove();

					searchMarker = mMap.addMarker(new MarkerOptions().position(
							new LatLng(add.getLatitude(), add.getLongitude()))
							.title(add.getFeatureName()));
					p = (new LatLng(add.getLatitude(), add.getLongitude()));
				}
				LatLng topLeft = new LatLng(b, l);
				LatLng bottomRight = new LatLng(t, r);
				LatLngBounds bounds = new LatLngBounds(topLeft, bottomRight);
				CameraUpdate upd = CameraUpdateFactory.newLatLngBounds(bounds,
						padding);

				mMap.animateCamera(upd);
				xxx = "x";
			}

		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play
	 * services APK is correctly installed) and the map has not already been
	 * instantiated.. This will ensure that we only ever call
	 * {@link #setUpMap()} once when {@link #mMap} is not null.
	 * <p>
	 * If it isn't installed {@link SupportMapFragment} (and
	 * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt
	 * for the user to install/update the Google Play services APK on their
	 * device.
	 * <p>
	 * A user can return to this Activity after following the prompt and
	 * correctly installing/updating/enabling the Google Play services. Since
	 * the Activity may not have been completely destroyed during this process
	 * (it is likely that it would only be stopped or paused),
	 * {@link #onCreate(Bundle)} may not be called again so we should call this
	 * method in {@link #onResume()} to guarantee that it will be called.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the
	 * camera. In this case, we just add a marker near Africa.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap}
	 * is not null.
	 */
	private void setUpMap() {
		mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(
				"Marker"));
		mMap.clear();
		mMap.setMyLocationEnabled(true);
	}

	@Override
	public void onLocationChanged(final Location location) {
		String xxx = "FAIL";
		try {
			xxx = getAddressFromPoint(new LatLng(location.getLatitude(),
					location.getLongitude()), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final AlertDialog.Builder al = new AlertDialog.Builder(
				MainActivity.this);

		al.setTitle(getResources().getString(R.string.ALERT_TITLE1));
		al.setMessage(getResources().getString(R.string.ALERT_MESSAGE1));
		al.setPositiveButton(getResources().getString(R.string.YES),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				});
		al.setNegativeButton(getResources().getString(R.string.NO),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(MainActivity.this,
								getResources().getString(R.string.TRY_AGAIN),
								Toast.LENGTH_SHORT).show();
					}
				});

		AlertDialog.Builder in = new AlertDialog.Builder(MainActivity.this);

		in.setTitle(getResources().getString(R.string.ALERT_TITLE2));
		in.setMessage(xxx);
		in.setPositiveButton(getResources().getString(R.string.YES),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(
								MainActivity.this,
								getResources().getString(R.string.ALERT_TOAST2),
								Toast.LENGTH_SHORT).show();
						p = new LatLng(location.getLatitude(), location
								.getLongitude());
						if (!(locationMarker == null)) {
							locationMarker.remove();
							locationMarker = mMap.addMarker(new MarkerOptions()
									.title(getResources().getString(
											R.string.MARKER_TITLE2))
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
									.position(
											new LatLng(location.getLatitude(),
													location.getLongitude()))
									.draggable(true));

						} else {
							locationMarker = mMap.addMarker(new MarkerOptions()
									.title(getResources().getString(
											R.string.MARKER_TITLE2))
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
									.position(
											new LatLng(location.getLatitude(),
													location.getLongitude()))
									.draggable(true));
						}

						CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(
								new LatLng(location.getLatitude(), location
										.getLongitude()), 17);
						mMap.animateCamera(upd);

					}
				});
		in.setNegativeButton(getResources().getString(R.string.NO),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						al.show();
					}
				});

		in.show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		display(provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void HailConfirm(final String x) {
		Hail second = new Hail(
				"http://ciakov.no-ip.org:8000/WebCallTaxiWS/GetTaxiPosition.aspx?tp=cr&cids="
						+ x + "&dm=t&txN=" + taxiNum, MainActivity.this,
				MainActivity.this, 2);
		second.start();
	}

	public void TaxiPosition(String ID) {
		double lang, lat;
		lat = p.latitude * 1E6;
		lang = p.longitude * 1E6;
		Hail third = new Hail(
				"http://ciakov.no-ip.org:8000/WebCallTaxiWS/GetTaxiPosition.aspx?tp=gp&taxi="
						+ ID + "&dm=t&posx=" + (int) lat + "&posy="
						+ (int) lang + "&dcc=" + times, MainActivity.this,
				MainActivity.this, 3);
		third.start();
		times++;

	}

	public void checkPosition() {
		Thread t = new Thread() {
			public void run() {
				try {
					sleep(2000);
					if (taxiID != null) {
						if ((x != 0) && (y != 0)) {
							if (!same) {
								TaxiPosition(taxiID);
							} else {
								MainActivity.this.runOnUiThread(new Runnable() {
									public void run() {
										ar.show();
										hail.setBackgroundResource(R.drawable.hail);
										hail.setEnabled(true);
										mMap.clear();
									}
								});
							}
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	@SuppressWarnings("deprecation")
	public void PickTaxiNum() {
		builder = new AlertDialog.Builder(MainActivity.this);
		Context mContext = MainActivity.this;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.spinner, null);

		array_spinner = new String[1];

		array_spinner[0] = "1";
		final Spinner spinner = (Spinner) layout.findViewById(R.id.Spinner01);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
				android.R.layout.simple_spinner_item, array_spinner);
		spinner.setAdapter(adapter);

		builder = new AlertDialog.Builder(mContext);
		builder.setView(layout);
		alertDialog = builder.create();
		alertDialog.setTitle(getResources().getString(R.string.ALERT_TITLE3));
		alertDialog.setButton(getResources().getString(R.string.OK),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						taxiNum = (spinner.getSelectedItem().toString().trim());
						double lang, lat;
						lat = p.latitude;
						lang = p.longitude;
						xxx = "FAIL";

						try {
							xxx = getAddressFromPoint((new LatLng(lat, lang)),
									null);
						} catch (Exception e) {
						}
						AlertDialog.Builder ad = new AlertDialog.Builder(
								MainActivity.this);

						ad.setTitle(getResources().getString(
								R.string.ALERT_TILTE4)
								+ " " + fname + " " + lname);
						ad.setMessage(getResources().getString(
								R.string.ALERT_MESSAGE4)
								+ " "
								+ taxiNum
								+ " "
								+ getResources().getString(
										R.string.ALERT_MESSAGE4_1)
								+ " ("
								+ xxx
								+ "). \n"
								+ getResources().getString(
										R.string.ALERT_MESSAGE4_2));
						ad.setPositiveButton(
								getResources().getString(R.string.YES),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										HailPost();
										progressDialog = new ProgressDialog(
												MainActivity.this);
										progressDialog
												.setMessage(getResources()
														.getString(
																R.string.PROGRESS_DIALOG));
										progressDialog
												.setIcon(R.drawable.ic_launcher);
										progressDialog.setCancelable(false);
										progressDialog.setIndeterminate(false);
										progressDialog.show();
										hail.setBackgroundResource(R.drawable.cancel_icon);
										hail.setEnabled(false);
									}
								});
						ad.setNegativeButton(
								getResources().getString(R.string.NO),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
						ad.show();

					}
				});
	}

	public void HailPost() {
		double lang, lat;
		lat = p.latitude * 1E6;
		lang = p.longitude * 1E6;
		final Hail first = new Hail(
				"http://ciakov.no-ip.org:8000/WebCallTaxiWS/GetTaxiPosition.aspx?tp=sc&cp="
						+ number + "&rnm=" + address + "&mn=" + city + "&rn="
						+ address_num + "&cm=&gx=" + (int) lat + "&gy="
						+ (int) lang + "&ir=f&ln=&vt=0&tn=" + taxiNum
						+ "&dn=&pn=" + fname + "_" + lname + "&pi=-1&bp=f",
				MainActivity.this, MainActivity.this, 1);
		first.start();

	}

	@Override
	public void OnHTTPResponse(Hail h, boolean reqOK, String response,
			String errorMsg) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (h.call == 1) {
			resp = response;
			HailConfirm(resp.trim());
		}
		if (h.call == 2) {
			if (response.trim().length() < 10) {
				HailConfirm(resp.trim());
			} else {
				answer = response;
				ShowConfirmation(answer.trim());
			}
		}
		if (h.call == 3) {
			position = response;
			if (response.length() < 15) {
				resp = response;
			} else {
				ShowPosition(position);
			}
		}
	}

	public void ShowPosition(final String pos) {

		
		Split2 sp = new Split2(pos);
		
		String x1 = sp.getAllXOrientations();
		String y1 = sp.getAllYOrientations();
		
		String xor[] = x1.split(",");
		String yor[] = y1.split(",");
		
		
		String temp[] = pos.split(";");
		
		taxiID = temp[0].trim();

		String geox = temp[1].trim();

		String geoy = temp[2].trim();

		String orient = temp[3].trim();
		
		String date = temp[4].trim();

		x = Double.parseDouble(geox);
		y = Double.parseDouble(geoy);
		
		System.out.println(x);
		System.out.println(y);

		taxi_p = new LatLng(x / 1E6, y / 1E6);

		if(getDistance(taxi_p.latitude, taxi_p.longitude,p.latitude,p.longitude)<20){
			same = true;
		}

		int or = Integer.parseInt(orient);
		if (or == 0)
			PATH = R.drawable.taxi0;
		else if (or == 45)
			PATH = R.drawable.taxi45;
		else if (or == 90)
			PATH = R.drawable.taxi90;
		else if (or == 135)
			PATH = R.drawable.taxi135;
		else if (or == 180)
			PATH = R.drawable.taxi180;
		else if (or == 225)
			PATH = R.drawable.taxi225;
		else if (or == 270)
			PATH = R.drawable.taxi270;
		else if (or == 315)
			PATH = R.drawable.taxi315;

		MainActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				if (taxiMarker1 != null) {
					taxiMarker1.remove();
					taxiMarker1 = mMap.addMarker(new MarkerOptions().icon(
							BitmapDescriptorFactory.fromResource(PATH))
							.position(new LatLng(x / 1E6, y / 1E6)));
				} else {
					taxiMarker1 = mMap.addMarker(new MarkerOptions().icon(
							BitmapDescriptorFactory.fromResource(PATH))
							.position(new LatLng(x / 1E6, y / 1E6)));
				}
			}
		});
		checkPosition();
	}

	public void ShowConfirmation(final String xx) {
		int tax = Integer.parseInt(taxiNum.trim());
		final String fulltime;
		final String message;
		final String callID;
		final String taxiID;
		final String secs;
		
		

		message = xx.substring(xx.indexOf("~") + 1, xx.length() - 1);
		String splitt[] = message.split("\\|");

		Split1 sp = new Split1();
		
		
		callID = splitt[0];
		taxiID = sp.getAllTaxiIDs(xx);
		

		
		int test = Integer.parseInt(splitt[3].trim());
		
		int hours = test / 3600, remainder = test % 3600, minutes = remainder / 60, seconds = remainder % 60;
		String mins = "", sec = "";
		if (minutes < 10)
			mins = "0" + minutes;
		else
			mins = minutes + "";
		if (seconds < 10)
			sec = "0" + seconds;
		else
			sec = seconds + "";
		fulltime = mins + ":" + sec + "secs";
		secs = splitt[3];

		MainActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder ad = new AlertDialog.Builder(
						MainActivity.this);
				ad.setTitle(getResources().getString(R.string.ALERT_TITLE5));
				ad.setMessage(getResources().getString(R.string.ALERT_MESSAGE5)
						+ " " + callID + "\n"
						+ getResources().getString(R.string.ALERT_MESSAGE5_1)
						+ " " + taxiID + "\n"
						+ getResources().getString(R.string.ALERT_MESSAGE5_2)
						+ " " + fulltime);
				ad.setPositiveButton(getResources().getString(R.string.OK),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								TaxiPosition(taxiID);
							}
						});
				ad.show();
			}
		});
	}
	
	public static double getDistance(double lat1, double lon1, double lat2,
			double lon2) {
		double R = 6371; // km
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		System.out.println(R*c*1000);
		return R * c * 1000;
	}
}
