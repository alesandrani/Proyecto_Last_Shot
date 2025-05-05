package com.example.proyecto_last_shot;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class WheelView extends View {
    private Paint paint;
    private int width, height;
    private int numSegments = 20;
    private int[] colors = new int[numSegments];
    private String[] numbers = new String[numSegments];
    private float radius;
    private float rotationAngle = 0f;

    public WheelView(Context context) {
        super(context);
        init(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < numSegments; i++) {
            numbers[i] = String.valueOf(i + 1);
            colors[i] = (i % 2 == 0) ? Color.RED : Color.BLACK;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        width = getWidth();
        height = getHeight();
        radius = Math.min(width, height) / 2f;

        float angle = 360f / numSegments;
        float centerX = width / 2f;
        float centerY = height / 2f;

        for (int i = 0; i < numSegments; i++) {
            float startAngle = angle * i + rotationAngle;

            // Pintamos el segmento con color sólido
            paint.setShader(null);
            paint.setColor(colors[i]);
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                    startAngle, angle, true, paint);

            // Línea divisoria dorada
            paint.setColor(Color.rgb(218, 165, 32)); // Dorado
            paint.setStrokeWidth(4);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                    startAngle, angle, true, paint);
            paint.setStyle(Paint.Style.FILL);
        }

        // Dibujar los números
        paint.setColor(Color.WHITE);
        paint.setTextSize(45);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < numSegments; i++) {
            float angleOffset = angle * i + angle / 2 + rotationAngle;
            float x = (float) (centerX + (radius - 100) * Math.cos(Math.toRadians(angleOffset)));
            float y = (float) (centerY + (radius - 100) * Math.sin(Math.toRadians(angleOffset)));
            canvas.drawText(numbers[i], x, y + 15, paint);
        }

        // Círculo central decorativo
        paint.setColor(Color.DKGRAY);
        canvas.drawCircle(centerX, centerY, 50, paint);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(centerX, centerY, 30, paint);

        // Flecha fija
        drawFixedArrow(canvas, centerX, centerY - radius - 20);
    }

    private void drawFixedArrow(Canvas canvas, float x, float y) {
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x - 40, y + 80);
        path.lineTo(x + 40, y + 80);
        path.close();
        canvas.drawPath(path, paint);
    }

    public void rotateWheelWithAnimation(float startAngle, float endAngle) {
        float totalRotation = endAngle - startAngle;
        if (totalRotation > 360f) {
            totalRotation = totalRotation % 360;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(0f, totalRotation);
        animator.setDuration(2000);
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            rotationAngle = (startAngle + animatedValue) % 360;
            invalidate();
        });

        animator.start();
    }

    public float getRotationAngle() {
        return rotationAngle;
    }
}
