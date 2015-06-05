package com.yjy998.ui.activity.main.popularize;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sp.lib.widget.list.refresh.PullToRefreshListView;
import com.yjy998.R;
import com.yjy998.ui.activity.main.more.WebViewActivity;
import com.yjy998.ui.view.ViewPagerTabItem;

import static com.sp.lib.widget.list.refresh.PullToRefreshBase.OnRefreshListener;

public class PopularizeView extends LinearLayout implements ListView.OnScrollListener, View.OnClickListener {
    private ListView mListView;//列表

    private ViewGroup floatTitle;//漂浮的title
    private ViewGroup fixedTitle;//固定的title

    private View headView;//listView的Head
    private View headBarFlow;//headView中的流水标题栏
    private View headBarInvited;//headView中邀请人的标题栏
    public TextView integerText;//整数部分
    public TextView floatText;//小数部分
    public TextView invitePeople;//累计邀请人数
    public PullToRefreshListView refreshListView;
    public OnSelectedListener listener;

    public PopularizeView(Context context) {
        this(context, null);
    }

    public PopularizeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopularizeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //使用垂直布局
        setOrientation(VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        headView = inflater.inflate(R.layout.popularize_head_view, null);
        //初始化固定的Title
        fixedTitle = (ViewGroup) headView.findViewById(R.id.popularizeTitleLayout);
        headBarFlow = headView.findViewById(R.id.headBarFlow);
        headBarInvited = headView.findViewById(R.id.headBarInvited);

        //创建漂浮的title
        floatTitle = (ViewGroup) inflater.inflate(R.layout.popularize_title, null);

        //find ids
        integerText = (TextView) headView.findViewById(R.id.integerText);
        floatText = (TextView) headView.findViewById(R.id.floatText);
        invitePeople = (TextView) headView.findViewById(R.id.invitePeople);

        //title栏中的 我邀请的人点击
        fixedTitle.findViewById(R.id.invitedPeople).setOnClickListener(this);
        floatTitle.findViewById(R.id.invitedPeople).setOnClickListener(this);
        //title栏中的 收益流水点击
        fixedTitle.findViewById(R.id.incomeFlow).setOnClickListener(this);
        floatTitle.findViewById(R.id.incomeFlow).setOnClickListener(this);
        //邀请好友点击事件
        headView.findViewById(R.id.invite).setOnClickListener(this);

        //活动说明
        headView.findViewById(R.id.introduce).setOnClickListener(this);

        //漂浮title先隐藏起来
        floatTitle.setVisibility(INVISIBLE);

        //初始化列表
        refreshListView = new PullToRefreshListView(getContext());
        refreshListView.setPullLoadEnabled(true);
        mListView = refreshListView.getRefreshableView();
        mListView.addHeaderView(headView);
        mListView.setOnScrollListener(this);
        mListView.setDividerHeight(2);
        mListView.setDivider(new ColorDrawable(Color.LTGRAY));
        addView(refreshListView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        toggle(false);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        refreshListView.setOnRefreshListener(listener);
    }

    public ListView getListView() {
        return mListView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        floatTitle.layout(l, 0, r, fixedTitle.getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        floatTitle.measure(widthMeasureSpec,
                floatTitle.getMeasuredHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (floatTitle.getVisibility() == VISIBLE) {
            drawChild(canvas, floatTitle, getDrawingTime());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int headBarTop = fixedTitle.getTop() + headView.getTop();
        int visibility;
        if (headBarTop < 0) {
            visibility = VISIBLE;
        } else {
            visibility = INVISIBLE;
        }
        if (visibility != floatTitle.getVisibility()) {
            floatTitle.setVisibility(visibility);
            invalidate();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite: {
                Context context = v.getContext();
                context.startActivity(new Intent(context, InviteFriends.class));
                break;
            }
            case R.id.introduce: {
                Context context = v.getContext();
                context.startActivity(new Intent(context, WebViewActivity.class)
                                .putExtra(WebViewActivity.EXTRA_URL, "http://m.yjy998.com/explain.html")
                );
                break;
            }
            case R.id.incomeFlow: {
                toggle(false);
                break;
            }
            case R.id.invitedPeople: {
                toggle(true);
                break;
            }
        }
    }

    void toggle(boolean isInvitedLayout) {

        toggleLayout(floatTitle, isInvitedLayout);
        toggleLayout(fixedTitle, isInvitedLayout);

        requestLayout();
        invalidate();

        if (listener != null) {
            listener.onSelected(isInvitedLayout ? 1 : 0);
        }
    }

    void toggleLayout(ViewGroup layout, boolean isInvited) {

        ViewGroup tabChild = (ViewGroup) layout.getChildAt(0);

        ViewPagerTabItem flow = (ViewPagerTabItem) tabChild.getChildAt(0);
        ViewPagerTabItem invited = (ViewPagerTabItem) tabChild.getChildAt(1);
        invited.onTabSelected(isInvited);
        flow.onTabSelected(!isInvited);

        View flowLayout = layout.getChildAt(1);
        View inviteLayout = layout.getChildAt(2);
        flowLayout.setVisibility(isInvited ? GONE : VISIBLE);
        inviteLayout.setVisibility(isInvited ? VISIBLE : GONE);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        floatTitle.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


    public interface OnSelectedListener {
        /**
         * @param i 0：返利流水 1：邀请好友
         */
        public void onSelected(int i);
    }
}
