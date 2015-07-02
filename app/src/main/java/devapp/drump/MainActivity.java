package devapp.drump;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import devapp.drump.helpers.ArrayState;
import devapp.drump.helpers.CursorState;
import devapp.drump.helpers.SoundUtil;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = "DRAM";
    private final int POSITION = 1;
    private final int STATE = 2;

    private final String STATE_CHECK = "check";
    private final String STATE_UNCHECK = "uncheck";

    private static int col = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initSound
        SoundUtil.initSound(this);

        // hide action bar
        if(Build.VERSION.SDK_INT < 16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

        // Display and cursor parametrs
        CursorState.init(this);

        // NumberPiker
        final NumberPicker np = (NumberPicker) findViewById(R.id.speed);
        String[] nums = new String[190];

        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i+20);
        np.setMaxValue(nums.length + 20);
        np.setMinValue(20);
        np.setValue(40);
        CursorState.speed = (int)((60f/(40*4))*CursorState.cols*1000);
        //np.setWrapSelectorWheel(false);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        NumberPicker.OnValueChangeListener numberPickerOnChangedListener = new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker numberPicker, int iOld, int iNew) {
                CursorState.speed = (int)((60f/(iNew*4))*CursorState.cols*1000);
                Log.d(LOG_TAG, String.valueOf(iNew));
                Log.d(LOG_TAG, String.valueOf(60f/(float)iNew));
            }
        };

        np.setOnValueChangedListener(numberPickerOnChangedListener);

        // Button Start/Stop
        final ImageView startButton = (ImageView) findViewById(R.id.start_play);
        // Cursor
        final ImageView cursor = (ImageView) findViewById(R.id.cursor);

        // Animation
        final ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setInterpolator(new LinearInterpolator());

        // Обработка движения курсора
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                // если поставлено на паузу пропускаем вызов
                if(CursorState.is_pause){
                    return;
                }

                CursorState.animValue = (Float) animation.getAnimatedValue();
                CursorState.cursorPosition = CursorState.placeWith*CursorState.animValue ;
                cursor.setTranslationX(CursorState.cursorPosition + (CursorState.displayWith - CursorState.placeWith)/2);

                int t = (int)CursorState.cursorPosition;
                if(t%(CursorState.col_width/2) < 20 && t%(CursorState.col_width) > 20){
                    int next_col = (int)((t+CursorState.col_width/2)/(CursorState.col_width));
                    if(next_col > col){
                        col = next_col;
                        ArrayState.check(col);
                    }
                }

                Log.d(LOG_TAG, "X: " + String.valueOf(CursorState.cursorPosition));
                if(CursorState.cursorPosition >= CursorState.placeWith){ //END
//                    CursorState.is_pause = true;
//                    anim.end();

//                    if(CursorState.is_repeat){
//                        CursorState.is_pause = true;
//                        anim.end();
//                        anim.setFloatValues(0.0f, 1.0f);
//                        //anim.start();
//                    } else{
//                        // отмечаем состояние анимации
//                        CursorState.is_run = false;
//                        // изменяем картинку на кнопке
//                        startButton.setImageResource(R.drawable.ic_fa_play);
//                    }
//
//                    // Возврат курсора
//                    CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;
//                    cursor.setTranslationX(CursorState.cursorPosition);
//                    // очищаем информацию о паузе
//                    CursorState.animValue = 0;
//                    CursorState.is_pause = false;
//                    col = 0;
                }
            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // отмечаем состояние анимации
                CursorState.is_run = false;
                // изменяем картинку на кнопке
                startButton.setImageResource(R.drawable.ic_fa_play);
                // Возврат курсора
                CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;
                cursor.setTranslationX(CursorState.cursorPosition);
                // очищаем информацию о паузе
                CursorState.animValue = 0;
                CursorState.is_pause = false;
                col = 0;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                anim.setFloatValues(0.0f, 1.0f);
                CursorState.animValue = 0;
                CursorState.is_pause = false;
                col = 0;
            }
        });


        // BUTTONS //

        // Button Start/Pause Onclick
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CursorState.is_run){
                    // постановка на паузу
                   // cursor.clearAnimation();

                    // отмечаем постановку на паузу
                    CursorState.is_pause = true;

                    // останавливаем анимацию
                    anim.end();
                    // отмечаем состояние анимации
                    CursorState.is_run = false;
                    // изменяем картинку на кнопке
                    startButton.setImageResource(R.drawable.ic_fa_play);
                    // Возврат курсора
                    //CursorState.cursorPosition = cursorShift;
                    cursor.setTranslationX(CursorState.cursorPosition+(CursorState.displayWith - CursorState.placeWith)/2);
                } else {
                    if(CursorState.is_pause){ // если поставлено на паузу
                        CursorState.is_pause = false;
                    }
                    anim.setFloatValues(CursorState.animValue, 1.0f);

                    // запускаем анимацию
                    anim.setDuration(CursorState.speed);
                    anim.start();
                    // останавливаем анимацию
                    CursorState.is_run = true;
                    // изменяем картинку на кнопке
                    startButton.setImageResource(R.drawable.ic_fa_pause);
                }
            }
        });

        // Button stop
        ImageView stopButton = (ImageView) findViewById(R.id.stop_play);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // останавливаем анимацию
                anim.end();
                // отмечаем состояние анимации
                CursorState.is_run = false;
                // изменяем картинку на кнопке
                startButton.setImageResource(R.drawable.ic_fa_play);
                // Возврат курсора
                CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;
                cursor.setTranslationX(CursorState.cursorPosition);
                // очищаем информацию о паузе
                CursorState.animValue = 0;
                CursorState.is_pause = false;
            }
        });

        // Button four_four
        final Button four_four = (Button) findViewById(R.id.four_four);
        four_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CursorState.cols == 12){
                    CursorState.setCol(16);
                    four_four.setText("4/4");
                } else {
                    CursorState.setCol(12);
                    four_four.setText("4/3");
                }
                // рисуем поле
                generateGrid();
            }
        });

        ImageView repeatButton = (ImageView) findViewById(R.id.repeatButton);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CursorState.is_repeat = !CursorState.is_repeat;
                if(CursorState.is_repeat){
                    v.setBackgroundColor(Color.parseColor("#e84747"));
                    anim.setRepeatCount(ValueAnimator.INFINITE);
                    anim.setRepeatMode(ValueAnimator.RESTART);
                }
                else{
                    v.setBackgroundColor(Color.TRANSPARENT);
                    anim.setRepeatCount(0);
                }
            }
        });

        // кнопки страниц
        Button page1 = (Button)findViewById(R.id.button1);
        page1.setPressed(true);

        // рисуем поле
        generateGrid();
    }


    public void onStart(){
        super.onStart();
    }

    /**
     * Прорисовка таблицы
     */
    public void generateGrid(){

        // генерируем первую строку

        int cols = CursorState.cols;
        int row;
        LinearLayout linLayout;
        int layout = R.layout.item_grid12;
        int layout_text = R.layout.item_grid12_text;

        linLayout = (LinearLayout) findViewById(R.id.note_line_0);
        linLayout.removeAllViews();
        row = 0;
        genereteRowText(cols, CursorState.col_width, row, layout_text, linLayout);

        linLayout = (LinearLayout) findViewById(R.id.note_line_1);
        linLayout.removeAllViews();
        row = 1;
        genereteRow(cols, CursorState.col_width, row, layout, linLayout);

        linLayout = (LinearLayout) findViewById(R.id.note_line_2);
        linLayout.removeAllViews();
        row = 2;
        genereteRow(cols, CursorState.col_width, row, layout, linLayout);

        linLayout = (LinearLayout) findViewById(R.id.note_line_3);
        linLayout.removeAllViews();
        row = 3;
        genereteRow(cols, CursorState.col_width, row, layout, linLayout);

        linLayout = (LinearLayout) findViewById(R.id.note_line_4);
        linLayout.removeAllViews();
        row = 4;
        genereteRow(cols, CursorState.col_width, row, layout, linLayout);

        linLayout = (LinearLayout) findViewById(R.id.note_line_5);
        linLayout.removeAllViews();
        row = 5;
        genereteRow(cols, CursorState.col_width, row, layout, linLayout);

        linLayout = (LinearLayout) findViewById(R.id.note_line_6);
        linLayout.removeAllViews();
        row = 6;
        genereteRow(cols, CursorState.col_width, row, layout, linLayout);

        linLayout = (LinearLayout) findViewById(R.id.note_line_7);
        linLayout.removeAllViews();
        row = 7;
        genereteRow(cols, CursorState.col_width, row, layout, linLayout);

    }

    /**
     * Генерация строки с подписями
     * @param cols
     * @param width_col
     * @param row
     * @param layout
     * @param linLayout
     */
    private void genereteRowText(int cols, int width_col, int row, int layout, LinearLayout linLayout){
        for (int i = 1; i<=cols; i++){
            TextView textView = (TextView) getLayoutInflater().inflate(layout, null);
            textView.setTag("note_" + String.valueOf(row) + "_" + String.valueOf(i));
            if (i % 2 == 0) {
                textView.setText("L");
            } else {
                textView.setText("R");
            }
            textView.setMinimumWidth(width_col);
            linLayout.addView(textView);
        }
    }

    /**
     * Генерация строки
     * @param cols
     * @param width_col
     * @param row
     * @param layout
     * @param linLayout
     */
    private void genereteRow(int cols, int width_col, int row, int layout, LinearLayout linLayout){
        for (int i = 1; i<=cols; i++){

            ImageView imageView = (ImageView) getLayoutInflater().inflate(layout, null);
            imageView.setTag("note_" + String.valueOf(row) + "_" + String.valueOf(i));
            imageView.setMinimumWidth(width_col);

            Map<Integer, String> st = ArrayState.states.get(i);
            if(st != null){
                String state = st.get(row);
                if(state != null && state == STATE_CHECK){
                    imageView.setBackgroundResource(R.drawable.circle_dark);
                }
            }

            linLayout.addView(imageView);
        }
    }


    /**
     * Клик по ноте
     * @param view
     */
    public void onclickNote(View view){

        String tag = String.valueOf(view.getTag());
        String[] separet = tag.split("_");
        int y = Integer.valueOf(separet[1]);
        int x = Integer.valueOf(separet[2]);

        Log.d(LOG_TAG, "click " + String.valueOf(view.getTag()));

        Map<Integer, String> state = ArrayState.states.get(x);

        if(state == null){
            state = new HashMap<>();
            state.put(y, STATE_CHECK);
            view.setBackgroundResource(R.drawable.circle_dark);
        } else {
            if(state.get(y) == STATE_CHECK){
                state.put(y, STATE_UNCHECK);
                view.setBackgroundResource(R.drawable.circle_empty);
            } else {
                state.put(y, STATE_CHECK);
                view.setBackgroundResource(R.drawable.circle_dark);
            }
        }
        ArrayState.states.put(x, state);
        Log.d(LOG_TAG, "click");
    }

    public void helpClick(View v){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

}
