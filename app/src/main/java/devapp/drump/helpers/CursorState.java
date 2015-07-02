package devapp.drump.helpers;

import android.app.Activity;

/**
 * Created by arseniy on 25/06/15.
 */
public class CursorState {
    public static Boolean is_run = false; // состояние анимации
    public static final int speedInterval = 100; // интервал изменения скорости
    public static int speed = 20000; //скорость
    public static float cursorPosition = 0; // Позиция курсора
    public static float animValue = 0; // значение анимации
    public static Boolean is_pause = false; //постановка на паузу

    public static int displayWith = 0; // ширина экрана
    public static int displayHeight = 0; //высота экрана
    public static int placeWith = 0; // ширина поля

    public static int col_width = 0; //ширина ячейки

    public static int cols = 12;

    public static Boolean is_repeat = false;

    public static void init(Activity act){
        displayHeight = DisplayUtil.getWinHeight(act);
        displayWith = DisplayUtil.getWinWith(act);
        col_width = (int)((displayHeight*0.7)/8); // размеры поля и колличество строк
        placeWith = col_width*cols;
    }

    public static void setCol(int cols_new){
        cols = cols_new;
        placeWith = col_width*cols;
    }

}
