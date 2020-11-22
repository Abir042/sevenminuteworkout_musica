package freaktemplate.sevenminworkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import freaktemplate.sevenminworkout.util.ConstantValues;

public class Instruction extends Activity {

//	RelativeLayout RL_7min, RL_Exe, RL_7min_layout, RL_Exe_layout;
	RelativeLayout RL_Exe_layout;
	LinearLayout ll_scroll;
	private LayoutInflater inflater = null;
	List<HashMap<String, String>> listResult1;
	Bitmap bitmap;
	InterstitialAd mInterstitialAd;
	boolean interstitialCanceled;
	AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	String advertise, ads;
	String MY_PREFS_NAME = "WORKOUT7";
//	TextView textview2, textview3, textview4, textview5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.instruction);

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

//		if (prefs.getString("advertise", null) != null) {
//			ads = prefs.getString("advertise", null);
//
//		} else {
//			ads = "yes";
//		}
//
//		if (ads.equals("yes")) {
//			Log.d("","");
//			if (getString(R.string.bannervisible).equals("yes")) {
//				AdView mAdView = (AdView) findViewById(R.id.adView);
//				AdRequest adRequest = new AdRequest.Builder().build();
//				mAdView.loadAd(adRequest);
//				//AdView mAdView = (AdView) findViewById(R.id.adView);
//				//mAdView.setVisibility(View.GONE);
//			} else if (getString(R.string.bannervisible).equals("no")) {
//				AdView mAdView = (AdView) findViewById(R.id.adView);
//				mAdView.setVisibility(View.GONE);
//			}
//
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

		final AdView mAdView = (AdView) findViewById(R.id.adView);

		AdRequest adRequest = new AdRequest.Builder().build();
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("A1DF92FC2A47285B1E783C4DD4A17C49").build();
		mAdView.loadAd(adRequest);

		loadInterstitialAd();

