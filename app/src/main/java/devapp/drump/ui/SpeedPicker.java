package devapp.drump.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by arseniy on 29/06/15.
 */
public class SpeedPicker extends NumberPicker {
    public SpeedPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        if(view instanceof EditText){
            ((EditText) view).setTextSize(20);
            ((EditText) view).setTextColor(Color.parseColor("#513d24"));
            //((EditText) view).setPadding(40, 20, 20, 10);
            //((EditText) view).setMinHeight(50);
        }
    }
}
