package com.yjy998.ui.activity.main.popularize;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshListView;
import com.yjy998.R;
import com.yjy998.ui.activity.main.more.WebViewActivity;

import static com.sp.lib.widget.list.refresh.PullToRefreshBase.OnRefreshListener;

public class PopularizeView extends LinearLayout implements ListView.OnScrollListener {
    private ListView mListView;//列表
    private View floatLayout;//漂浮的标题
    private View headView;//listView的Head
    private View headBar;//headView中的标题栏
    public TextView integerText;//整数部分
    public TextView floatText;//小数部分
    public TextView invitePeople;//累计邀请人数
    public PullToRefreshListView refreshListView;

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
        headBar = headView.findViewById(R.id.headBar);
        floatLayout = inflater.inflate(R.layout.popularize_float, null);

        //find ids
        integerText = (TextView) headView.findViewById(R.id.integerText);
        floatText = (TextView) headView.findViewById(R.id.floatText);
        invitePeople = (TextView) headView.findViewById(R.id.invitePeople);

        headView.findViewById(R.id.invite).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //邀请好友点击事件
                Context context = v.getContext();
                context.startActivity(new Intent(context, InviteFriends.class));
            }
        });

        headView.findViewById(R.id.introduce).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //活动说明
                Context context = v.getContext();
                context.startActivity(new Intent(context, WebViewActivity.class)
                                .putExtra(WebViewActivity.EXTRA_URL, "http://m.yjy998.com/explain.html")
                );
            }
        });


        //漂浮title先隐藏起来
        floatLayout.setVisibility(INVISIBLE);
        refreshListView = new PullToRefreshListView(getContext());
        refreshListView.setPullLoadEnabled(true);
        mListView = refreshListView.getRefreshableView();
        mListView.addHeaderView(headView);
        mListView.setOnScrollListener(this);
        mListView.setDividerHeight(2);
        mListView.setDivider(new ColorDrawable(Color.LTGRAY));
        addView(refreshListView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
        floatLayout.layout(l, 0, r, floatLayout.getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        floatLayout.measure(widthMeasureSpec,
                floatLayout.getMeasuredHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (floatLayout.getVisibility() == VISIBLE) {
            drawChild(canvas, floatLayout, getDrawingTime());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int headBarTop = headBar.getTop() + headView.getTop();
        int visibility;
        if (headBarTop < 0) {
            visibility = VISIBLE;
        } else {
            visibility = INVISIBLE;
        }
        if (visibility != floatLayout.getVisibility()) {
            floatLayout.setVisibility(visibility);
            invalidate();
        }
    }
}
