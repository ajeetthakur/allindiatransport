package com.tpnagar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

public class AppController extends MultiDexApplication {
	private static ProgressDialog dialog=null;
	public static final String TAG = AppController.class
			.getSimpleName();
	private static GoogleAnalytics sAnalytics;
	private static Tracker sTracker;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static AppController mInstance;
	public static boolean isPageone = false;
	public static String fontPathbold = "fonts/Lato-Bold.ttf";
	public static String fontPathbolditalic = "fonts/Lato-BoldItalic.ttf";
	public static String fontPathitalic = "fonts/Lato-Italic.ttf";
	public static String fontPathlight = "fonts/Lato-Light.ttf";
	public static String fontPathlightitalic = "fonts/Lato-LightItalic.ttf";
	public static String fontPathregular = "fonts/Lato-Regular.ttf";

	public static String BrokerServiceName = "";
	public static String BrokerStateTo = "";
	public static String BrokerStateFrom = "";
	public static String ToComefromEdit = "No";
	public static String PrimaryMobile = "";
	public static String FromCity = "";
	public static String ToCity = "";
	public static String Service_Id ="";
	public static String FromCityId ="";
	public static String ToCityId = "";
	public static String spinnerId ="";
	public static String ToSateName = "";
	public static String ToSateId = "";

	public static String FromSateName = "";
	public static String FromSateId = "";
	public static String SearchText = "";

	public static int IMAGEPICKER=100;
	public static int POS =0;



	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		AnalyticsTrackers.initialize(this);
		AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public synchronized Tracker getGoogleAnalyticsTracker() {
		AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
		return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
	}

	/***
	 * Tracking screen view
	 *
	 * @param screenName screen name to be displayed on GA dashboard
	 */
	public void trackScreenView(String screenName) {
		Tracker t = getGoogleAnalyticsTracker();

		// Set screen name.
		t.setScreenName(screenName);

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		GoogleAnalytics.getInstance(this).dispatchLocalHits();
	}

	/***
	 * Tracking exception
	 *
	 * @param e exception to be tracked
	 */
	public void trackException(Exception e) {
		if (e != null) {
			Tracker t = getGoogleAnalyticsTracker();

			t.send(new HitBuilders.ExceptionBuilder()
					.setDescription(
							new StandardExceptionParser(this, null)
									.getDescription(Thread.currentThread().getName(), e))
					.setFatal(false)
					.build()
			);
		}
	}

	/***
	 * Tracking event
	 *
	 * @param category event category
	 * @param action   action of the event
	 * @param label    label
	 */
	public void trackEvent(String category, String action, String label) {
		Tracker t = getGoogleAnalyticsTracker();

		// Build and send an Event.
		t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
	}


	@Override
	protected void attachBaseContext(Context context) {
		super.attachBaseContext(context);
		MultiDex.install(this);
	}




	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public static void spinnerStart(Context context) {
		String pleaseWait = "please wait...";
		spinnerStop();
		dialog = ProgressDialog.show(context, "", pleaseWait, true);
	}

	public static void spinnerStop() {

		try {

			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void popErrorMsg(String titleMsg, String errorMsg,
								   Context context) {
		// pop error message
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleMsg).setMessage(errorMsg)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void ShowMassage(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();


	}
	public static void showConfirmDialog(Activity mContext, String Msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle("Message");
		builder.setMessage(Msg)
				.setCancelable(true)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}


	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}



	public static Typeface Externalbold(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPathbold);


		return tf;

	}

	public static Typeface Externalbolditalic(Context context) {

		Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPathbolditalic);


		return tf;

	}

	public static Typeface Externalitalic(Context context) {

		Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPathitalic);


		return tf;

	}

	public static Typeface Externallight(Context context) {

		Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPathlight);


		return tf;

	}

	public static Typeface Externallightitalic(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPathlightitalic);
		return tf;
	}

	public static Typeface Externalregular(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPathregular);
		return tf;
	}

	public static void showToast(final Activity context, final String msg) {
		try {
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isInternetPresent(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}


}
