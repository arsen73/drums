package devapp.drump.helpers;

import android.os.AsyncTask;

import java.util.Timer;
import java.util.TimerTask;

import devapp.drump.MainActivity;

/**
 * Created by arseniy on 05/07/15.
 */
public class PlayBackground extends AsyncTask<Void, Void, String> {

    private static long old_time = 0;

    @Override
    protected String doInBackground(Void... voids) {
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if(!CursorState.is_run){
                    timer.cancel();
                    timer.purge();
                    return;
                }
                MainActivity.count_col++;
                ArrayState.check(MainActivity.count_col);

//                long current = System.currentTimeMillis();
//                Log.d("WWWM", String.valueOf(current - old_time));
//                old_time = current;

                //SoundUtil.playNote(SoundUtil.bassId);
                if (MainActivity.count_col >= CursorState.cols) {
                    MainActivity.count_col = 0;
                    if (!CursorState.is_repeat) {
                        //CursorState.is_run = false;
                        timer.cancel();
                        timer.purge();
                    }
                }
            }
        }, 0, MainActivity.time);
        return null;
    }
}
