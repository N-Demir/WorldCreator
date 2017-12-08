package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ndemir on 12/4/2017.
 */

public class EditorGameView extends View {
    private float width, height;
    private float oldX, oldY; //For dragging a shape implementation

    public EditorGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainActivity.curContext = context; //NECESSARY?
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

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("MESSAGE", "onDraw: ");
        super.onDraw(canvas);
        Game.curGame.drawPage(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //TODO: Extension = touch resizing?
        float x = e.getX();
        float y = e.getY();
        Log.d("MESSAGE", "TOUCH EVENT");
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Game.curGame.setCurrentShape(Game.curGame.getCurrentPage().getShapeAtCoords(x, y));
                if(Game.curGame.getCurrentShape() != null) Log.d("MESSAGE", "clicked on " + Game.curGame.getCurrentShape().getName());
                if (Game.curGame.getCurrentShape() != null) {
                    oldX = x;
                    oldY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Game.curGame.getCurrentShape() == null) break;
                Game.curGame.getCurrentShape().move(x - oldX, y - oldY);
                oldX = x;
                oldY = y;
                break;
            case MotionEvent.ACTION_UP:
                //TODO:Do we need to do anything here?
                break;
        }

        invalidate();
        return true;
        //TODO:CALL UPDATE FIELDS IN PARENT ACTIVITY
    }
}
