package devapp.drump.helpers;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by arseniy on 26/06/15.
 */
public class DisplayUtil {
    /**
     * Определение параметров экрана
     * @param act
     * @return
     */
    private static DisplayMetrics displaymetrics;

    private static void getMetrics(){
        if(displaymetrics == null)
            displaymetrics = new DisplayMetrics();
    }

    public static int getWinWith(Activity act){
        getMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static int getWinHeight(Activity act){
        getMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int getDpi(Activity act){
        getMetrics();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.densityDpi;
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
