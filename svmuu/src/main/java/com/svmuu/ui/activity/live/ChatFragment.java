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
import com.svmuu.common.adapter.chat.ChatAdapterImpl;
import com.svmuu.common.adapter.chat.ChatItemDec;
import com.svmuu.common.adapter.chat.ChatParams;
import com.svmuu.common.entity.Chat;
import com.svmuu.ui.BaseFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatFragment extends BaseFragment implements ChatManager.Callback {

    private RecyclerView recyclerView;
    private EditText editContent;
    private List<Chat> data = new ArrayList<>();
    private ChatAdapterImpl adapter;
    ChatManager mChatManager;
    LinkedList<Chat> tempChatPool = new LinkedList<>();
    private static ChatParams mSharedParams;

    /**
     * 获取当前聊天的参数
     * 包括:圈号，自己对于当前圈子的粉丝类型
     */
    public static ChatParams getSharedChatParams(){
        if (mSharedParams==null){
            mSharedParams=new ChatParams();
        }
        return mSharedParams;
    }

    public static ChatFragment newInstance(String circleId) {
        ChatFragment fragment = new ChatFragment();
        getSharedChatParams().quanzhu_id=circleId;
        return fragment;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mChatManager = new ChatManager(activity, this);
        mChatManager.setCircleId(getSharedChatParams().quanzhu_id);
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
        //todo  测试

        adapter = new ChatAdapterImpl(getActivity(), data);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ChatItemDec());

    }


    @Override
    public void onMessageAdded(Chat chat) {
        data.add(chat);
        tempChatPool.add(chat);
        recyclerView.scrollToPosition(data.size() - 1);
        adapter.notifyItemInserted(data.size() - 1);
        editContent.setText(null);
    }

    @Override
    public void onNewMessageLoaded(ArrayList<Chat> newMessages) {

        if (tempChatPool.size() > 0) {
            data.removeAll(tempChatPool);
            tempChatPool.clear();
            adapter.notifyDataSetChanged();
        }

        int fromPosition = data.size();
        data.addAll(newMessages);
        adapter.notifyItemRangeInserted(fromPosition, newMessages.size());
        recyclerView.scrollToPosition(data.size() - 1);
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
    public void onResume() {
        super.onResume();
        mChatManager.updateMessageList(4 * 1000);
    }

    @Override
    public void onStop() {
        super.onStop();
        mChatManager.recycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatManager != null) {
            mChatManager.recycle();
        }
    }
}
