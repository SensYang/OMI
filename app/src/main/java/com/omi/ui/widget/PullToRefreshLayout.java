package com.omi.ui.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omi.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头
 *
 * @author 陈靖
 */
public class PullToRefreshLayout extends RelativeLayout {
    // 初始状态
    public static final int INIT = 0;
    // 释放刷新
    public static final int RELEASE_TO_REFRESH = 1;
    // 正在刷新
    public static final int REFRESHING = 2;
    // 释放加载
    public static final int RELEASE_TO_LOAD = 3;
    // 正在加载
    public static final int LOADING = 4;
    // 操作完毕
    public static final int DONE = 5;
    // 当前状态
    private int state = INIT;
    // 刷新回调接口
    private OnRefreshListener mListener;
    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY, downX;

    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float pullDownY = 0;
    // 上拉的距离
    private float pullUpY = 0;

    /**
     * 释放刷新的距离
     */
    private float refreshDist = 200;
    /**
     * 释放加载的距离
     */
    private float loadmoreDist = 200;
    /**
     * 释放刷新的默认距离
     */
    private float refreshInvalidateDist = 200;
    /**
     * 释放加载的默认距离
     */
    private float loadmoreInvalidateDist = 200;
    /**
     * 释放刷新的偏移距离
     */
    private float refreshDiff = 0;
    /**
     * 释放加载的偏移距离
     */
    private float loadmoreDiff = 0;


    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;

    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    // 下拉头
    private View refreshView;
    // 下拉的箭头
    private View pullView;
    // 正在刷新的图标
    private View refreshingView;
    // 刷新结果图标
    private View refreshStateImageView;
    // 刷新结果：成功或失败
    private TextView refreshStateTextView;

    // 上拉头
    private View loadmoreView;
    // 上拉的箭头
    private View pullUpView;
    // 正在加载的图标
    private View loadingView;
    // 加载结果图标
    private View loadStateImageView;
    // 加载结果：成功或失败
    private TextView loadStateTextView;

    // 实现了Pullable接口的View
    private View pullableView;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;
    private boolean needPullDown = true;
    private boolean needPullUp = false;

    private final int FINISHED = 11;
    private final int LONG_TIME_LOAD = 0x211;
    private final int LONG_TIME_REFRESH = 0x31;
    private long longTimeForFail = 10000;
    /**
     * 正在刷新
     */
    private boolean refreshIng = false;
    /**
     * 正在加载更多
     */
    private boolean loadmoreIng = false;

    public void setLongTimeForFail(long longTimeForFail) {
        this.longTimeForFail = longTimeForFail;
    }

