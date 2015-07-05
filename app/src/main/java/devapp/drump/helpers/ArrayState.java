package devapp.drump.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arseniy on 25/06/15.
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
            if(st != null && st == "check") {
                switch (y){
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
            }


        }
    }
}
