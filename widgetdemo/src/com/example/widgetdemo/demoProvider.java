package com.example.widgetdemo;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;


public class demoProvider extends AppWidgetProvider {

	private static final String CLICK_NAME_ACTION = "com.terry.action.widget.click";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, intent.toString(),Toast.LENGTH_LONG).show();
		
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	public static void updateAppWidget(Context context,
			AppWidgetManager appWidgeManger, int appWidgetId) {
		rv = new RemoteViews(context.getPackageName(), R.layout.activity_main);
		Intent intentClick = new Intent(CLICK_NAME_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intentClick, 0);
		rv.setOnClickPendingIntent(R.id.text01, pendingIntent);
		appWidgeManger.updateAppWidget(appWidgetId, rv);
	}
	private static RemoteViews rv = null;
}
