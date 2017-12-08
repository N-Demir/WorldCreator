package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PlayerGameView extends View {
    private static final float SEPARATOR_STROKE_WIDTH = 5.0f; //TODO FIGURE OUT
    public static final float SEPARATOR_HEIGHT = 200.0f;

    private Paint separatorPaint;
    private boolean modeIsPlaying;

    public static float width, height; //Kind of a hack so that inventory drawing works
    private float oldX, oldY;

    public PlayerGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainActivity.curContext = context; //NECESSARY?

        init();
    }

    private void init() {
        separatorPaint = new Paint();
        separatorPaint.setColor(Color.BLACK);
        separatorPaint.setStyle(Paint.Style.STROKE);
        separatorPaint.setStrokeWidth(SEPARATOR_STROKE_WIDTH);
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
        Game.curGame.drawPage(canvas);
        drawSeparator(canvas);
        Game.curGame.drawInventory(canvas);
    }

    private void drawSeparator(Canvas canvas) {
        canvas.drawLine(0.0f, height - SEPARATOR_HEIGHT, width, height - SEPARATOR_HEIGHT, separatorPaint);
    }

    public boolean onTouchEvent(MotionEvent e) {
        //TODO: Extension = touch resizing?
        float x = e.getX();
        float y = e.getY();
        Shape curShape = Game.curGame.getCurrentShape();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Game.curGame.setCurrentShape(Game.curGame.getCurrentPage().getShapeAtCoords(x, y));
                curShape = Game.curGame.getCurrentShape();
                if (curShape != null) {
                    oldX = x;
                    oldY = y;
                    if (y >= height - SEPARATOR_HEIGHT /*so inside inventory*/) {
                        Game.curGame.removeFromInventory(curShape);
                    } else {
                        curShape.executeOnClick(); //TODO: FIX THIS
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (curShape == null || !curShape.getMovable() || curShape.getHidden()) break;
                curShape.move(x - oldX, y - oldY);
                oldX = x;
                oldY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (curShape == null) break;
                //If we are dropping Shape in inventory, put in inventory and run on drop
                float shapesMiddle = curShape.getY() + curShape.getHeight()/2.0f;
                if (shapesMiddle >= height - SEPARATOR_HEIGHT) Game.curGame.addToInventory(curShape);
                else if (curShape.getY() + curShape.getHeight() >= height - SEPARATOR_HEIGHT)
                    curShape.setY(height - SEPARATOR_HEIGHT - curShape.getHeight());
                break;
        }

        return true;
    }
}