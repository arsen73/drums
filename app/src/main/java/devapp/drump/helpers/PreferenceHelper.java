package devapp.drump.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Работа с preference
 */
public class PreferenceHelper {
    /**
     * Сохранение настройки
     * @param ctx
     * @param pref
     * @param val
     */
    public static void save(Context ctx, String pref, String val){
        SharedPreferences sPref =  ctx.getSharedPreferences("SettingsApp", ctx.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(pref, val);
        ed.commit();
    }

    /**
     * Получение настройки
     * @param ctx
     * @param pref
     * @param default_val
     * @return
     */
    public static String get(Context ctx, String pref, String default_val){
        SharedPreferences sPref =  ctx.getSharedPreferences("SettingsApp", ctx.MODE_PRIVATE);
        return sPref.getString(pref, default_val);
    }
}