		GetDataFromPlist();
		Initialize();
		setExcercise();
//		RL_Exe_layout.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		// main = new Main(Instruction.this);
		/*
		 * if (main != null) startad(AdConfig.AdType.overlay);
		 */
		super.onResume();
	}

	private void GetDataFromPlist() {

		ProductsPlistParsing Obj = new ProductsPlistParsing(Instruction.this);
		listResult1 = new ArrayList<HashMap<String, String>>();
		listResult1 = Obj.getProductsPlistValues();
		Log.d("LOG", "" + listResult1.get(0).get("Video URL"));
	}

	private void setExcercise() {

		for (int i = 0; i < 12; i++) {

			View v = inflater.inflate(R.layout.instruction_cell, null);
			Typeface tf = Typeface.createFromAsset(Instruction.this.getAssets(), "fonts/OpenSans-Regular.ttf");
			Typeface tf1 = Typeface.createFromAsset(Instruction.this.getAssets(), "fonts/OpenSans-Bold_0.ttf");
			ImageView img_exe = (ImageView) v.findViewById(R.id.img_instruction_exe1);
			TextView txt_exe = (TextView) v.findViewById(R.id.txt_instruction1);
			TextView txt_name = (TextView) v.findViewById(R.id.txt_instruction_name);
			txt_exe.setTypeface(tf);
			txt_name.setTypeface(tf1);
			txt_name.setText("" + (i + 1) + "/12 " + listResult1.get(i).get("Name"));
			txt_exe.setText(listResult1.get(i).get("Description"));
			img_exe.setImageBitmap(getBitmapFromAsset(listResult1.get(i).get("Image")));

			Button btn = (Button) v.findViewById(R.id.btn_instruction_video);
			btn.setTag(i);
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					ConstantValues.total_video_count++;

					int EXE_INDEX = (Integer) view.getTag();
					Intent iv = new Intent(Instruction.this, PlayVideo.class);
					iv.putExtra("EXE_Name", listResult1.get(EXE_INDEX).get("Name"));
					iv.putExtra("Video_URL", listResult1.get(EXE_INDEX).get("Video URL"));
					iv.putExtra("Copyright", listResult1.get(EXE_INDEX).get("Copyright"));

					if(ConstantValues.total_video_count % ConstantValues.VIDEO_PLAY_COUNT == 0) {
						iv.putExtra("is_ad_show", true);
						ConstantValues.total_video_count = 0;
					} else {
						iv.putExtra("is_ad_show", false);
					}

					startActivity(iv);
				}
			});
			ll_scroll.addView(v);
		}
	}

	private Bitmap getBitmapFromAsset(String strName) {
		AssetManager assetManager = getAssets();
		InputStream istr = null;
		try {
			istr = assetManager.open(strName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bitmap = BitmapFactory.decodeStream(istr);
		return bitmap;
	}

	private void Initialize() {
//		textview2 = (TextView) findViewById(R.id.textView2);
//		textview3 = (TextView) findViewById(R.id.textView3);
//		textview4 = (TextView) findViewById(R.id.textView4);
//		textview5 = (TextView) findViewById(R.id.textView5);
//
//		Typeface tf = Typeface.createFromAsset(Instruction.this.getAssets(), "fonts/OpenSans-Regular.ttf");
//		textview2.setTypeface(tf);
//		textview3.setTypeface(tf);
//		textview4.setTypeface(tf);
//		textview5.setTypeface(tf);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		RL_7min_layout = (RelativeLayout) findViewById(R.id.RL_7min_layout);
		RL_Exe_layout = (RelativeLayout) findViewById(R.id.RL_EXE_layout);

//		RL_7min = (RelativeLayout) findViewById(R.id.RL_SevenMinute);
//		RL_7min.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//
//				RL_7min.setBackgroundColor(getResources().getColor(R.color.blue));
//				RL_Exe.setBackgroundColor(getResources().getColor(R.color.orange));
//				RL_Exe_layout.setVisibility(View.GONE);
//				RL_7min_layout.setVisibility(View.VISIBLE);
//			}
//		});

//		RL_Exe = (RelativeLayout) findViewById(R.id.RL_EXE);
//		RL_Exe.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//
////				RL_7min.setBackgroundColor(getResources().getColor(R.color.orange));
//				RL_Exe.setBackgroundColor(getResources().getColor(R.color.blue));
//				RL_Exe_layout.setVisibility(View.VISIBLE);
////				RL_7min_layout.setVisibility(View.GONE);
//
//			}
//		});

		ll_scroll = (LinearLayout) findViewById(R.id.ll_scroll);
	}

	public class ProductsPlistParsing {
		Context context;

		// constructor for to get the context object from where you are using
		// this plist parsing
		public ProductsPlistParsing(Context ctx) {

			context = ctx;
		}

		public List<HashMap<String, String>> getProductsPlistValues() {

			// specifying the your plist file.And Xml ResourceParser is an event
			// type parser for more details Read android source
			XmlResourceParser parser = context.getResources().getXml(R.xml.excerciselist);

			boolean keytag = false;
			boolean valuetag = false;
			String keyStaring = null;
			String stringvalue = null;

			HashMap<String, String> hashmap = new HashMap<String, String>();
			List<HashMap<String, String>> listResult = new ArrayList<HashMap<String, String>>();

			int event;
			try {
				event = parser.getEventType();

				// repeting the loop at the end of the doccument

				while (event != XmlPullParser.END_DOCUMENT) {

					switch (event) {
					// use switch case than the if ,else statements
					case 0:
						// start doccumnt nothing to do

						break;
					case 1:
						// end doccument

						break;
					case 2:

						if (parser.getName().equals("key")) {
							keytag = true;
							valuetag = false;
						}
						if (parser.getName().equals("string")) {
							valuetag = true;
						}

						break;
					case 3:
						if (parser.getName().equals("dict")) {
							listResult.add(hashmap);
							hashmap = null;
							hashmap = new HashMap<String, String>();
						}
						break;
					case 4:
						if (keytag) {
							if (valuetag == false) {
								hashmap.put("value", parser.getText());
								keyStaring = parser.getText();
							}
						}
						if (valuetag && keytag) {
							stringvalue = parser.getText();
							hashmap.put(keyStaring, stringvalue);
							valuetag = false;
							keytag = false;
						}
						break;
					default:
						break;
					}
					event = parser.next();
				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return listResult;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
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
}
