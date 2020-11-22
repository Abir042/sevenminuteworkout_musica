package freaktemplate.sevenminworkout;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import freaktemplate.sevenminworkout.util.IabHelper;
import freaktemplate.sevenminworkout.util.IabResult;
import freaktemplate.sevenminworkout.util.Inventory;
import freaktemplate.sevenminworkout.util.Purchase;

public class Setting extends Activity {

	Button Btn_Sound;
	RelativeLayout RL_Sound, RL_Reminder, Rl_Removeads, Rl_Share;
	SharedPreferences.Editor editor;
	SharedPreferences prefs;
	String MY_PREFS_NAME = "WORKOUT7";
	String SOUND = "SOUND";
	String SoundOnOff;
	String advertise, ads;
	TextView ed_time, ed_date, txt_temp1, txt_temp2, txt_temp3, txt_temp4;
	Button Btn_Done, Btn_cancel;
	RelativeLayout RL_Reminder_layout;
	private DatePickerDialog fromDatePickerDialog;
	private TimePickerDialog fromTimePickerDialog;
	private SimpleDateFormat dateFormatter;

	IInAppBillingService mservice;
	ServiceConnection connection;

	// static final String ITEM_SKU = "redixbit.sevenminwork";
	static final String ITEM_SKU = "android.test.purchased";
	// ask
	// String inappid = "redixbit.gtacheats";
	IabHelper mHelper;

	int calHours, calMin, calSeconds, calDay, calMonth, calYear;

	InterstitialAd mInterstitialAd;
	AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		//getActionBar().hide();
		setContentView(R.layout.setting);
		Rl_Removeads = (RelativeLayout) findViewById(R.id.RL_Removeads);
		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

//		if (prefs.getString("advertise", null) != null) {
//			ads = prefs.getString("advertise", null);
//
//		} else {
//			ads = "yes";
//		}
//		if (ads.equals("yes")) {
//			if (getString(R.string.insertialvisible).equals("yes")) {
//				interstitialCanceled = false;
//				CallNewInsertial();
//			} else if (getString(R.string.insertialvisible).equals("no")) {
//
//			}
//		} else if (ads.equals("no")) {
//			Rl_Removeads.setVisibility(View.INVISIBLE);
//		}
		/*
		 * if (getString(R.string.insertialvisible).equals("yes")) {
		 * interstitialCanceled = false; CallNewInsertial(); } else if
		 * (getString(R.string.insertialvisible).equals("no")) {
		 * 
		 * }
		 */

		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

		editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

		loadInterstitialAd();
		Initialize();
		setReminderLayout();
		prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		SoundOnOff = prefs.getString(SOUND, null);

