package devapp.drump.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by arseniy on 25/06/15.
 */
public class BoldTextView extends TextView {
    public BoldTextView(Context context) {
        super(context);
        init();
    }

    public BoldTextView( Context context,
                            AttributeSet attrs )
    {
        super( context, attrs, -1 );
        init();
    }
    public BoldTextView( Context context,
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
