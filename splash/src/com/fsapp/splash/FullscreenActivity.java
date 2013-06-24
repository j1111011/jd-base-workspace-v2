package com.fsapp.splash;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fsapp.splash.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	Class<?> nextActivity = null;
	int delaySecs = 3;
	String updateAddress;
	String UriBase = getString(R.string.a1);
	String appId = getString(R.string.a2);
	String version = "1.0";
	String appName = "splash";
	String splashPath = Environment.getExternalStorageDirectory().toString() + "fsmob/" + appName + "/images/" + "splash.png";
	public void setDelaySecs(int delaySecs) {
		this.delaySecs = delaySecs;
	}
	public void setUpdateAddress(String updateAddress) {
		this.updateAddress = updateAddress;
	}
	public void setUriBase(String uriBase) {
		UriBase = uriBase;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public void setSplashPath(String splashPath) {
		this.splashPath = splashPath;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setmSystemUiHider(SystemUiHider mSystemUiHider) {
		this.mSystemUiHider = mSystemUiHider;
	}
	public void setmDelayHideTouchListener(
			View.OnTouchListener mDelayHideTouchListener) {
		this.mDelayHideTouchListener = mDelayHideTouchListener;
	}
	public void setmHideHandler(Handler mHideHandler) {
		this.mHideHandler = mHideHandler;
	}
	public void setmHideRunnable(Runnable mHideRunnable) {
		this.mHideRunnable = mHideRunnable;
	}


	String date = null;
	public void setNextActivity(Class<?> nextActivity) {
		this.nextActivity = nextActivity;
	}
	String generateUri(){
		updateAddress = new String();
		updateAddress = UriBase;
		updateAddress += "appid=";
		updateAddress += appId;
		updateAddress += "&version=";
		updateAddress += version;
		updateAddress += "&data=";
		updateAddress += date;
		return updateAddress;
	}
	
	void checkUpdate(){
		try {
			HttpURLConnection  urlconn = (HttpURLConnection) new URL(generateUri()).openConnection();
			InputStream in = new BufferedInputStream(urlconn.getInputStream());
			byte[] result = new byte[urlconn.getContentLength()];
			in.read(result);
			String tmp = new String(result);
			String[] array = tmp.split(";");
			doUpdateResponse(array);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void doUpdateResponse(String[] array) throws URISyntaxException, IOException {
		// TODO Auto-generated method stub
		for(String one:array){
			String[] vars = one.split("=");
				if(vars.length == 2){
					String cmd = vars[0];
					if(cmd.contains("status")){
						String status = vars[1];
						if(status == "newest"){
							break;
						}
						else if(status == "needupdate"){
							
						}
					}
					else if(cmd.contains("img")){						
						URL uri = new URL(vars[1]);
						HttpURLConnection  urlconn = (HttpURLConnection) uri.openConnection();
						InputStream in = new BufferedInputStream(urlconn.getInputStream());
						byte[] uPng = new byte[urlconn.getContentLength()];
						in.read(uPng);
						File png = new File(splashPath);
						if(!png.exists()){
							png.mkdirs();
							png.createNewFile();
						}
						FileOutputStream out = new FileOutputStream(png);
						out.write(uPng);
					}
					else if(cmd.contains("desc")){
						
					}
					else if(cmd.contains("date")){
						String date = vars[1];				
					}
				}
		}
		
	}
	void showNextActivity(){
		if(nextActivity != null){
			Timer timer = new Timer();
			TimerTask task = new TimerTask(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent = new Intent(FullscreenActivity.this,nextActivity);
					FullscreenActivity.this.startActivity(intent);
					
				}
				
			};
			timer.schedule(task, delaySecs*1000);
		}
	}


	/**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        File splash = new File(splashPath);
        if(splash.exists()){
        	((ImageView)contentView).setImageBitmap(BitmapFactory.decodeFile(splashPath));
        }
        else {
        	((ImageView)contentView).setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        }

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        
        new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				checkUpdate();
			}
        	
        }).start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
