package devapp.drump;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import devapp.drump.helpers.PlayBackground;

/**
 * Created by arseniy on 05/07/15.
 */
public class PlayService extends Service {
    private final String TAG = "PLAY";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "CREATE SERVICE");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DESTROY SERVICE");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand SERVICE");
        AsyncTask<Void, Void, String> task = new PlayBackground();
        //task.execute();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return super.onStartCommand(intent, flags, startId);
    }
}
