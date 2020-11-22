package sevenminutes.workout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExcerciseStart extends Activity {

	// Layout Views
	TextView txt_Excercise_name, txt_Next_excercise ,textview, txt_previous, txt_next;;
	ImageView ImgExcercise;
	TextView txt_progress_number, textview_temp1, txt_rest;
	ProgressBar pBar;
	Button Btn_Next, Btn_Prevuious, Btn_Continue, Btn_video;
	TextView txt_conti, txt_pause;
	String advertise, ads;
	int pStatus = 0, IS_REST = 0;
	private Handler handler = new Handler();

	int EXE_INDEX = 0, EXE_TOTAL = 12;
	Timer timer;
	TimerTask timerTask;

	int PEXECERCISE_TIME = 30;
	int PREST_TIME = 10;
	List<HashMap<String, String>> listResult1;
	RelativeLayout RL_Touch;

	// Shared Prefrences
	SharedPreferences prefs;
	String MY_PREFS_NAME = "WORKOUT7";
	String SOUND = "SOUND";
	String SoundOnOff;

	// Play SOund

	MediaPlayer Media_Go, Media_Stop;

//	InterstitialAd mInterstitialAd;
	boolean interstitialCanceled;
	AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.excercise_start);

		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

		if (prefs.getString("advertise", null) != null) {
			ads = prefs.getString("advertise", null);

		} else {
			ads = "yes";
		}

		if (ads.equals("yes")) {
			if (getString(R.string.insertialvisible).equals("yes")) {
				interstitialCanceled = false;
				CallNewInsertial();
			} else if (getString(R.string.insertialvisible).equals("no")) {

			}
		} else if (ads.equals("no")) {

		}

		Initialize();
		GetMusicPlayer();
		GetDataFromPlist();
		pBar.setMax(PEXECERCISE_TIME);

		SetExcercise(EXE_INDEX);
		pBar.setSecondaryProgress(PEXECERCISE_TIME);
	}

	private void GetMusicPlayer() {

		int resID = getResources().getIdentifier("go", "raw", getPackageName());
		Media_Go = MediaPlayer.create(ExcerciseStart.this, resID);

		int resID1 = getResources().getIdentifier("stop", "raw", getPackageName());
		Media_Stop = MediaPlayer.create(ExcerciseStart.this, resID1);
		// Media_Stop.prepare();

	}

	@Override
	protected void onPause() {
		stoptimertask();
//		mInterstitialAd = null;
		interstitialCanceled = true;
		super.onPause();
	}

	@Override
	protected void onResume() {

		prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		SoundOnOff = prefs.getString(SOUND, null);

		if (SoundOnOff != null) {
			Log.d("SoundOnOff", SoundOnOff);
		}
		super.onResume();
	}

	private void SetExcercise(int exe_index) {

		if (exe_index >= EXE_TOTAL) {
			Toast.makeText(ExcerciseStart.this, "Sorry, Next Excercise is not Available", Toast.LENGTH_LONG).show();
		} else if (exe_index < 0) {
			EXE_INDEX = 0;
			Toast.makeText(ExcerciseStart.this, "Sorry, Previus Excercise is not Available", Toast.LENGTH_LONG).show();
		} else {

			txt_rest.setVisibility(View.GONE);
			ImgExcercise.setVisibility(View.VISIBLE);
			txt_Excercise_name.setVisibility(View.VISIBLE);

			txt_Excercise_name.setText(listResult1.get(EXE_INDEX).get("Name"));
			txt_Next_excercise.setText(listResult1.get(EXE_INDEX + 1).get("Name"));
			ImgExcercise.setImageBitmap(getBitmapFromAsset(listResult1.get(EXE_INDEX).get("Image")));
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
		Bitmap bitmap = BitmapFactory.decodeStream(istr);
		return bitmap;
	}

	private void GetDataFromPlist() {

		ProductsPlistParsing Obj = new ProductsPlistParsing(ExcerciseStart.this);
		listResult1 = new ArrayList<HashMap<String, String>>();
		listResult1 = Obj.getProductsPlistValues();
		Log.d("LOG", "" + listResult1.get(0).get("Video URL"));
	}

	public void Initialize() {

//		txt_conti = (TextView) findViewById(R.id.txt_continuous);
//		txt_pause = (TextView) findViewById(R.id.txt_pause);
//		txt_pause.setVisibility(View.INVISIBLE);

		RL_Touch = (RelativeLayout) findViewById(R.id.RL_Touch);
		RL_Touch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (textview_temp1.getText().equals(getString(R.string.touchtopause))) {
					textview_temp1.setText(getString(R.string.touchtostart));
					Btn_Continue.setBackgroundResource(R.drawable.continue_icon);
//					txt_conti.setVisibility(View.VISIBLE);
//					txt_pause.setVisibility(View.INVISIBLE);
					stoptimertask();
				} else {

					startTimer();
					textview_temp1.setText(getString(R.string.touchtopause));
					Btn_Continue.setBackgroundResource(R.drawable.pause_icon);
//					txt_conti.setVisibility(View.INVISIBLE);
//					txt_pause.setVisibility(View.VISIBLE);
				}
			}
		});

		Btn_Next = (Button) findViewById(R.id.btn_next);
		Btn_Next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

