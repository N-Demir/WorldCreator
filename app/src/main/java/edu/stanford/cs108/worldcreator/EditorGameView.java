package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ndemir on 12/4/2017.
 */

public class EditorGameView extends View {
    private float width, height;

    public EditorGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Automatically gets called when View is first created and whenever it is resized.
     * Doing width and height calculation once like this is the preferred way apparently.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
    }

}
