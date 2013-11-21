package rosalila.studio.patadadecisiva2013;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appbucks.sdk.AppBucksAPI;

public class BootReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		AppBucksAPI.initialize(context, "<Application Name>", R.drawable.ic_launcher,-1, "<API Key>", false, null, null);
	}
}