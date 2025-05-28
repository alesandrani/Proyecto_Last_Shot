package com.example.proyecto_last_shot;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
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

    private RectF arcRectF;
    private RadialGradient centerGradient;
    private Path arrowPath;

    // Listener para el número seleccionado tras la animación
    public interface OnNumberSelectedListener {
        void onNumberSelected(String number);
    }

    private OnNumberSelectedListener onNumberSelectedListener;

    public void setOnNumberSelectedListener(OnNumberSelectedListener listener) {
        this.onNumberSelectedListener = listener;
    }

    public WheelView(Context context) {
        super(context);
        init();
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        int rojo = Color.parseColor("#D32F2F");
        int negro = Color.parseColor("#1A1A1A");

        for (int i = 0; i < numSegments; i++) {
            if (i == 0) {
                numbers[i] = "0";
                colors[i] = Color.parseColor("#4CAF50");
            } else {
                numbers[i] = String.valueOf(i);
                colors[i] = (i % 2 == 0) ? rojo : negro;
            }
        }

        arcRectF = new RectF();
        arrowPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        radius = Math.min(width, height) / 2f;
        arcRectF.set(width / 2f - radius, height / 2f - radius, width / 2f + radius, height / 2f + radius);
        centerGradient = new RadialGradient(width / 2f, height / 2f, 80,
                new int[]{Color.parseColor("#FFD700"), Color.parseColor("#B8860B")},
                null, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float angle = 360f / numSegments;
        float centerX = width / 2f;
        float centerY = height / 2f;

        for (int i = 0; i < numSegments; i++) {
            float startAngle = angle * i + rotationAngle;

            paint.setShader(null);
            paint.setColor(colors[i]);
            canvas.drawArc(arcRectF, startAngle, angle, true, paint);

            paint.setColor(Color.parseColor("#FFD700"));
            paint.setStrokeWidth(4);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(arcRectF, startAngle, angle, true, paint);
            paint.setStyle(Paint.Style.FILL);
        }

        // Texto
        paint.setTextSize(42);
        paint.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < numSegments; i++) {
            float angleOffset = angle * i + angle / 2 + rotationAngle;
            float x = (float) (centerX + (radius - 90) * Math.cos(Math.toRadians(angleOffset)));
            float y = (float) (centerY + (radius - 90) * Math.sin(Math.toRadians(angleOffset)));

            paint.setColor(colors[i] == Color.parseColor("#1A1A1A") ? Color.WHITE : Color.BLACK);
            canvas.drawText(numbers[i], x, y + 15, paint);
        }

        // Centro
        paint.setShader(centerGradient);
        canvas.drawCircle(centerX, centerY, 60, paint);
        paint.setShader(null);

        // Flecha
        drawShotArrow(canvas, centerX, centerY - radius - 30);
    }

    private void drawShotArrow(Canvas canvas, float x, float y) {
        paint.setColor(Color.parseColor("#FFD700"));
        paint.setStyle(Paint.Style.FILL);
        arrowPath.reset();
        arrowPath.moveTo(x, y);
        arrowPath.lineTo(x - 30, y + 60);
        arrowPath.lineTo(x + 30, y + 60);
        arrowPath.close();
        canvas.drawPath(arrowPath, paint);

        paint.setColor(Color.WHITE);
        canvas.drawRect(x - 15, y + 60, x + 15, y + 75, paint);
    }

    public void rotateWheelWithAnimation(float startAngle, float endAngle) {
        float totalRotation = endAngle - startAngle;
        if (totalRotation > 360f) totalRotation = totalRotation % 360;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, totalRotation);
        animator.setDuration(3000);
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            rotationAngle = (startAngle + animatedValue) % 360;
            invalidate();
        });

        animator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animator) {}

            @Override
            public void onAnimationEnd(android.animation.Animator animator) {
                if (onNumberSelectedListener != null) {
                    String numero = getSelectedNumber();
                    onNumberSelectedListener.onNumberSelected(numero);
                }
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animator) {}

            @Override
            public void onAnimationRepeat(android.animation.Animator animator) {}
        });

        animator.start();
    }

    public float getRotationAngle() {
        return rotationAngle;
    }

    public int getSelectedSegmentIndex() {
        float anglePerSegment = 360f / numSegments;
        float correctedAngle = (360f - (rotationAngle % 360) + anglePerSegment / 2) % 360;
        return (int) (correctedAngle / anglePerSegment);
    }

    public String getSelectedNumber() {
        return numbers[getSelectedSegmentIndex()];
    }
}