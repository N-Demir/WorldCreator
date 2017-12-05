package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class PlayerGameView extends View {
    private static final float SEPARATOR_STROKE_WIDTH = 5.0f; //TODO FIGURE OUT
    private static final float SEPARATOR_HEIGHT = 200.0f;

    private Paint separatorPaint;
    private boolean modeIsPlaying;

    private float width, height;

    public PlayerGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.modeIsPlaying = modeIsPlaying;

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

}