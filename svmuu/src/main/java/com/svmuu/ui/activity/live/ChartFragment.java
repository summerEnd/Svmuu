package com.svmuu.ui.activity.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;
import com.svmuu.common.adapter.chat.ChatAdapter;
import com.svmuu.common.adapter.chat.ChatItemDec;
import com.svmuu.common.entity.Chat;
import com.svmuu.ui.BaseFragment;
import com.svmuu.ui.widget.GifDrawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ChartFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private TextView send;
    private EditText editContent;
    private List<Chat> data;
    private ChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    protected void initialize() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        send = (TextView) findViewById(R.id.send);
        editContent = (EditText) findViewById(R.id.editContent);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=editContent.getText().toString();

                try {
                    int position=Integer.valueOf(content);
                    recyclerView.scrollToPosition(position);
                } catch (Exception e) {
                    Chat chat = new Chat();
                    chat.name = "LINCOLN";
                    chat.job = "圈主";
                    chat.time = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
                    chat.msg = editContent.getText().toString();
                    chat.isSelf = true;
                    data.add(chat);
                    adapter.notifyItemInserted(data.size());
                    editContent.setText(null);
                    recyclerView.scrollToPosition(data.size()-1);
                }
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
        data = getData();
        adapter = new ChatAdapter(getActivity(), data);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ChatItemDec());


    }

    private List<Chat> getData() {
        ArrayList<Chat> data = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            Chat object = new Chat();
            object.isSelf = random.nextBoolean();
            if (object.isSelf) {
                object.name = "Lincoln";
                object.job = "圈主";
            } else {
                object.name = createString(random, 8);
                switch (random.nextInt(6)) {
                    case 0: {
                        object.job = "圈主";
                        break;
                    }
                    case 1: {
                        object.job = "管理员";
                        break;
                    }
                    default:
                        object.job = "";
                }
            }
            object.msg = createString(random, random.nextInt(50));
            object.time = random.nextInt(24) + ":" + random.nextInt(60);
            data.add(object);
        }
        return data;
    }

    String createString(Random random, int length) {
        char[] a = new char[length];
        for (int i = 0; i < length; i++) {
            a[i] = (char) ('0' + random.nextInt(36));
        }
        return new String(a);
    }


}