		if (SoundOnOff != null) {
			if (SoundOnOff.equals("ON")) {
				Btn_Sound.setBackgroundResource(R.drawable.on_buttons);

			} else {
				Btn_Sound.setBackgroundResource(R.drawable.off_buttons);
			}
		}
	}

	private void setReminderLayout() {

		// Reminder

		RL_Reminder_layout = (RelativeLayout) findViewById(R.id.RL_Reminder_layout);
		Btn_Done = (Button) findViewById(R.id.btn_save);
		Btn_Done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setReminder();
				RL_Reminder_layout.setVisibility(View.GONE);
			}
		});
		Btn_cancel = (Button) findViewById(R.id.btn_cancel);
		Btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				RL_Reminder_layout.setVisibility(View.GONE);
			}
		});

		ed_date = (TextView) findViewById(R.id.ed_date);
		ed_date.setClickable(true);

		ed_time = (TextView) findViewById(R.id.ed_time);
		ed_time.setClickable(true);
		Typeface tf = Typeface.createFromAsset(Setting.this.getAssets(), "fonts/OpenSans-Regular.ttf");
		txt_temp1 = (TextView) findViewById(R.id.txt_temp1);
		txt_temp2 = (TextView) findViewById(R.id.txt_temp2);
		txt_temp3 = (TextView) findViewById(R.id.txt_temp3);
		txt_temp4 = (TextView) findViewById(R.id.txt_temp4);
		ed_date.setTypeface(tf);
		ed_time.setTypeface(tf);
		txt_temp1.setTypeface(tf);
		txt_temp2.setTypeface(tf);
		txt_temp3.setTypeface(tf);
		txt_temp4.setTypeface(tf);

		Calendar newDate = Calendar.getInstance();
		calMonth = newDate.get(Calendar.MONTH);
		calDay = newDate.get(Calendar.DAY_OF_MONTH);
		;
		calYear = newDate.get(Calendar.YEAR);
		;

		ed_date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fromDatePickerDialog.show();
			}
		});

		ed_time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fromTimePickerDialog.show();
			}
		});

		Calendar newCalendar = Calendar.getInstance();
		fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				ed_date.setText(dateFormatter.format(newDate.getTime()));
				calMonth = monthOfYear;
				calDay = dayOfMonth;
				calYear = year;
			}

		}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

		int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = newCalendar.get(Calendar.MINUTE);
		fromTimePickerDialog = new TimePickerDialog(Setting.this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
				ed_time.setText(selectedHour + ":" + selectedMinute);
				calHours = selectedHour;
				calMin = selectedMinute;
			}
		}, hour, minute, true);

		ed_date.setText(dateFormatter.format(newCalendar.getTime()));
		ed_time.setText(hour + ":" + minute);

		RL_Reminder_layout.setVisibility(View.GONE);
	}

	private void setReminder() {

		Log.d("LOG", calHours + " " + calMin);
		Log.d("LOG", calDay + " " + calMonth + " " + calYear);

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.MONTH, calMonth);
		calendar.set(Calendar.YEAR, calYear);
		calendar.set(Calendar.DAY_OF_MONTH, calDay);

		calendar.set(Calendar.HOUR_OF_DAY, calHours);
		calendar.set(Calendar.MINUTE, calMin);
		calendar.set(Calendar.SECOND, 0);
		if (calHours > 12) {
			calendar.set(Calendar.AM_PM, Calendar.PM);
		} else {
			calendar.set(Calendar.AM_PM, Calendar.AM);
		}

		Intent myIntent = new Intent(Setting.this, MyReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(Setting.this, 0, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

		Toast.makeText(Setting.this, "REMIDER SET", Toast.LENGTH_LONG).show();
	}

	private void Initialize() {
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3WrlQhwYjMMjs8/LJA6pFq8/PxI2MeZHY+xev9rR+x+gydcyTi7SQFAG15s/EcZ0JkFqUeMzjL5EvAASdQPibJ9SYDxQWM+LJesiP2Lqx5hpsmETlAzK3Jn/XtZt2EBluVntKJCIyjCuIR3m9JZ/WAWPUinsZv5P3bwvW1Y3MQaegixqryCKWDpNEtkOdg9YPAyzAUNLyGCN9vBKagCG4Zc0qIG0KfzsDhMach73JWQSz3mu7YkUvwXzvATc6QILzGj2CUFKmBX4YfgPz+J0RkXbcKC5petOI88nexmDX+wiyWst2nc6f3mk2GBv4/uhT90RVCBmRPJkw5D/oP2c3QIDAQAB";

		mHelper = new IabHelper(this, base64EncodedPublicKey);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					Log.d("tag", "In-app Billing setup failed: " + result);
				} else {

					Log.d("tag", "In-app Billing is set up OK");
				}

			}
		});
		Btn_Sound = (Button) findViewById(R.id.btn_sound_switch);
		Btn_Sound.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SetPrefrences();
			}
		});

		RL_Sound = (RelativeLayout) findViewById(R.id.RL_Sound);
		RL_Sound.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SetPrefrences();
			}
		});

		RL_Reminder = (RelativeLayout) findViewById(R.id.RL_Reminder);
		RL_Reminder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RL_Reminder_layout.setVisibility(View.VISIBLE);
			}
		});

		Rl_Share = (RelativeLayout) findViewById(R.id.RL_Share);
		Rl_Share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent share = new Intent(android.content.Intent.ACTION_SEND);
				share.setType("text/plain");

				share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				share.putExtra(Intent.EXTRA_SUBJECT, "7 Minute Workout");

				share.putExtra(Intent.EXTRA_TEXT,
						"https://play.google.com/store/apps/details?id=" + Setting.this
								.getPackageName() + "\n"
								+ "Iím not telling you itís going to be easy, Iím telling you itís going to be worth it.");
				startActivity(Intent.createChooser(share, "Share Link!"));
			}
		});

		Rl_Removeads.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mHelper.launchPurchaseFlow(Setting.this, ITEM_SKU, 10001, mPurchaseFinishedListener, "mypurchasetoken");
			}
		});
	}

	private void SetPrefrences() {
		prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		String SoundOnOff = prefs.getString(SOUND, null);
		Log.d("SoundOnOff", SoundOnOff);
		if (SoundOnOff != null) {
			if (SoundOnOff.equals("ON")) {
				Btn_Sound.setBackgroundResource(R.drawable.off_buttons);
				editor.putString(SOUND, "OFF");
				editor.commit();
			} else {
				Btn_Sound.setBackgroundResource(R.drawable.on_buttons);
				editor.putString(SOUND, "ON");
				editor.commit();
			}
		}
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
		/*
		 * Intent iv = new Intent(Setting.this, Home.class); startActivity(iv);
		 */
		Log.d("advertise", "" + advertise);
		SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
		editor.putString("advertise", "" + advertise);

		editor.commit();
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				// Handle error
				return;
			} else if (purchase.getSku().equals(ITEM_SKU)) {
				consumeItem();
				Rl_Removeads.setEnabled(false);

			}

		}
	};

	public void consumeItem() {
		mHelper.queryInventoryAsync(mReceivedInventoryListener);
	}

	IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

			if (result.isFailure()) {
				// Handle failure
			} else {
				mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU), mConsumeFinishedListener);
			}
		}
	};

	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {

			if (result.isSuccess()) {
				Rl_Removeads.setVisibility(View.INVISIBLE);
				advertise = "no";
				SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
				editor.putString("advertise", "" + advertise);

				editor.commit();

				// btn_remove.setEnabled(true);
			} else {
				// handle error
				advertise = "yes";
				SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
				editor.putString("advertise", "" + advertise);

				editor.commit();
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null)
			mHelper.dispose();

		mHelper = null;

	}
}