    /**
     * 执行自动回滚的handler
     */
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISHED:
                    changeState(DONE);
                    hide();
                    return;
                case LONG_TIME_REFRESH:
                    doneRefresh(false);
                    return;
                case LONG_TIME_LOAD:
                    doneLoadmore(false);
                    return;
            }
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示正在刷新...
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                } else if (state == LOADING && -pullUpY <= loadmoreDist) {
                    pullUpY = -loadmoreDist;
                    timer.cancel();
                } else {
                    if (pullDownY == 0 && pullUpY == 0) {
                        timer.cancel();
                    }
                }
            }
            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            else if (pullUpY < 0)
                pullUpY += MOVE_SPEED;
            if (pullDownY < 0) {
                // 已完成回弹
                pullDownY = 0;
                pullView.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
            }
            if (pullUpY > 0) {
                // 已完成回弹
                pullUpY = 0;
                pullUpView.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
            }
            // 刷新布局,会自动调用onLayout
            requestLayout();
        }

    };

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        refreshView = inflate(context, R.layout.refresh_head, null);
        loadmoreView = inflate(context, R.layout.load_more, null);
        addView(refreshView, 0);
        addView(loadmoreView);
        timer = new MyTimer(updateHandler);
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    public void setPullColor(int color) {
        if (refreshView != null) refreshView.setBackgroundColor(color);
        if (loadmoreView != null) loadmoreView.setBackgroundColor(color);
    }

    private void hide() {
        timer.schedule(5);
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     */
    /**
     * @param isSuccess true 代表成功，false 代表失败
     */
    public void doneRefresh(boolean isSuccess) {
        if (!refreshIng) return;
        refreshIng = false;
        updateHandler.removeMessages(LONG_TIME_REFRESH);
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);
        if (isSuccess) {
            // 刷新成功
            refreshStateImageView.setVisibility(View.VISIBLE);
            refreshStateTextView.setText(R.string.refresh_succeed);
            refreshStateImageView.setBackgroundResource(R.mipmap.refresh_succeed);
        } else {
            // 刷新失败
            refreshStateImageView.setVisibility(View.VISIBLE);
            refreshStateTextView.setText(R.string.refresh_fail);
            refreshStateImageView.setBackgroundResource(R.mipmap.refresh_failed);
        }
        // 刷新结果停留1秒
        updateHandler.sendEmptyMessageDelayed(FINISHED, 1000);
    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param isSuccess true代表成功，false代表失败
     */
    public void doneLoadmore(boolean isSuccess) {
        if (!loadmoreIng) return;
        loadmoreIng = false;
        updateHandler.removeMessages(LONG_TIME_LOAD);
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        if (isSuccess) {
            // 加载成功
            loadStateImageView.setVisibility(View.VISIBLE);
            loadStateTextView.setText(R.string.load_succeed);
            loadStateImageView.setBackgroundResource(R.mipmap.load_succeed);
        } else {
            // 加载失败
            loadStateImageView.setVisibility(View.VISIBLE);
            loadStateTextView.setText(R.string.load_fail);
            loadStateImageView.setBackgroundResource(R.mipmap.load_failed);
        }
        // 刷新结果停留1秒
        updateHandler.sendEmptyMessageDelayed(FINISHED, 1000);
    }

    private void changeState(int to) {
        state = to;
        switch (state) {
            case INIT:
                // 下拉布局初始状态
                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setText(R.string.pull_to_refresh);
                pullView.clearAnimation();
                pullView.setVisibility(View.VISIBLE);
                // 上拉布局初始状态
                loadStateImageView.setVisibility(View.GONE);
                loadStateTextView.setText(R.string.pullup_to_load);
                pullUpView.clearAnimation();
                pullUpView.setVisibility(View.VISIBLE);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                refreshStateTextView.setText(R.string.release_to_refresh);
                pullView.startAnimation(rotateAnimation);
                break;
            case REFRESHING:
                // 正在刷新状态
                pullView.clearAnimation();
                refreshingView.setVisibility(View.VISIBLE);
                pullView.setVisibility(View.INVISIBLE);
                refreshingView.startAnimation(refreshingAnimation);
                refreshStateTextView.setText(R.string.refreshing);
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                loadStateTextView.setText(R.string.release_to_load);
                pullUpView.startAnimation(rotateAnimation);
                break;
            case LOADING:
                // 正在加载状态
                pullUpView.clearAnimation();
                loadingView.setVisibility(View.VISIBLE);
                pullUpView.setVisibility(View.INVISIBLE);
                loadingView.startAnimation(refreshingAnimation);
                loadStateTextView.setText(R.string.loading);
                break;
            case DONE:
                // 刷新或加载完毕，啥都不做
                break;
        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                downX = ev.getX();
                lastY = downY;
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - downX) > 2 * Math.abs(ev.getY() - downY)) {
                    downY = ev.getY();
                    super.dispatchTouchEvent(ev);
                    return true;
                } else {
                    downX = ev.getX();
                }
                if (mEvents == 0) {
                    if (needPullDown && PullableUtil.canPullDown(pullableView) && canPullDown && state != LOADING) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
                            canPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (state == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    } else if (needPullUp && PullableUtil.canPullUp(pullableView) && canPullUp && state != REFRESHING) {
                        // 可以上拉，正在刷新时不能上拉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight())
                            pullUpY = -getMeasuredHeight();
                        if (state == LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    } else
                        releasePull();
                } else
                    mEvents = 0;
                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
                requestLayout();
                if (pullDownY <= refreshDist && state == RELEASE_TO_REFRESH) {
                    // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                    changeState(INIT);
                }
                if (pullDownY >= refreshDist && state == INIT) {
                    // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                    changeState(RELEASE_TO_REFRESH);
                }
                // 下面是判断上拉加载的，同上，注意pullUpY是负值
                if (-pullUpY <= loadmoreDist && state == RELEASE_TO_LOAD) {
                    changeState(INIT);
                }
                if (-pullUpY >= loadmoreDist && state == INIT) {
                    changeState(RELEASE_TO_LOAD);
                }
                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
                    // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                    isTouch = false;
                if (state == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING);
                    // 刷新操作
                    if (mListener != null) {
                        refreshIng = true;
                        mListener.onRefresh(this);
                        updateHandler.sendEmptyMessageDelayed(LONG_TIME_REFRESH, longTimeForFail);
                    }
                } else if (state == RELEASE_TO_LOAD) {
                    changeState(LOADING);
                    // 加载操作
                    if (mListener != null) {
                        loadmoreIng = true;
                        mListener.onLoadMore(this);
                        updateHandler.sendEmptyMessageDelayed(LONG_TIME_LOAD, longTimeForFail);
                    }
                }
                hide();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hide();
    }

    /**
     * @author chenjing 自动模拟手指滑动的task
     */
    private class AutoRefreshAndLoadTask extends AsyncTask<Integer, Float, String> {

        @Override
        protected String doInBackground(Integer... params) {
            while (pullDownY < 4 / 3 * refreshDist) {
                pullDownY += MOVE_SPEED;
                publishProgress(pullDownY);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            changeState(REFRESHING);
            // 刷新操作
            if (mListener != null) {
                refreshIng = true;
                mListener.onRefresh(PullToRefreshLayout.this);
                updateHandler.sendEmptyMessageDelayed(LONG_TIME_REFRESH, longTimeForFail);
            }
            hide();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (pullDownY > refreshDist)
                changeState(RELEASE_TO_REFRESH);
            requestLayout();
        }

    }

    /**
     * 设置是否需要下拉刷新
     */
    public void setNeedPullDown(boolean needPullDown) {
        this.needPullDown = needPullDown;
    }

    /**
     * 设置是否需要上拉加载更多
     */
    public void setNeedPullUp(boolean needPullUp) {
        this.needPullUp = needPullUp;
    }

    /**
     * 自动刷新
     */
    public void doRefresh() {
        AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
        task.execute(5);
    }

    /**
     * 自动加载
     */
    public void doLoadMore() {
        pullUpY = -loadmoreDist;
        requestLayout();
        changeState(LOADING);
        // 加载操作
        if (mListener != null) {
            loadmoreIng = true;
            mListener.onLoadMore(this);
            updateHandler.sendEmptyMessageDelayed(LONG_TIME_LOAD, longTimeForFail);
        }
    }

    private void initView() {
        // 初始化下拉布局
        pullView = refreshView.findViewById(R.id.pull_icon);
        refreshStateTextView = (TextView) refreshView.findViewById(R.id.state_tv);
        refreshingView = refreshView.findViewById(R.id.refreshing_icon);
        refreshStateImageView = refreshView.findViewById(R.id.state_iv);
        // 初始化上拉布局
        pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
        loadStateTextView = (TextView) loadmoreView.findViewById(R.id.loadstate_tv);
        loadingView = loadmoreView.findViewById(R.id.loading_icon);
        loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                if (v != refreshView && v != loadmoreView) {
                    pullableView = v;
                }
            }
            isLayout = true;
            initView();
            refreshInvalidateDist = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
            loadmoreInvalidateDist = ((ViewGroup) loadmoreView).getChildAt(0).getMeasuredHeight();
            refreshDist = refreshInvalidateDist + refreshDiff;
            loadmoreDist = loadmoreInvalidateDist + loadmoreDiff;
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        int diffY = (int) (pullDownY + pullUpY);
        refreshView.layout(0, diffY - refreshView.getMeasuredHeight(), refreshView.getMeasuredWidth(), diffY);
        pullableView.layout(0, diffY, pullableView.getMeasuredWidth(), diffY + pullableView.getMeasuredHeight());
        loadmoreView.layout(0, diffY + pullableView.getMeasuredHeight(), pullableView.getMeasuredWidth(), pullableView.getMeasuredHeight());
    }

    public float getRefreshDiff() {
        return refreshDiff;
    }

    /**
     * 设置下拉刷新的偏移距离
     */
    public void setRefreshDiff(float refreshDiff) {
        if (-refreshDiff > refreshInvalidateDist) return;
        this.refreshDiff = refreshDiff;
        refreshDist = refreshInvalidateDist + refreshDiff;
    }

    public float getLoadmoreDiff() {
        return loadmoreDiff;
    }

    public void setLoadmoreDiff(float loadmoreDiff) {
        if (-loadmoreDiff > loadmoreInvalidateDist) return;
        this.loadmoreDiff = loadmoreDiff;
        loadmoreDist = loadmoreInvalidateDist + loadmoreDiff;
    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(PullToRefreshLayout pullToRefreshLayout);

        /**
         * 加载操作
         */
        void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
    }

}