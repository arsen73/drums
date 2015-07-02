package devapp.drump.helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;

import devapp.drump.R;

/**
 * Created by arseniy on 26/06/15.
 */
public class SoundUtil {

    public static SoundPool soundPool;

    public static int bassId;
    public static int hihtId;
    public static int snrId;
    public static int tom1Id;
    public static int tom2Id;
    public static int hiopId;
    public static int crashId;

    public static Boolean is_init = false;

    public static void initSound(Context context){
        soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);

        bassId = soundPool.load(context, R.raw.bass, 1);
        hihtId = soundPool.load(context, R.raw.hiht, 1);
        snrId = soundPool.load(context, R.raw.snr, 1);
        tom1Id = soundPool.load(context, R.raw.tom, 1);
        tom2Id = soundPool.load(context, R.raw.tom2, 1);
        hiopId = soundPool.load(context, R.raw.hiop, 1);
        crashId = soundPool.load(context, R.raw.crash, 1);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                SoundUtil.is_init = true;
            }
        });
    }

    public static void playNote(final int id){
        if(!is_init)
            return;
        AsyncTask<Void, Void, Void> at = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                SoundUtil.soundPool.play(id, 1, 1, 0, 0, 1);
            }
        };

        at.execute();
    }

}
