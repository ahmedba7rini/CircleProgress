package com.bnation.circleprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bruce on 11/6/14.
 */
public class CircleProgress extends View {
    public static int PROGRESS_DIRECTION_LTR = 0;
    public static int PROGRESS_DIRECTION_RTL = 1;

    private Paint paint;
    protected Paint textPaint;

    private RectF rectF = new RectF();

    private float strokeWidth;
    private float suffixTextSize;
    private float arcTextTitleSize;
    private String arcTextTitle;
    private float progressTextSize;
    private int textColor;
    private float progress = 0;
    private int max;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private float arcAngle;
    private String suffixText = "%";
    private float suffixTextPadding;
    private int suffixTextColor;

    private String bottomPercentageText;
    private float bottomPercentageTextSize;
    private int bottomPercentageTextColor;
    private float bottomPercentageTextPaddingTop;

    private String prefixText;
    private float prefixTextSize;
    private int prefixTextColor;
    private float prefixTextPadding;

    private String topText = "";
    private float topTextSize;
    private int topTextColor;
    private float topTextPadding;

    private int progressDirection = 0; // 0 ltr, 1 rtl

    private int progressStartAngle = 270; // 0 ltr, 1 rtl

    private float arcBottomHeight;

    private final int default_finished_color = Color.WHITE;
    private final int default_unfinished_color = Color.rgb(72, 106, 176);
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final float default_suffix_text_size;
    private final float default_suffix_padding;
    private final float default_bottom_text_size;
    private final float default_stroke_width;
    private final String default_suffix_text;
    private final int default_suffix_text_color = Color.rgb(72, 106, 176);
    private final int default_max = 100;
    private final float default_arc_angle = 360 * 0.8f;
    private float default_text_size;
    private final int min_size;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
    private static final String INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size";
    private static final String INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding";
    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_ARC_ANGLE = "arc_angle";
    private static final String INSTANCE_SUFFIX = "suffix";

    private boolean middleLineShow = false;
    private int middleLineColor = Color.BLACK;
    private float middleLineStrokeWidth = 3;
    private float middleLinePaddingTop = 10;

