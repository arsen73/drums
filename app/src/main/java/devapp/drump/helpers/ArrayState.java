package devapp.drump.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Массив состояний клеток с нотами
 */
public class ArrayState {

    private static final int CRASH = 1;
    private static final int HIHT = 2;
    private static final int HIOP = 3;
    private static final int SNR = 4;
    private static final int TOM1 = 5;
    private static final int TOM2 = 6;
    private static final int BASS = 7;

    public static Map<Integer, Map<Integer, String>> states = new HashMap<>(); // состояния столбца

    /**
     * Проверка столбцов
     * @param x
     */
    public static void check(int x){
        Map<Integer, String> state = ArrayState.states.get(x);
        if(state != null)
        for(int y=1; y<=7; y++){
            String st = state.get(y);
            if(st != null && st.equals("check")) {
                final int yy = y;
                AsyncTask<Void, Void, Void> at = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        switch (yy){
                            case CRASH:
                                SoundUtil.playNote(SoundUtil.crashId);
                                break;
                            case HIHT:
                                SoundUtil.playNote(SoundUtil.hihtId);
                                break;
                            case HIOP:
                                SoundUtil.playNote(SoundUtil.hiopId);
                                break;
                            case SNR:
                                SoundUtil.playNote(SoundUtil.snrId);
                                break;
                            case TOM1:
                                SoundUtil.playNote(SoundUtil.tom1Id);
                                break;
                            case TOM2:
                                SoundUtil.playNote(SoundUtil.tom2Id);
                                break;
                            case BASS:
                                SoundUtil.playNote(SoundUtil.bassId);
                                break;
                        }
                        return null;
                    }
                };

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    at.execute();
            }
        }
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static void saveState(Context ctx){
        // сохранение текущей страницы
        Gson gson = new Gson();
        PreferenceHelper.save(ctx, String.valueOf(CursorState.current_page), gson.toJson(ArrayState.states));
    }
}
