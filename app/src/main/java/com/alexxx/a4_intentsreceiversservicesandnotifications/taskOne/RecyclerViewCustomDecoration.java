package com.alexxx.a4_intentsreceiversservicesandnotifications.taskOne;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

public class RecyclerViewCustomDecoration extends RecyclerView.ItemDecoration {

    Paint textPaint, tagPaint, circlePaint, trianglePaint, linePaint;

    public RecyclerViewCustomDecoration() {

        tagPaint = new Paint();
        textPaint = new Paint();
        circlePaint = new Paint();
        trianglePaint = new Paint();
        linePaint = new Paint();

        tagPaint.setColor(Color.GRAY);
        textPaint.setColor(Color.GREEN);
        circlePaint.setColor(Color.GREEN);
        trianglePaint.setColor(Color.GREEN);
        linePaint.setColor(Color.GREEN);

        tagPaint.setStyle(Paint.Style.FILL);
        textPaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStyle(Paint.Style.STROKE);
        trianglePaint.setStyle(Paint.Style.FILL);
        linePaint.setStyle(Paint.Style.STROKE);

        tagPaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        circlePaint.setAntiAlias(true);
        trianglePaint.setAntiAlias(true);
        linePaint.setAntiAlias(true);

        textPaint.setFakeBoldText(true);
        textPaint.setTextSize(35);

        circlePaint.setStrokeWidth(5f);
        linePaint.setStrokeWidth(15f);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        final int height = parent.getLayoutParams().height;
        final int width = parent.getLayoutParams().width;

        for (int i = 0; i < parent.getChildCount(); i++){
            final View layout = parent.getChildAt(i);
            View descriptionTextView = ((RelativeLayout) layout).getChildAt(1);
            if (i%2 == 0) {
                descriptionTextView.setPadding(layoutManager.getDecoratedLeft(layout) + 110,0,0,0);

                canvas.drawRoundRect(
                        layoutManager.getDecoratedLeft(layout),
                        layoutManager.getDecoratedTop(layout) + 125,
                        layoutManager.getDecoratedRight(layout) - 800,
                        layoutManager.getDecoratedBottom(layout) - 40,
                        35,
                        35,
                        tagPaint);

                canvas.drawText("EXPLICIT", layoutManager.getDecoratedLeft(layout) + 25,
                        layoutManager.getDecoratedBottom(layout) - 60, textPaint);
            } else {
                descriptionTextView.setPadding(100,15,0,0);

                Path path = new Path();
                path.moveTo(layout.getX() + 15, layout.getY() + 170);
                path.lineTo(layout.getX() + 15, layout.getY() + 170);
                path.lineTo(layout.getX() + 70, layout.getY() + 170);
                path.lineTo((layout.getX() + 70) / 2, layout.getY() + 200);
                path.close();

                canvas.drawPath(path, trianglePaint);

                canvas.drawCircle(layoutManager.getDecoratedLeft(layout) + 40,
                        layoutManager.getDecoratedBottom(layout) - 65,
                        35,
                        circlePaint);

                canvas.drawLine(layout.getX() + 40,
                        (int) layout.getY() + 155,
                        (int) layout.getX() + 40,
                        (int) layout.getY() + 190,
                        linePaint);
            }
        }

    }
}
