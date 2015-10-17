package devapp.drump;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import devapp.drump.helpers.ArrayState;
import devapp.drump.helpers.CursorState;
import devapp.drump.helpers.DisplayUtil;
import devapp.drump.helpers.PreferenceHelper;
import devapp.drump.helpers.SoundUtil;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = "DRAM";
    private final String STATE_CHECK = "check";
    private final String STATE_UNCHECK = "uncheck";
    private int CICLE = 0;

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
        CursorState.speed = (int)((60f/(CursorState.default_speed*4))*CursorState.cols*1000);
        //sp_test = (TextView) findViewById(R.id.sp_test);

        // NumberPiker
        final Button npb = (Button) findViewById(R.id.speed);
        npb.setText(String.valueOf(CursorState.default_speed));
        npb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });

        // Button Start/Stop
        final ImageView startButton = (ImageView) findViewById(R.id.start_play);
        // Cursor
        final ImageView cursor = (ImageView) findViewById(R.id.cursor);
        // Repeat
        final ImageView repeatButton = (ImageView) findViewById(R.id.repeatButton);
        // Button four_four
        final Button four_four = (Button) findViewById(R.id.four_four);
        // Button cicle type
        final Button cicle_type = (Button) findViewById(R.id.pl);

        // BUTTONS //

        // Button Start/Pause Onclick
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CursorState.is_run){
                    Log.d(LOG_TAG, "Пауза");
                    // постановка на паузу
                    pausePlay();
                    // отмечаем постановку на паузу
                    CursorState.is_pause = true;
                    // отмечаем состояние анимации
                    CursorState.is_run = false;
                    // изменяем картинку на кнопке
                    startButton.setImageResource(R.drawable.ic_fa_play);
                } else {
                    Log.d(LOG_TAG, "старт");
                    if(CursorState.is_pause){ // если поставлено на паузу
                        Log.d(LOG_TAG, "Возобновление");
                        //Log.d(LOG_TAG, String.valueOf((float)(CursorState.placeWith-CursorState.cursorPosition)/(float)CursorState.placeWith));

                    } else {
                        CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;
                        repeatButton.setVisibility(View.INVISIBLE);
                        npb.setVisibility(View.INVISIBLE);
                        four_four.setVisibility(View.INVISIBLE);
                        cursor.setVisibility(View.VISIBLE);
                    }
                    //Log.d(LOG_TAG, String.valueOf(CursorState.cursorPosition));
                    // запускаем анимацию
                    CursorState.is_pause = false;
                    CursorState.is_run = true;
                    // изменяем картинку на кнопке
                    startButton.setImageResource(R.drawable.ic_fa_pause);
                    startPlay();
                }
            }
        });

        // Button stop
        ImageView stopButton = (ImageView) findViewById(R.id.stop_play);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlay();
                //TODO можно перенести всё в отдельную функцию если вынести обьекты view в свойства класса
                // останавливаем анимацию
                Boolean tmp = CursorState.is_repeat;
                CursorState.is_repeat = false;
                CursorState.is_run = false;
                CursorState.is_repeat = tmp;

                // изменяем картинку на кнопке
                startButton.setImageResource(R.drawable.ic_fa_play);
                // Возврат курсора
                CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;
                cursor.setTranslationX(CursorState.cursorPosition);
                // очищаем информацию о паузе
                //CursorState.animValue = 0;
                CursorState.is_pause = false;
                col = 0;
                repeatButton.setVisibility(View.VISIBLE);
                npb.setVisibility(View.VISIBLE);
                four_four.setVisibility(View.VISIBLE);
                cursor.setVisibility(View.INVISIBLE);
            }
        });


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


        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CursorState.is_repeat = !CursorState.is_repeat;
                if (CursorState.is_repeat) {
                    v.setBackgroundColor(Color.parseColor("#e84747"));
                } else {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        cicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CICLE == 1){
                    ((Button)view).setText("ONE");
                    CICLE = 0;
                } else {
                    ((Button)view).setText("ALL");
                    CICLE = 1;
                }
            }
        });

        // кнопки страниц
        Button page1 = (Button)findViewById(R.id.page_1);
        page1.setActivated(true);

        // рисуем поле
        refreshPage();
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
            textView.setMinimumWidth(width_col+ DisplayUtil.dpToPx(2));
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
            // отображаем ноты
            Map<Integer, String> st = ArrayState.states.get(i);
            if(st != null){
                String state = st.get(row);
                Log.d(LOG_TAG, "NOTE: " + row + " " + state);
                if(state != null && state.equals(STATE_CHECK)){
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

        if(state == null || state.equals(STATE_UNCHECK)){
            state = new HashMap<>();
            state.put(y, STATE_CHECK);
            view.setBackgroundResource(R.drawable.circle_dark);
        } else {
            if(state.get(y) != null && state.get(y).equals(STATE_CHECK)){
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

    /**
     * клик по кнопке справка
     * @param v
     */
    public void helpClick(View v){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    /**
     * Показ диалога для выбора колличества
     */
    private void showAlert(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog_speed, null);
        final NumberPicker np = (NumberPicker) textEntryView.findViewById(R.id.sp);
        String[] nums = new String[190];

        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i+20);
        np.setMaxValue(nums.length + 20);
        np.setMinValue(20);
        np.setValue(CursorState.default_speed);
        CursorState.speed = (int)((60f/(CursorState.default_speed*4))*CursorState.cols*1000);
        //np.setWrapSelectorWheel(false);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(textEntryView);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(np.getValue() > 0){ // если колличество больше 0
                    CursorState.default_speed = np.getValue();
                    CursorState.speed = (int)((60f/(np.getValue()*4))*CursorState.cols*1000);
                    Button npb = (Button) findViewById(R.id.speed);
                    npb.setText(String.valueOf(np.getValue()));
                }
            }
        });
        alert.show();
    }

    private ImageView cur;
    public static long time;
    public static int count_col = 0;

    long starttime = 0;

    // Таймер для движения курсора

    //tells activity to run on ui thread
    class secondTask extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //sp_test.setText(String.valueOf(CursorState.temp_time)); // отображение периода
                    CursorState.cursorPosition += CursorState.col_width / 45; //шаг курсора
                    cur.setTranslationX(CursorState.cursorPosition);

                    if (CursorState.cursorPosition >= CursorState.placeWith+(CursorState.displayWith - CursorState.placeWith)/2) {
                        CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;
                        if((!CursorState.is_repeat && CICLE != 1) || CursorState.current_page == R.id.page_10){
                            CursorState.is_run = false;

                            // Button Start/Stop
                            final ImageView startButton = (ImageView) findViewById(R.id.start_play);
                            // Cursor
                            final ImageView cursor = (ImageView) findViewById(R.id.cursor);
                            // Repeat
                            final ImageView repeatButton = (ImageView) findViewById(R.id.repeatButton);
                            // Button four_four
                            final Button four_four = (Button) findViewById(R.id.four_four);

                            startButton.setImageResource(R.drawable.ic_fa_play);
                            // Возврат курсора
                            CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;

                            CursorState.cursorPosition+=CursorState.col_width/2;

                            cursor.setTranslationX(CursorState.cursorPosition);
                            // очищаем информацию о паузе
                            //CursorState.animValue = 0;
                            CursorState.is_pause = false;
                            col = 0;
                            repeatButton.setVisibility(View.VISIBLE);
                            final Button npb = (Button) findViewById(R.id.speed);
                            npb.setVisibility(View.VISIBLE);
                            four_four.setVisibility(View.VISIBLE);
                            cursor.setVisibility(View.INVISIBLE);

                        }
                    }
                    if(!CursorState.is_run){
                        if(CursorState.is_pause){
                            pausePlay();
                        } else {
                            stopPlay();
                        }
                    }

                }
            });
        }
    };


    Timer timer = new Timer();

    /**
     * Начало воспроизведения
     */
    private void startPlay(){
        cur = (ImageView) findViewById(R.id.cursor);
        time = (int)((float)CursorState.speed)/(CursorState.cols);
        starttime = System.currentTimeMillis();
        timer = new Timer();
        long d = ((long)CursorState.speed/(long)CursorState.cols)/50L;
        Log.d("TEST", String.valueOf(d));
        timer.schedule(new secondTask(), 0, d);

        // воспроизведение
        Thread th = new Thread(new Runnable(){



            public void run() {

                long old_time = 0;
                long millis = 0;
                long m_time = 0;
               // long curr = 0;
                long diff = ((long)CursorState.speed/(long)CursorState.cols)*1000000L;
                long diff2 = diff/2L;
                // int cursor_diff = (CursorState.displayWith - CursorState.placeWith)/2;

                // пока не остановлено воспроизведение
                while (CursorState.is_run){
                    m_time = System.nanoTime() - old_time;

                    if ((m_time >= diff2 && m_time <= diff) || old_time == 0L ) {
                        // ожидаем недостающий промежуток вермени
                        while (m_time <= diff){
                            m_time =  System.nanoTime() - old_time;
                        }
                        // если воспроизведение остановлено
                        if (!CursorState.is_run) {
                            break;
                        }

                        CursorState.temp_time = (System.nanoTime()-millis) / 1000000L;
                        old_time = System.nanoTime();
                        millis = old_time;

                        MainActivity.count_col++;
                        ArrayState.check(MainActivity.count_col);

                        CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;

                        CursorState.cursorPosition+=CursorState.col_width/2;

                        CursorState.cursorPosition+=CursorState.col_width*(MainActivity.count_col-1);
                        // если дошли до конца листа
                        if (MainActivity.count_col >= CursorState.cols) {
                            MainActivity.count_col = 0;

                            if(CICLE == 1){ //если воспроизведение всех страниц
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        nextPage();
                                    }
                                });
                            }

                            // если нет повтора
                            if ((!CursorState.is_repeat && CICLE != 1) || CursorState.current_page == R.id.page_10) {
                                break;
                            }

                            CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;
                        }

                       // CursorState.cursorPosition = cursor_diff + CursorState.col_width*MainActivity.count_col - CursorState.col_width/2 ;

                        old_time = old_time - (System.nanoTime() - old_time);
                    }
                }

            }
        });
        th.setPriority(Thread.MAX_PRIORITY);
        th.start();
    }

    /**
     * Пауза воспроизведения
     */
    private void pausePlay(){
        timer.cancel();
        timer.purge();
    }

    /**
     * Остановка воспроизведения
     */
    private void stopPlay(){
        pausePlay();
        count_col = 0;
        CursorState.is_pause = false;
        CursorState.is_run = false;
        CursorState.cursorPosition = (CursorState.displayWith - CursorState.placeWith)/2;
        CursorState.cursorPosition+=CursorState.col_width/2;
    }

    /**
     * Клик по странице
     * @param v
     */
    public void clickByPage(View v){
        // если страница не изменилась
        if(CursorState.current_page == v.getId())
            return;
        toPage(v);
    }

    private void toPage(View v){
        // сохранение текущей страницы
        ArrayState.saveState(this);
        // снимаем активацию с предыдущей кнопки
        View old_button = findViewById(CursorState.current_page);
        old_button.setActivated(false);
        // отмечаем активной текущую кнопку
        v.setActivated(true);
        // обновляем ид активной страницы
        CursorState.current_page = v.getId();
        Log.d(LOG_TAG, "CURRENT PAGE " + CursorState.current_page);
        refreshPage();
    }

    /**
     * Получаем сохранённые данные
     */
    private void refreshPage(){
        String page_date = PreferenceHelper.get(this, String.valueOf(CursorState.current_page), "");
        Log.d(LOG_TAG, "SAVED DATE " + page_date);
        ArrayState.states.clear();
        if(page_date.equals("")){
            // рисуем поле
            generateGrid();
            return;
        }
        JSONObject json;
        try{
            json = new JSONObject(page_date);

            for(int x = 16; x > 0; x--){
                if(json.isNull(String.valueOf(x)))
                    continue;
                JSONObject col = json.getJSONObject(String.valueOf(x));
                Map<Integer, String> m = new HashMap<>();
                Log.d(LOG_TAG, "X "+x);
                for(int y = 7; y > 0; y--){
                    if(col.isNull(String.valueOf(y)))
                        continue;
                    Log.d(LOG_TAG, "Y "+y);
                    String state = col.getString(String.valueOf(y));
                    Log.d(LOG_TAG, "PUT STATE" + state);
                    m.put(y, state);
                }
                ArrayState.states.put(x, m);
            }
        } catch (Exception e){
            Log.d(LOG_TAG, e.getMessage());
            return;
        } finally {
            // рисуем поле
            generateGrid();
        }
    }

    /**
     * переключение на следующую страницу
     */
    private void nextPage(){
       Integer[] pages = {
               R.id.page_1,
               R.id.page_2,
               R.id.page_3,
               R.id.page_4,
               R.id.page_5,
               R.id.page_6,
               R.id.page_7,
               R.id.page_8,
               R.id.page_9,
               R.id.page_10,
       };
        int current_index = Arrays.asList(pages).indexOf(CursorState.current_page) + 1;
        if(current_index >= pages.length){
            current_index = 0;
        }
        View v= findViewById(pages[current_index]);
        toPage(v);
    }

    public void toFirst(View view){
        View v= findViewById(R.id.page_1);
        toPage(v);
    }

    @Override
    protected void onPause(){
        // сохранение текущей страницы
        ArrayState.saveState(this);
        stopPlay();
        super.onPause();
    }

    protected void onStop(){
        // сохранение текущей страницы
        ArrayState.saveState(this);
        stopPlay();
        super.onStop();
    }
}
