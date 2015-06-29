package com.svmuu.ui.activity.live;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.svmuu.R;
import com.svmuu.common.ChatManager;
import com.svmuu.common.adapter.chat.ChatAdapter;
import com.svmuu.common.adapter.chat.ChatItemDec;
import com.svmuu.common.entity.Chat;
import com.svmuu.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends BaseFragment implements ChatManager.Callback {

    private RecyclerView recyclerView;
    private EditText editContent;
    private List<Chat> data = new ArrayList<>();
    private ChatAdapter adapter;
    private String quanzhu_id;
    ChatManager mChatManager;

    public static ChatFragment newInstance(String circleId) {
        ChatFragment fragment = new ChatFragment();
        fragment.setCircleId(circleId);
        return fragment;
    }

    public void setCircleId(String circle_id) {
        this.quanzhu_id = circle_id;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mChatManager = new ChatManager(activity, this);
        mChatManager.setCircleId(quanzhu_id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    protected void initialize() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        editContent = (EditText) findViewById(R.id.editContent);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editContent.getText().toString();
                mChatManager.sendMessage("1", content);
            }
        });

        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }


        };
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layout);
        adapter = new ChatAdapter(getActivity(), data);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ChatItemDec());

        requestRefresh();
    }

    @Override
    protected void refresh() {

        mChatManager.getNewMessages();
    }

    @Override
    public void onMessageAdded() {
        mChatManager.getNewMessages();
    }

    @Override
    public void onNewMessageLoaded(ArrayList<Chat> newMessages) {
        int fromPosition = data.size() - 1;
        data.addAll(newMessages);
        adapter.notifyItemRangeInserted(fromPosition, newMessages.size());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (data.size() == 0) {
                mChatManager.getNewMessages();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatManager != null) {
            mChatManager.recycle();
        }
    }
}
