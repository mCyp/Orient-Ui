package com.orient.me.widget.placeholder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orient.me.R;
import com.orient.me.widget.loading.LoadingIndicatorView;


/**
 * 改自 Qiujuer老师https://github.com/qiujuer
 * <p>
 * 简单的占位控件，
 * 实现了显示一个空的图片显示，
 * 可以和MVP配合显示没有数据，正在加载等状态
 */
@SuppressWarnings("unused")
public class StatusView extends LinearLayout implements PlaceHolderView {
    private ImageView mEmptyImg;
    private TextView mStatusText;
    private LoadingIndicatorView loadingIndicatorView;

    private Drawable[] mDrawableIds;
    private ColorStateList[] mColor = new ColorStateList[]{null};
    private CharSequence[] mTextIds = new CharSequence[]{"我去帮你找一下数据～", "哎，错误发生了!", "加载中~"};

    private View[] mBindViews;

    public StatusView(Context context) {
        super(context);
        init(null, 0);
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.status_view, this);
        mEmptyImg = findViewById(R.id.im_empty);
        mStatusText = findViewById(R.id.txt_empty);
        loadingIndicatorView = findViewById(R.id.loading);

        Resources res = getContext().getResources();
        mDrawableIds =
                new Drawable[]{res.getDrawable(R.drawable.ic_empty)
                        , res.getDrawable(R.drawable.ic_error)};

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.StatusView, defStyle, 0);

        Drawable emptyDrawable = a.getDrawable(R.styleable.StatusView_comEmptyDrawable);
        if (emptyDrawable != null) {
            mDrawableIds[0] = emptyDrawable;
        }
        Drawable errorDrawable = a.getDrawable(R.styleable.StatusView_comErrorDrawable);
        if (errorDrawable != null) {
            mDrawableIds[1] = errorDrawable;
        }
        ColorStateList loadingColor = a.getColorStateList(R.styleable.StatusView_comLoadingColor);
        if (loadingColor != null) {
            mColor[0] = loadingColor;
        }
        CharSequence emptyText = a.getText(R.styleable.StatusView_comEmptyText);
        if (emptyText != null && emptyText.length() != 0) {
            mTextIds[0] = emptyText;
        }
        CharSequence errorText = a.getText(R.styleable.StatusView_comErrorText);
        if (errorText != null && errorText.length() != 0) {
            mTextIds[1] = errorText;
        }
        CharSequence loadingText = a.getText(R.styleable.StatusView_comLoadingText);
        if (loadingText != null && loadingText.length() != 0) {
            mTextIds[2] = loadingText;
        }

        if (mColor[0] != null)
            loadingIndicatorView.setIndicatorColor(mColor[0].getDefaultColor());

        a.recycle();
    }

    /**
     * 绑定一系列数据显示的布局
     * 当前布局隐藏时（有数据时）自动显示绑定的数据布局
     * 而当数据加载时，自动显示Loading，并隐藏数据布局
     *
     * @param views 数据显示的布局
     */
    public void bind(View... views) {
        this.mBindViews = views;
    }

    /**
     * 更改绑定布局的显示状态
     *
     * @param visible 显示的状态
     */
    private void changeBindViewVisibility(int visible) {
        final View[] views = mBindViews;
        if (views == null || views.length == 0)
            return;

        for (View view : views) {
            view.setVisibility(visible);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerEmpty() {
        loadingIndicatorView.setVisibility(GONE);
        loadingIndicatorView.hide();
        mEmptyImg.setImageDrawable(mDrawableIds[0]);
        mStatusText.setText(mTextIds[0]);
        mEmptyImg.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerNetError() {
        loadingIndicatorView.setVisibility(GONE);
        loadingIndicatorView.hide();

        mEmptyImg.setImageDrawable(mDrawableIds[1]);
        mStatusText.setText(mTextIds[1]);
        mEmptyImg.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerError(@StringRes int strRes) {
        loadingIndicatorView.setVisibility(GONE);
        loadingIndicatorView.hide();

        mEmptyImg.setImageDrawable(mDrawableIds[1]);
        mStatusText.setText(mTextIds[1]);
        mEmptyImg.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        changeBindViewVisibility(GONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerLoading() {
        mEmptyImg.setVisibility(GONE);
        mStatusText.setText(mTextIds[2]);
        setVisibility(VISIBLE);
        loadingIndicatorView.setVisibility(VISIBLE);
        loadingIndicatorView.show();
        changeBindViewVisibility(GONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerOk() {
        setVisibility(GONE);
        changeBindViewVisibility(VISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void triggerOkOrEmpty(boolean isOk) {
        if (isOk)
            triggerOk();
        else
            triggerEmpty();
    }

}