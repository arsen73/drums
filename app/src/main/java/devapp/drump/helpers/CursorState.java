package devapp.drump.helpers;

import android.app.Activity;

import devapp.drump.R;

/**
 * Created by arseniy on 25/06/15.
 */
public class CursorState {
    public static Boolean is_run = false; // состояние анимации
    public static final int speedInterval = 100; // интервал изменения скорости
    public static int speed = 20000; //скорость
    public static int cursorPosition = 0; // Позиция курсора
    public static int animValue = 0; // значение анимации
    public static Boolean is_pause = false; //постановка на паузу

    public static int displayWith = 0; // ширина экрана
    public static int displayHeight = 0; //высота экрана
    public static int placeWith = 0; // ширина поля

    public static int col_width = 0; //ширина ячейки

    public static int cols = 12;

    public static Boolean is_repeat = false;

    public static int default_speed = 80;

    public static long temp_time = 0;

    public static int current_page = R.id.page_1;

    public static void init(Activity act){
        displayHeight = DisplayUtil.getWinHeight(act);
        displayWith = DisplayUtil.getWinWith(act);
        col_width = (int)((displayHeight*0.7)/8); // размеры поля и колличество строк
        setCol(cols);
    }

    public static void setCol(int cols_new){
        cols = cols_new;
        placeWith = col_width*cols+(cols);
        speed = (int)((60f/(default_speed*4))*cols*1000);
    }

}
