package devapp.drump.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Button in Into Bold
 */
public class BoldButton extends Button{
    public BoldButton( Context context )
    {
        super( context);
        init();
    }
    public BoldButton( Context context,
                               AttributeSet attrs )
    {
        super( context, attrs );
        init();
    }
    public BoldButton( Context context,
                               AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        init();
    }



    public void init() {
        // Изменение шрифта
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/IB.OTF");
        setTypeface(tf);
    }
}
