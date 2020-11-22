package freaktemplate.sevenminworkout;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AboutUs extends Activity {
	InterstitialAd mInterstitialAd;
	boolean interstitialCanceled;
	AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	String advertise, ads;
	String MY_PREFS_NAME = "WORKOUT7";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aboutus);

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
//			if (getString(R.string.insertialvisible).equals("yes")) {
//				interstitialCanceled = false;
//				CallNewInsertial();
//			} else if (getString(R.string.insertialvisible).equals("no")) {
//
//			}
//		} else if (ads.equals("no")) {
//			AdView mAdView = (AdView) findViewById(R.id.adView);
//			mAdView.setVisibility(View.GONE);
//		}

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		mAdView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.e("Ad loaded", "_______________");
				super.onAdLoaded();
			}

			@Override
			public void onAdOpened() {
				Log.e("Ad Opened", "_______________");
				super.onAdOpened();
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				Log.e("Ad Failed", "_______________" + errorCode);
				super.onAdFailedToLoad(errorCode);
			}

			@Override
			public void onAdLeftApplication() {
				super.onAdLeftApplication();
			}

			@Override
			public void onAdClosed() {
				Log.e("Ad Closed", "_______________");
				super.onAdClosed();
			}
		});

		/*if (getString(R.string.insertialvisible).equals("yes")) {
			interstitialCanceled = false;
			CallNewInsertial();
		} else if (getString(R.string.insertialvisible).equals("no")) {

		}*/

	}

	@Override
	protected void onStart() {
		super.onStart();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// Your code to show add

				if (interstitialCanceled) {

				} else {

					if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
						mInterstitialAd.show();

					} else {

						// ContinueIntent();
					}
				}

			}
		}, 5000);
	}

	private void CallNewInsertial() {
		cd = new ConnectionDetector(AboutUs.this);

		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(AboutUs.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		} else {
			// AdView mAdView = (AdView) findViewById(R.id.adView);
			// AdRequest adRequest = new AdRequest.Builder().build();
			// mAdView.loadAd(adRequest);
			Log.d("call", "call");

			mInterstitialAd = new InterstitialAd(AboutUs.this);
			mInterstitialAd.setAdUnitId(getString(R.string.insertial_ad_key));
			requestNewInterstitial();
			mInterstitialAd.setAdListener(new AdListener() {
				@Override
				public void onAdClosed() {

				}

			});

		}
	}

	private void requestNewInterstitial() {
		Log.d("request", "request");
		final AdRequest adRequest = new AdRequest.Builder().build();
		mInterstitialAd.loadAd(adRequest);

	}

	@Override
	public void onPause() {
		mInterstitialAd = null;
		interstitialCanceled = true;
		super.onPause();
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}
}