//				stoptimertask();
				EXE_INDEX++;
				IS_REST = 0;
				pBar.setMax(PEXECERCISE_TIME);
				pStatus = 0;
				pBar.setProgress(pStatus);
				txt_progress_number.setText("" + pStatus);
				SetExcercise(EXE_INDEX);
				if (EXE_INDEX < EXE_TOTAL || EXE_INDEX > 0) {
					if (SoundOnOff != null) {
						if (SoundOnOff.equals("ON")) {
							Media_Go.start();
						}
					}
				}
//				startTimer();
			}
		});

		Btn_Prevuious = (Button) findViewById(R.id.btn_previous);
		Btn_Prevuious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

//				stoptimertask();
				EXE_INDEX--;
				IS_REST = 0;
				pBar.setMax(PEXECERCISE_TIME);
				pStatus = 0;
				pBar.setProgress(pStatus);
				txt_progress_number.setText("" + pStatus);
				SetExcercise(EXE_INDEX);
				if (EXE_INDEX < EXE_TOTAL || EXE_INDEX > 0) {
					if (SoundOnOff != null) {
						if (SoundOnOff.equals("ON")) {
							Media_Go.start();
						}
					}
				}
//				startTimer();
			}
		});

		Btn_Continue = (Button) findViewById(R.id.btn_continue);
		Btn_Continue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (textview_temp1.getText().equals(getString(R.string.touchtopause))) {
					textview_temp1.setText(getString(R.string.touchtostart));
					Btn_Continue.setBackgroundResource(R.drawable.continue_icon);
//					txt_conti.setVisibility(View.VISIBLE);
//					txt_pause.setVisibility(View.INVISIBLE);
					stoptimertask();
					// main = new Main(ExcerciseStart.this);
					/*
					 * if (main != null) startad(AdConfig.AdType.landing_page);
					 */

				} else {
//					txt_conti.setVisibility(View.INVISIBLE);
//					txt_pause.setVisibility(View.VISIBLE);
					startTimer();
					textview_temp1.setText(getString(R.string.touchtopause));
					Btn_Continue.setBackgroundResource(R.drawable.pause_icon);
				}
			}
		});

		Btn_video = (Button) findViewById(R.id.btn_video);
		Btn_video.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String EXE_Name, Video_URL, Copyright;
				EXE_Name = listResult1.get(EXE_INDEX).get("Name");
				Video_URL = listResult1.get(EXE_INDEX).get("Video URL");
				Copyright = listResult1.get(EXE_INDEX).get("Copyright");

				Log.d("EXE_Name", "" + EXE_Name);
				Log.d("EXE_Name", "" + Video_URL);
				Log.d("EXE_Name", "" + Copyright);

				Intent iv = new Intent(ExcerciseStart.this, PlayVideo.class);
				iv.putExtra("EXE_Name", listResult1.get(EXE_INDEX).get("Name"));
				iv.putExtra("Video_URL", listResult1.get(EXE_INDEX).get("Video URL"));
				iv.putExtra("Copyright", listResult1.get(EXE_INDEX).get("Copyright"));
				startActivity(iv);
			}
		});

		Button BtnSetting = (Button) findViewById(R.id.btn_exe_home);
		BtnSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent iv = new Intent(ExcerciseStart.this, Setting.class);
				startActivity(iv);
			}
		});

		txt_rest = (TextView) findViewById(R.id.txt_rest);
		textview_temp1 = (TextView) findViewById(R.id.textview_temp1);
		textview_temp1.setText(getString(R.string.touchtostart));
		txt_progress_number = (TextView) findViewById(R.id.txt_progress_number);
		pBar = (ProgressBar) findViewById(R.id.progressBar1);
		listResult1 = new ArrayList<HashMap<String, String>>();
		txt_Excercise_name = (TextView) findViewById(R.id.txt_execercise_name);
		txt_Next_excercise = (TextView) findViewById(R.id.txt_next_excercise);
		ImgExcercise = (ImageView) findViewById(R.id.img_excercise);
		textview = (TextView) findViewById(R.id.textView);
		txt_next = (TextView) findViewById(R.id.txt_next);
		txt_previous = (TextView) findViewById(R.id.txt_previous);
		Typeface tf = Typeface.createFromAsset(ExcerciseStart.this.getAssets(), "fonts/OpenSans-Regular.ttf");
		Typeface tf1 = Typeface.createFromAsset(ExcerciseStart.this.getAssets(), "fonts/OpenSans-Bold_0.ttf");
		txt_Excercise_name.setTypeface(tf1);
		textview_temp1.setTypeface(tf);
		txt_Next_excercise.setTypeface(tf);
		textview.setTypeface(tf1);
		txt_previous.setTypeface(tf);
		txt_next.setTypeface(tf);
