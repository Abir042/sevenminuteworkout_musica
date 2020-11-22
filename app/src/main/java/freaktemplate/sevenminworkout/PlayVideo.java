package freaktemplate.sevenminworkout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class PlayVideo extends Activity {

    String EXE_Name, Video_URL, Copyright;
    TextView txt_name, txt_copyright;
    WebView webVideo;
    String advertise, ads;
    String MY_PREFS_NAME = "WORKOUT7";

    public static final int USER_MOBILE = 0;
    InterstitialAd mInterstitialAd;
    AlertDialogManager alert = new AlertDialogManager();
    private ConnectionDetector cd;

    private boolean isAdShow = false;

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
//			if (getString(R.string.insertialvisible).equals("yes")) {
//				interstitialCanceled = false;
//				CallNewInsertial();
//			} else if (getString(R.string.insertialvisible).equals("no")) {
//
//			}
		/*} else if (ads.equals("no")) {

		}*/

        Intent iv = getIntent();
        EXE_Name = iv.getStringExtra("EXE_Name");
        Video_URL = iv.getStringExtra("Video_URL");
        Copyright = iv.getStringExtra("Copyright");

        if (getIntent().getExtras() != null) {
            isAdShow = getIntent().getExtras().getBoolean("is_ad_show");
        }

        if (isAdShow) {
            loadInterstitialAd();
            Log.e("Ad show is ", "_________________");
        } else {

        }

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
    }

    private void loadInterstitialAd() {
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.insertial_ad_key));

        AdRequest adRequest = new AdRequest.Builder().build();
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("E998953AB23712D9A53268DC575BF907").build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
