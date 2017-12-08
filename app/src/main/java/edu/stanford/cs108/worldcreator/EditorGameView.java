package edu.stanford.cs108.worldcreator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by ndemir on 12/4/2017.
 */

public class EditorGameView extends View {
    private float width, height;
    private float oldX, oldY; //For dragging a shape implementation
    private boolean canDrag;

    public EditorGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainActivity.curContext = context; //NECESSARY?
        canDrag = true;
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
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Shape temp = Game.curGame.getCurrentPage().getShapeAtCoords(x, y);
                if (temp != null) Game.curGame.setCurrentShape(temp);
                else canDrag = false;
                if (Game.curGame.getCurrentShape() != null) {
                    oldX = x;
                    oldY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Game.curGame.getCurrentShape() == null || !canDrag) break;
                Game.curGame.getCurrentShape().move(x - oldX, y - oldY);

                oldX = x;
                oldY = y;
                break;
            case MotionEvent.ACTION_UP:
                canDrag = true;
                //TODO:Do we need to do anything here?
                break;
        }

        invalidate();
        ((Editor) (Activity) getContext()).updateShapeSpinner();
        return true;
    }
}
