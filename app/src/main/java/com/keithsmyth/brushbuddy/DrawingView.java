package com.keithsmyth.brushbuddy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;

import static android.graphics.Paint.Style.STROKE;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;

public class DrawingView extends View {

    private final LinkedList<DrawAction> drawActions = new LinkedList<>();
    private int color = Color.BLACK;

    public DrawingView(Context context) {
        super(context);
        onFinishInflate();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case ACTION_DOWN:
                final DrawAction drawAction = new DrawAction(x, y, createPaint());
                drawAction.move(x, y);
                drawActions.add(drawAction);
                break;
            case ACTION_MOVE:
                drawActions.peekLast().move(x, y);
                break;
            default:
                return false;
        }
        postInvalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (DrawAction drawAction : drawActions) {
            drawAction.draw(canvas);
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void undo() {
        if (drawActions.isEmpty()) return;
        drawActions.removeLast();
        postInvalidate();
    }

    private Paint createPaint() {
        final Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(20);
        paint.setStyle(STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    public void clear() {
        drawActions.clear();
        postInvalidate();
    }

    private static class DrawAction {
        private final Path path;
        private final Paint paint;

        DrawAction(float x, float y, Paint paint) {
            path = new Path();
            path.moveTo(x, y);
            this.paint = paint;
        }

        DrawAction(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }

        void move(float x, float y) {
            path.lineTo(x, y);
        }

        void draw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
    }
}
