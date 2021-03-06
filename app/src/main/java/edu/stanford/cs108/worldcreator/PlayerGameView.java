package edu.stanford.cs108.worldcreator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PlayerGameView extends View {
    public static final float SEPARATOR_HEIGHT = 200.0f;

    private static final float SEPARATOR_STROKE_WIDTH = 5.0f; //TODO FIGURE OUT
    private static final int SEPARATOR_COLOR = Color.BLACK;
    private static final Paint.Style SEPARATOR_STYLE = Paint.Style.STROKE;
    private Paint separatorPaint;

    public static boolean drawOutline;
    public static String selectedShapeName;

    public static float width, height; //Kind of a hack so that inventory drawing works
    private float oldX, oldY;

    public PlayerGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainActivity.curContext = context; //NECESSARY?

        init();
    }

    private void init() {
        separatorPaint = new Paint();
        separatorPaint.setColor(SEPARATOR_COLOR);
        separatorPaint.setStyle(SEPARATOR_STYLE);
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
        Game.curGame.drawPage(canvas, false);
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

        Log.d("FUCK U RUSS", "curPage: " + Game.curGame.getCurPageName());
        for(Shape shape: Game.curGame.getCurrentPage().getShapes()){
            Log.d("FUCK U RUSS", "SHAPES ON PAGE: " + shape.getName());
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                curShape = Game.curGame.getShapeAtCoords(x, y);
                if(curShape != null) Log.d("MESSAGE", "onTouchEvent: curShape:" + curShape.getName());
                else Log.d("MESSAGE", "onTouchEvent: curShape is null");
                if (curShape != null) {
                    if (curShape.getHidden()) return true;
                    Game.curGame.setCurrentShape(curShape);
                    oldX = x;
                    oldY = y;
                    if (y >= height - SEPARATOR_HEIGHT /*so inside inventory*/) {
                        Game.curGame.removeFromInventory(curShape);
                    }
                    curShape.executeOnClick(); //TODO:BUGGY?
                } else {
                    Game.curGame.setCurrentShape(null);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (curShape == null || !curShape.getMovable() || curShape.getHidden()) break;
                curShape.move(x - oldX, y - oldY);
                oldX = x;
                oldY = y;

                //Test for onDrop
                Shape underShape = Game.curGame.getShapeUnder(x, y, curShape);
                if (underShape != null && underShape.canDropOn(curShape)) {
                    //TODO:DRAW RECT AROUND UNDERSHAPE
                    drawOutline = true;
                    selectedShapeName = underShape.getName();
                } else {
                    drawOutline = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (curShape == null) break;
                //Test for onDrop
                underShape = Game.curGame.getShapeUnder(x, y, curShape); //Weird switch statements
                if (underShape != null && underShape.canDropOn(curShape)) {
                    drawOutline = false;
                    underShape.executeOnDrop(curShape);
                }

                //If we are dropping Shape in inventory, put in inventory and run on drop
                float shapesMiddle = curShape.getY() + curShape.getHeight()/2.0f;
                if (shapesMiddle >= height - SEPARATOR_HEIGHT) Game.curGame.addToInventory(curShape);
                else if (curShape.getY() + curShape.getHeight() >= height - SEPARATOR_HEIGHT)
                    curShape.setY(height - SEPARATOR_HEIGHT - curShape.getHeight());
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }
}