    private float bottomTextTruncateAt = -1;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = Utils.sp2px(getResources(), 18);
        min_size = (int) Utils.dp2px(getResources(), 100);
        default_text_size = Utils.sp2px(getResources(), 40);
        default_suffix_text_size = Utils.sp2px(getResources(), 15);
        default_suffix_padding = Utils.dp2px(getResources(), 4);
        default_suffix_text = "%";
        default_bottom_text_size = Utils.sp2px(getResources(), 10);
        default_stroke_width = Utils.dp2px(getResources(), 4);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.CircleProgress_progressColor, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.CircleProgress_progressBackroundColor, default_unfinished_color);
        textColor = attributes.getColor(R.styleable.CircleProgress_progressTextColor, default_text_color);

        bottomPercentageTextColor = attributes.getColor(R.styleable.CircleProgress_bottomTextColor, default_finished_color);
        bottomPercentageTextSize = attributes.getDimension(R.styleable.CircleProgress_bottomTextSize, default_text_size);
        bottomPercentageText = attributes.getString(R.styleable.CircleProgress_bottomText);
        bottomPercentageTextPaddingTop = attributes.getDimension(R.styleable.CircleProgress_bottomTextPaddingTop, default_suffix_padding);
        bottomTextTruncateAt = attributes.getDimension(R.styleable.CircleProgress_bottomTextTruncateAt, -1);

        prefixText = attributes.getString(R.styleable.CircleProgress_prefixText);
        prefixTextSize = attributes.getDimension(R.styleable.CircleProgress_prefixTextSize, default_suffix_text_size);
        prefixTextColor = attributes.getColor(R.styleable.CircleProgress_prefixTextColor, default_suffix_text_color);
        prefixTextPadding = attributes.getDimension(R.styleable.CircleProgress_prefixTextPadding, default_suffix_padding);

        topText = attributes.getString(R.styleable.CircleProgress_topText);
        topTextSize = attributes.getDimension(R.styleable.CircleProgress_topTextSize, default_suffix_text_size);
        topTextColor = attributes.getColor(R.styleable.CircleProgress_topTextColor, default_suffix_text_color);
        topTextPadding = attributes.getDimension(R.styleable.CircleProgress_topTextPadding, default_suffix_padding);

        progressTextSize = attributes.getDimension(R.styleable.CircleProgress_progressTextSize, default_text_size);
        arcAngle = attributes.getFloat(R.styleable.CircleProgress_angle, default_arc_angle);
        setMax(attributes.getInt(R.styleable.CircleProgress_max, default_max));
        setProgress(attributes.getFloat(R.styleable.CircleProgress_progress, 0));
        strokeWidth = attributes.getDimension(R.styleable.CircleProgress_strokeWidth, default_stroke_width);

        suffixTextSize = attributes.getDimension(R.styleable.CircleProgress_suffixTextSize, default_suffix_text_size);
        suffixText = attributes.getString(R.styleable.CircleProgress_suffixText);
        suffixTextPadding = attributes.getDimension(R.styleable.CircleProgress_suffixTextPadding, default_suffix_padding);
        suffixTextColor = attributes.getColor(R.styleable.CircleProgress_suffixTextColor, default_suffix_text_color);

        arcTextTitleSize = attributes.getDimension(R.styleable.CircleProgress_textTitleSize, default_bottom_text_size);
        arcTextTitle = attributes.getString(R.styleable.CircleProgress_textTitle);

        progressDirection = attributes.getInt(R.styleable.CircleProgress_progressDirection, PROGRESS_DIRECTION_LTR);

        progressStartAngle = attributes.getInt(R.styleable.CircleProgress_progressStartAngle, 270);

        middleLineShow = attributes.getBoolean(R.styleable.CircleProgress_showMiddleLine, middleLineShow);
        middleLineColor = attributes.getColor(R.styleable.CircleProgress_middleLineColor, middleLineColor);
        middleLinePaddingTop = attributes.getDimension(R.styleable.CircleProgress_middleLineTopPadding, middleLinePaddingTop);
        middleLineStrokeWidth = attributes.getDimension(R.styleable.CircleProgress_middleLineStrokeWidth, middleLineStrokeWidth);

    }

    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(progressTextSize);
        textPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }

    public float getSuffixTextSize() {
        return suffixTextSize;
    }

    public void setSuffixTextSize(float suffixTextSize) {
        this.suffixTextSize = suffixTextSize;
        this.invalidate();
    }

    public String getArcTextTitle() {
        return arcTextTitle;
    }

    public void setArcTextTitle(String arcTextTitle) {
        this.arcTextTitle = arcTextTitle;
        this.invalidate();
    }

    public String getBottomPercentageText() {
        return bottomPercentageText;
    }

    public void setBottomPercentageText(String bottomPercentageText) {
        this.bottomPercentageText = bottomPercentageText;
        this.invalidate();
    }

    public float getBottomPercentageTextSize() {
        return bottomPercentageTextSize;
    }

    public void setBottomPercentageTextSize(float bottomPercentageTextSize) {
        this.bottomPercentageTextSize = bottomPercentageTextSize;
        this.invalidate();
    }

    public int getBottomPercentageTextColor() {
        return bottomPercentageTextColor;
    }

    public void setBottomPercentageTextColor(int bottomPercentageTextColor) {
        this.bottomPercentageTextColor = bottomPercentageTextColor;
        this.invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getArcTextTitleSize() {
        return arcTextTitleSize;
    }

    public void setArcTextTitleSize(float arcTextTitleSize) {
        this.arcTextTitleSize = arcTextTitleSize;
        this.invalidate();
    }

    public float getProgressTextSize() {
        return progressTextSize;
    }

    public void setProgressTextSize(float progressTextSize) {
        this.progressTextSize = progressTextSize;
        this.invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    public int getFinishedStrokeColor() {
        return finishedStrokeColor;
    }

    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.finishedStrokeColor = finishedStrokeColor;
        this.invalidate();
    }

    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
        this.invalidate();
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
        this.invalidate();
    }

    public String getSuffixText() {
        return suffixText;
    }

    public void setSuffixText(String suffixText) {
        this.suffixText = suffixText;
        this.invalidate();
    }

    public float getSuffixTextPadding() {
        return suffixTextPadding;
    }

    public void setSuffixTextPadding(float suffixTextPadding) {
        this.suffixTextPadding = suffixTextPadding;
        this.invalidate();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return min_size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f);
        float radius = width / 2f;
        float angle = (360 - arcAngle) / 2f;
        arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = progressStartAngle - arcAngle / 2f;
        float finishedSweepAngle = progress / (float) getMax() * arcAngle;
        float finishedStartAngle = progressStartAngle + arcAngle / 2f;
        paint.setColor(unfinishedStrokeColor);
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);
        paint.setColor(finishedStrokeColor);

        if(progressDirection == PROGRESS_DIRECTION_LTR){
            finishedStartAngle = progressStartAngle - arcAngle / 2f;
            canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint);
        } else {
            canvas.drawArc(rectF, finishedStartAngle, -finishedSweepAngle, false, paint);
        }

        float progressTextBaseLine = 0;
        float progressTextMeasurement = 0;

        String text = String.format("%.2f",getProgress())/*valueOf(getProgress())*/;
        if (!TextUtils.isEmpty(text)) {
            textPaint.setColor(textColor);
            textPaint.setTextSize(progressTextSize);
            float textHeight = textPaint.descent() + textPaint.ascent();
            float textBaseline = (getHeight() - textHeight) / 2.0f;
            progressTextBaseLine = textBaseline;
            progressTextMeasurement = textPaint.measureText(text);
            float progressTextStartX = (getWidth() - textPaint.measureText(text)) / 2.0f;
            canvas.drawText(text, progressTextStartX, textBaseline, textPaint);

            // draw line under progress percentage
            if(middleLineShow){
                Paint paint = new Paint();
                paint.setColor(middleLineColor);
                paint.setAntiAlias(true);
                paint.setStrokeWidth(middleLineStrokeWidth);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);

                float startLineX = (getWidth() - progressTextMeasurement) / 2.0f;
                float endLineX = (getWidth() - progressTextMeasurement) / 2.0f + progressTextMeasurement;
                float startEndLineY = textBaseline + middleLinePaddingTop;
                canvas.drawLine(startLineX, startEndLineY, endLineX, startEndLineY, paint);
            }

            // draw suffix text
            if (!TextUtils.isEmpty(suffixText)){
                textPaint.setColor(suffixTextColor);
                textPaint.setTextSize(suffixTextSize);
                float suffixHeight = textPaint.descent() + textPaint.ascent();
                canvas.drawText(suffixText, getWidth() / 2.0f + textPaint.measureText(text) + suffixTextPadding, textBaseline + textHeight - suffixHeight, textPaint);
            }

            //draw prefix text "%"
            if (!TextUtils.isEmpty(prefixText)){
                textPaint.setColor(prefixTextColor);
                textPaint.setTextSize(prefixTextSize);
                float prefixHeight = textPaint.descent() + textPaint.ascent();
                float prefixTextWidth = textPaint.measureText(prefixText);
                float prefixStartX = progressTextStartX - prefixTextWidth + prefixTextPadding;
                float prefixY = textBaseline + textHeight - prefixHeight;
                canvas.drawText(prefixText, prefixStartX,prefixY,textPaint);
            }
        }

        if (!TextUtils.isEmpty(getTopText())){
            textPaint.setColor(getTopTextColor());
            textPaint.setTextSize(getTopTextSize());
            float textHeight = textPaint.descent() + textPaint.ascent();
//            float textBaseline = (getHeight() - textHeight - progressTextHeight) / 2.0f + progressTextHeight; /*+ progress height*/
            float textBaseline = progressTextBaseLine + textHeight - getTopTextPadding();
            canvas.drawText(getTopText(), (getWidth() - textPaint.measureText(getTopText())) / 2.0f, textBaseline, textPaint);
        }

        if(!TextUtils.isEmpty(getBottomPercentageText())){
            // bottom percentage text not empty
            // draw text under percentage text
            TextPaint textPaint = new TextPaint();//The Paint that will draw the text
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setAntiAlias(true);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            textPaint.setLinearText(true);

            textPaint.setColor(getBottomPercentageTextColor());
            textPaint.setTextSize(getBottomPercentageTextSize());

            float textWidth = (rectF.width() - bottomTextTruncateAt);

            if(bottomTextTruncateAt == -1){ // dont truncate
                textWidth = textPaint.measureText(getBottomPercentageText());
            }

            CharSequence bottomText = TextUtils.ellipsize(getBottomPercentageText(), textPaint, textWidth, TextUtils.TruncateAt.END);

            float textHeight = textPaint.descent() + textPaint.ascent();
//            float textBaseline = (getHeight() - textHeight - progressTextHeight) / 2.0f + progressTextHeight; /*+ progress height*/
            float textBaselineY = progressTextBaseLine - textHeight + getBottomPercentageTextPaddingTop();
            /*float textStartX = (getWidth() - textPaint.measureText(getBottomPercentageText())) / 2.0f;*/
            float textStartX = (getWidth() - textWidth) / 2.0f;

            canvas.drawText(bottomText.toString(), textStartX, textBaselineY, this.textPaint);
            /*canvas.drawText(bottomText, 0, bottomText.length(), textStartX, textBaselineY, textPaint);*/
        }

        if(arcBottomHeight == 0) {
            float radius = getWidth() / 2f;
            float angle = (360 - arcAngle) / 2f;
            arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
        }

        if (!TextUtils.isEmpty(getArcTextTitle())) {
            textPaint.setTextSize(arcTextTitleSize);
            float bottomTextBaseline = getHeight() - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(getArcTextTitle(), (getWidth() - textPaint.measureText(getArcTextTitle())) / 2.0f, bottomTextBaseline, textPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding());
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getArcTextTitleSize());
        bundle.putString(INSTANCE_BOTTOM_TEXT, getArcTextTitle());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getProgressTextSize());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle());
        bundle.putString(INSTANCE_SUFFIX, getSuffixText());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            suffixTextSize = bundle.getFloat(INSTANCE_SUFFIX_TEXT_SIZE);
            suffixTextPadding = bundle.getFloat(INSTANCE_SUFFIX_TEXT_PADDING);
            arcTextTitleSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE);
            arcTextTitle = bundle.getString(INSTANCE_BOTTOM_TEXT);
            progressTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getFloat(INSTANCE_PROGRESS));
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            suffixText = bundle.getString(INSTANCE_SUFFIX);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public float getBottomPercentageTextPaddingTop() {
        return bottomPercentageTextPaddingTop;
    }

    public void setBottomPercentageTextPaddingTop(float bottomPercentageTextPaddingTop) {
        this.bottomPercentageTextPaddingTop = bottomPercentageTextPaddingTop;
        this.invalidate();
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
        this.invalidate();
    }

    public float getTopTextSize() {
        return topTextSize;
    }

    public void setTopTextSize(float topTextSize) {
        this.topTextSize = topTextSize;
        this.invalidate();
    }

    public int getTopTextColor() {
        return topTextColor;
    }

    public void setTopTextColor(int topTextColor) {
        this.topTextColor = topTextColor;
        this.invalidate();
    }

    public float getTopTextPadding() {
        return topTextPadding;
    }

    public void setTopTextPadding(float topTextPadding) {
        this.topTextPadding = topTextPadding;
        this.invalidate();
    }

    /**
     * Progress Direction {@link CircleProgress#PROGRESS_DIRECTION_LTR}, {@link CircleProgress#PROGRESS_DIRECTION_RTL}
     *
     * @return
     */
    public int getProgressDirection() {
        return progressDirection;
    }

    /**
     * Progress Direction {@link CircleProgress#PROGRESS_DIRECTION_LTR}, {@link CircleProgress#PROGRESS_DIRECTION_RTL}
     *
     * @param progressDirection
     */
    public void setProgressDirection(int progressDirection) {
        this.progressDirection = progressDirection;
        this.invalidate();
    }

    public int getProgressStartAngle() {
        return progressStartAngle;
    }

    public void setProgressStartAngle(int progressStartAngle) {
        this.progressStartAngle = progressStartAngle;
        this.invalidate();
    }
}
