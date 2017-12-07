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


        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Game.curGame.setCurrentShape(Game.curGame.getCurrentPage().getShapeAtCoords(x, y));
                if (Game.curGame.getCurrentShape() != null) {
                    oldX = x;
                    oldY = y;
                    if (y >= height - SEPARATOR_HEIGHT /*so inside inventory*/) {
                        Game.curGame.removeFromInventory(Game.curGame.getCurrentShape());
                    } else {
                        Game.curGame.getCurrentShape().runScript_OnClick(); //TODO:Needs implementation
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Game.curGame.getCurrentShape() == null ||
                        !Game.curGame.getCurrentShape().getMovable()) break;
                Game.curGame.getCurrentShape().move(x - oldX, y - oldY);
                oldX = x;
                oldY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (Game.curGame.getCurrentShape() == null) break;
                //If we are dropping Shape in inventory, put in inventory and run on drop
                if (y >= height - SEPARATOR_HEIGHT) {
                    Game.curGame.addToInventory(Game.curGame.getCurrentShape());
                }
                break;
        }

        return true;
    }
}