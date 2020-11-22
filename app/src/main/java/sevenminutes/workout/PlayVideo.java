package sevenminutes.workout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;

public class PlayVideo extends Activity {

	String EXE_Name, Video_URL, Copyright;
	TextView txt_name, txt_copyright;
	WebView webVideo;
	String advertise, ads;
	String MY_PREFS_NAME = "WORKOUT7";

	public static final int USER_MOBILE = 0;
//	InterstitialAd mInterstitialAd;
	boolean interstitialCanceled;
	AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.playvideo);

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

		/*if (prefs.getString("advertise", null) != null) {
			ads = prefs.getString("advertise", null);

		} else {
			ads = "yes";
		}*/

		//if (ads.equals("yes")) {
			if (getString(R.string.insertialvisible).equals("yes")) {
				interstitialCanceled = false;
				CallNewInsertial();
			} else if (getString(R.string.insertialvisible).equals("no")) {

			}
		/*} else if (ads.equals("no")) {

		}*/

		Intent iv = getIntent();
		EXE_Name = iv.getStringExtra("EXE_Name");
		Video_URL = iv.getStringExtra("Video_URL");
		Copyright = iv.getStringExtra("Copyright");

		Log.d("EXE_Name", "" + EXE_Name + " " + Video_URL + " " + Copyright);
		Initialize();
		embadedVideo();
	}

	private void embadedVideo() {

		webVideo.getSettings().setJavaScriptEnabled(true);
		webVideo.getSettings().setPluginState(WebSettings.PluginState.ON);
		webVideo.getSettings().setUserAgentString(String.valueOf(USER_MOBILE));
		webVideo.setWebChromeClient(new WebChromeClient() {
		});

		final String mimeType = "text/html";
		final String encoding = "UTF-8";
		String html = getHTML();
		webVideo.loadDataWithBaseURL("", html, mimeType, encoding, "");
	}

	public String getHTML() {

		String html = "<iframe class=\"youtube-player\" " + "style=\"border: 0; width: 100%; height: 95%;"
				+ "padding:0px; margin:0px\" " + "id=\"ytplayer\" type=\"text/html\" " + "src=" + Video_URL
				+ "?fs=0\" frameborder=\"0\" " + "allowfullscreen autobuffer " + "controls onclick=\"this.play()\">\n"
				+ "</iframe>\n";

		return html;
	}

	private void Initialize() {

		txt_name = (TextView) findViewById(R.id.txt_exe_name);
		txt_name.setText(EXE_Name);

		txt_copyright = (TextView) findViewById(R.id.txt_copyright_name);
		txt_copyright.setText(Copyright);

		webVideo = (WebView) findViewById(R.id.web_video);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// Your code to show add
//
//				if (interstitialCanceled) {
//
//				} else {
//
//					if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
//						mInterstitialAd.show();
//
//					} else {
//
//						// ContinueIntent();
//					}
//				}
//
//			}
//		}, 5000);
	}

	private void CallNewInsertial() {
		cd = new ConnectionDetector(PlayVideo.this);

		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(PlayVideo.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		} else {
			// AdView mAdView = (AdView) findViewById(R.id.adView);
			// AdRequest adRequest = new AdRequest.Builder().build();
			// mAdView.loadAd(adRequest);
			Log.d("call", "call");

//			mInterstitialAd = new InterstitialAd(PlayVideo.this);
//			mInterstitialAd.setAdUnitId(getString(R.string.insertial_ad_key));
//			requestNewInterstitial();
//			mInterstitialAd.setAdListener(new AdListener() {
//				@Override
//				public void onAdClosed() {
//
//				}
//
//			});

		}
	}

//	private void requestNewInterstitial() {
//		Log.d("request", "request");
//		final AdRequest adRequest = new AdRequest.Builder().build();
//		mInterstitialAd.loadAd(adRequest);
//
//	}

	@Override
	public void onPause() {
//		mInterstitialAd = null;
		interstitialCanceled = true;
		webVideo.onPause();
		webVideo.pauseTimers();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		webVideo.destroy();
		webVideo = null;
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		webVideo.resumeTimers();
		webVideo.onResume();
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this,ExcerciseStart.class));
		finish();
	}

}
