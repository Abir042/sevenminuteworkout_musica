package sevenminutes.workout;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.startapp.sdk.ads.splash.SplashConfig;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

public class Home extends Activity {

	SharedPreferences.Editor editor;
	SharedPreferences prefs;
	String MY_PREFS_NAME = "WORKOUT7";
	String SOUND = "SOUND";
	String advertise, ads;
	private ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	TextView txt_aboutus, txt_instruction, txt_moreapp, txt_rateus, txt_homestate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StartAppSDK.init(this,"210558411");
//		StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);

		StartAppAd.showSplash(this, savedInstanceState,
				new SplashConfig()
						.setTheme(SplashConfig.Theme.BLAZE)
		);

		setContentView(R.layout.activity_home);

		//setContentView(R.layout.activity_home);

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

		if (prefs.getString("advertise", null) != null) {
			ads = prefs.getString("advertise", null);

		} else {
			ads = "yes";
		}


//		if (ads.equals("yes")) {
//			if (getString(R.string.bannervisible).equals("yes")) {
//				AdView mAdView = (AdView) findViewById(R.id.adView);
//				AdRequest adRequest = new AdRequest.Builder().build();
//				mAdView.loadAd(adRequest);
//			} else if (getString(R.string.bannervisible).equals("no")) {
//				AdView mAdView = (AdView) findViewById(R.id.adView);
//				mAdView.setVisibility(View.GONE);
//			}
//		}else if (ads.equals("no")) {
//
//		}

//		final AdView mAdView = (AdView) findViewById(R.id.adView);
//		AdRequest adRequest = new AdRequest.Builder().build();
////		AdRequest adRequest = new AdRequest.Builder().addTestDevice("C413017DC8CDE1EC18AD67A86F7EE0CB").build();
//		mAdView.loadAd(adRequest);
//
//		mAdView.setAdListener(new AdListener() {
//			@Override
//			public void onAdLoaded() {
//				Log.e("Ad Loaded", "__________________");
//				super.onAdLoaded();
//			}
//
//			@Override
//			public void onAdOpened() {
//				Log.e("Ad Opened", "__________________");
//				super.onAdOpened();
//			}
//
//			@Override
//			public void onAdFailedToLoad(int errorCode) {
//				Log.e("Ad Failed", "__________________"+errorCode);
//				super.onAdFailedToLoad(errorCode);
//			}
//
//			@Override
//			public void onAdLeftApplication() {
//				super.onAdLeftApplication();
//			}
//
//			@Override
//			public void onAdClosed() {
//				Log.e("Ad Closed", "__________________");
//				super.onAdClosed();
//			}
//		});


		textviewinitialize();
		InitializeLayout();
	}

	private void textviewinitialize() {
		// TODO Auto-generated method stub

		Typeface tf = Typeface.createFromAsset(Home.this.getAssets(), "fonts/OpenSans-Regular.ttf");
		txt_aboutus = (TextView) findViewById(R.id.txt_about);
		txt_instruction = (TextView) findViewById(R.id.txt_instruction);
		txt_moreapp = (TextView) findViewById(R.id.txt_moreapp);
		txt_rateus = (TextView) findViewById(R.id.txt_rateus);
		txt_homestate = (TextView) findViewById(R.id.textView2);
		txt_homestate.setTypeface(tf);
		txt_aboutus.setTypeface(tf);
		txt_instruction.setTypeface(tf);
		txt_moreapp.setTypeface(tf);
		txt_rateus.setTypeface(tf);
	}


	/*@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		if (prefs.getString("advertise", null) != null) {
			ads = prefs.getString("advertise", null);

		} else {
			ads = "yes";
		}


		if (ads.equals("yes")) {
			if (getString(R.string.bannervisible).equals("yes")) {
				AdView mAdView = (AdView) findViewById(R.id.adView);
				AdRequest adRequest = new AdRequest.Builder().build();
				mAdView.loadAd(adRequest);
			} else if (getString(R.string.bannervisible).equals("no")) {
				AdView mAdView = (AdView) findViewById(R.id.adView);
				mAdView.setVisibility(View.GONE);
			}
		}else if (ads.equals("no")) {

		}
		super.onStart();

	}*/
	@Override
	protected void onResume() {
		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		if (prefs.getString("advertise", null) != null) {
			ads = prefs.getString("advertise", null);

		} else {
			ads = "yes";
		}


//		if (ads.equals("yes")) {
//			if (getString(R.string.bannervisible).equals("yes")) {
//				AdView mAdView = (AdView) findViewById(R.id.adView);
//				AdRequest adRequest = new AdRequest.Builder().build();
//				mAdView.loadAd(adRequest);
//			} else if (getString(R.string.bannervisible).equals("no")) {
//				AdView mAdView = (AdView) findViewById(R.id.adView);
//				mAdView.setVisibility(View.GONE);
//			}
//		}else if (ads.equals("no")) {
//			AdView mAdView = (AdView) findViewById(R.id.adView);
//			mAdView.setVisibility(View.GONE);
//		}
		editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
		prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		String SoundOnOff = prefs.getString(SOUND, null);

		if (SoundOnOff != null) {
			Log.d("SoundOnOff", SoundOnOff);

		} else {
			editor.putString(SOUND, "ON");
			editor.commit();
		}

		// main = new Main(Home.this);

		super.onResume();
	}

	private void InitializeLayout() {

		Button BtnStart = (Button) findViewById(R.id.btn_start);
		BtnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent iv = new Intent(Home.this, ExcerciseStart.class);
				startActivity(iv);
				StartAppAd.showAd(Home.this);
			}
		});

		LinearLayout ll_aboutus = (LinearLayout) findViewById(R.id.ll_aboutus);
		ll_aboutus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent iv = new Intent(Home.this, AboutUs.class);
				startActivity(iv);
			}
		});

		LinearLayout ll_Instruction = (LinearLayout) findViewById(R.id.ll_instruction);
		ll_Instruction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent iv = new Intent(Home.this, Instruction.class);
				startActivity(iv);
			}
		});

		LinearLayout ll_More = (LinearLayout) findViewById(R.id.ll_more);
		ll_More.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Uri uri = Uri.parse(getString(R.string.moreapplink));
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(goToMarket);
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse(getString(R.string.moreapplink))));
				}
			}
		});

		LinearLayout ll_Rate = (LinearLayout) findViewById(R.id.ll_rate);
		ll_Rate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Uri uri = Uri
						.parse("https://appgallery.huawei.com");
//								+ Home.this.getPackageName());

				Intent iv = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(iv);
			}
		});

		Button BtnSetting = (Button) findViewById(R.id.btn_home_setting);
		BtnSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent iv = new Intent(Home.this, Setting.class);
				startActivity(iv);
			}
		});
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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
}
