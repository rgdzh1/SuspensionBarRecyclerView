package com.yey.sbrv;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

/**
 * 悬挂条设置Alpha动画是为了让悬挂调更新内容的时候更加的自然, 没有Alpha动画更新内容的时候非常的生硬.
 * 设置悬挂条Alpha动画,设置透明不透明这些时机都是通过打log来确定的,硬想出来它们的执行的时机还是有些难的.
 */
public class SuspensionBarRecyclerView extends FrameLayout {
    private String TAG = "测试";
    private RecyclerView mRecyclerView;
    private int mResSuspensionBar;
    private View mSuspensionBar;
    private LinearLayoutManager mLayoutManager;
    private int mSuspensionHeight;
    private int mSuspensionBarHeight;
    private int mCurrentFirstVisibleIndex;
    private UpdateSuspensionBarListener mUpdateSuspensionBarListener;

    public SuspensionBarRecyclerView(Context context) {
        this(context, null);
    }

    public SuspensionBarRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuspensionBarRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuspensionBarRecyclerView, defStyleAttr, 0);
        mResSuspensionBar = typedArray.getResourceId(R.styleable.SuspensionBarRecyclerView_sbrv_bar, R.layout.sbrv_default_bar);
        mSuspensionBarHeight = typedArray.getLayoutDimension(R.styleable.SuspensionBarRecyclerView_sbrv_bar_height, ViewGroup.LayoutParams.WRAP_CONTENT);
        typedArray.recycle();
        initView(context);
        setListener();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置悬挂条的尺寸
        ViewGroup.LayoutParams mSuspensionBarParames = (ViewGroup.LayoutParams) mSuspensionBar.getLayoutParams();
        mSuspensionBarParames.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mSuspensionBarParames.height = mSuspensionBarHeight;
        mSuspensionBar.setLayoutParams(mSuspensionBarParames);
        // 设置RecyclerView的尺寸
        ViewGroup.LayoutParams mRecyclerViewParams = (ViewGroup.LayoutParams) mRecyclerView.getLayoutParams();
        mRecyclerViewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mRecyclerViewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mRecyclerView.setLayoutParams(mRecyclerViewParams);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView(Context context) {
        mSuspensionBar = LayoutInflater.from(context).inflate(mResSuspensionBar, null);
        mRecyclerView = new RecyclerView(context);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        this.addView(mRecyclerView, 0);
        this.addView(mSuspensionBar, 1);
    }

    /**
     * 设置RecyclerView的滚动监听
     */
    private void setListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取悬挂条的高度
                mSuspensionHeight = mSuspensionBar.getHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurrentFirstVisibleIndex != mLayoutManager.findFirstVisibleItemPosition()) {
                    // 假如第一Item显示索引与当前获取到的不一致, 就更新第一Item显示索引
                    mCurrentFirstVisibleIndex = mLayoutManager.findFirstVisibleItemPosition();
                    if (mUpdateSuspensionBarListener != null) {
                        // 然后再更新悬挂条中的数据.
                        mUpdateSuspensionBarListener.updateSuspensionBar(mSuspensionBar, mCurrentFirstVisibleIndex);
                    }
                }
                // 找到当前屏幕中显示的第二个Item条目
                View view = mLayoutManager.findViewByPosition(mCurrentFirstVisibleIndex + 1);
                if (view != null) {
                    if (view.getTop() <= mSuspensionHeight) {
                        // 当前屏幕中第二Item离控件顶部距离如果小于悬挂条高度.
                        // 则悬挂条随着RecyclerView的滚动移动, 这个悬挂条一直遮挡着Item条目的头.
                        mSuspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                        if (dy < 0) {
                            // 假如手指向下滑动, 刚好新的第一Item从屏幕顶端滑出来时,设置mSuspensionBar为透明.
                            mSuspensionBar.setAlpha(0);
                        }
//                        Log.e(TAG, "正在移动");
                    } else {
                        // mSuspensionBar.getY() < 0 保证Alpha动画不会多次执行
                        if (mSuspensionBar.getY() < 0) {
                            // 当前屏幕中第二Item离控件顶部距离如果大于悬挂条高度.
                            // 则悬挂调不需要移动,保持在控件顶部静止不动.
                            mSuspensionBar.setY(0);
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                            alphaAnimation.setDuration(1500);
                            mSuspensionBar.startAnimation(alphaAnimation);
//                            Log.e(TAG, "正在执行动画");
                        } else {
                            // 这里是当mSuspensionBar完全展示在屏幕中时候,设置为不透明
                            mSuspensionBar.setAlpha(1);
//                            Log.e(TAG, "mSuspensionBar.getY() > 0");
                        }
                    }
                }
//                Log.e(TAG, "dy  " + dy);
            }
        });
    }


    /**
     * 设置Adapter
     *
     * @param mAdapter
     */
    public void setAdapter(RecyclerView.Adapter mAdapter) {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        if (mUpdateSuspensionBarListener != null) {
            // 更新悬挂条中的数据
            mUpdateSuspensionBarListener.updateSuspensionBar(mSuspensionBar, mCurrentFirstVisibleIndex);
        }
    }

    /**
     * 设置悬挂头更新监听
     *
     * @param updateSuspensionBarListener
     */
    public void setUpdateSuspensionBarListener(UpdateSuspensionBarListener updateSuspensionBarListener) {
        this.mUpdateSuspensionBarListener = updateSuspensionBarListener;
    }

    public interface UpdateSuspensionBarListener {
        void updateSuspensionBar(View mSuspensionBar, int mFirstVisibleIndex);
    }
}