//		txt_conti.setTypeface(tf);
//		txt_pause.setTypeface(tf);
	}

	public void startTimer() {

		if (EXE_INDEX == 0) {
			if (SoundOnOff != null) {
				if (SoundOnOff.equals("ON")) {
					Media_Go.start();
				}
			}
		}
		// set a new Timer
		timer = new Timer();

		// initialize the TimerTask's job
		initializeTimerTask();

		// schedule the timer, after the first 5000ms the TimerTask will run
		// every 10000ms
		timer.schedule(timerTask, 0, 1000); //
	}

	public void stoptimertask() {
		// stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public void initializeTimerTask() {

		timerTask = new TimerTask() {
			@Override
			public void run() {

				// use a handler to run a toast that shows the current timestamp
				handler.post(new Runnable() {
					@Override
					public void run() {

						if (EXE_INDEX == EXE_TOTAL) {
							// Excercise Complete
							ExcerciseComeplete();
						} else {

							if (IS_REST == 0) {
								if (pStatus <= PEXECERCISE_TIME) {
									pBar.setProgress(pStatus);
									txt_progress_number.setText("" + pStatus);
									pStatus++;
								} else {

									CallRestTimer();
								}
							} else {
								if (pStatus <= PREST_TIME) {
									pBar.setProgress(pStatus);
									txt_progress_number.setText("" + pStatus);
									pStatus++;
								} else {

									CallExcerciseTimer();
								}
							}
						}
					}

					private void CallExcerciseTimer() {
						IS_REST = 0;
						pBar.setMax(PEXECERCISE_TIME);
						pStatus = 0;
						pBar.setProgress(pStatus);
						txt_progress_number.setText("" + pStatus);
						SetExcercise(EXE_INDEX);
						if (SoundOnOff != null) {
							if (SoundOnOff.equals("ON")) {
								Media_Go.start();
							}
						}
					}

					private void CallRestTimer() {
						ImgExcercise.setVisibility(View.GONE);
						txt_Excercise_name.setVisibility(View.GONE);
						txt_rest.setVisibility(View.VISIBLE);
						IS_REST = 1;
						EXE_INDEX++;
						pBar.setMax(PREST_TIME);
						pStatus = 0;
						pBar.setProgress(pStatus);
						txt_progress_number.setText("" + pStatus);
						txt_Next_excercise.setText(listResult1.get(EXE_INDEX).get("Name"));
						if (SoundOnOff != null) {
							if (SoundOnOff.equals("ON")) {
								Media_Stop.start();
							}
						}
					}
				});
			}
		};
	}

	private void ExcerciseComeplete() {

		stoptimertask();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExcerciseStart.this);

		// Setting Dialog Title
		alertDialog.setTitle("7 Minute Workout");

		// Setting Dialog Message
		alertDialog.setMessage("You have successfully completed 7 min workout. Would you like to start again?");

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.jumping_jack);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("Yes, I Love to", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				EXE_INDEX = 0;
				IS_REST = 0;
				SetExcercise(EXE_INDEX);
				startTimer();
				Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
			}
		});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("No, I m tired", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				EXE_INDEX = 0;
				IS_REST = 0;
				textview_temp1.setText(getString(R.string.touchtostart));
				SetExcercise(EXE_INDEX);
				Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
			}
		});

		// Showing Alert Message
		alertDialog.show();
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
			XmlResourceParser parser = ExcerciseStart.this.getResources().getXml(R.xml.excerciselist);

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
						// start doccumnt nothing to do®r
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
							System.out.println("end tag");
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

//	@Override
//	protected void onDestroy() {
//		startActivity(new Intent(this,Home.class));
//		super.onDestroy();
//	}

	private void CallNewInsertial() {
		cd = new ConnectionDetector(ExcerciseStart.this);

		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(ExcerciseStart.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
//		 else {
			// AdView mAdView = (AdView) findViewById(R.id.adView);
			// AdRequest adRequest = new AdRequest.Builder().build();
			// mAdView.loadAd(adRequest);
//			Log.d("call", "call");
//
//			mInterstitialAd = new InterstitialAd(ExcerciseStart.this);
//			mInterstitialAd.setAdUnitId(getString(R.string.insertial_ad_key));
//			requestNewInterstitial();
//			mInterstitialAd.setAdListener(new AdListener() {
//				public void onAdClosed() {
//
//				}
//
//			});

//		}
	}

//	private void requestNewInterstitial() {
//		Log.d("request", "request");
//		final AdRequest adRequest = new AdRequest.Builder().build();
//		mInterstitialAd.loadAd(adRequest);
//
//	}

	/*
	 * @Override public void onPause() { mInterstitialAd = null;
	 * interstitialCanceled = true; super.onPause(); }
	 */

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

}
