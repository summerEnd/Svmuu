package com.sp.lib.activity.album;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.sp.lib.R;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.sp.lib.activity.album.PhotoDirAdapter.PhotoDirInfo;

public class PhotoDirWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    View contentView;
    ListView list;
    Context context;
    List<PhotoDirInfo> dirs;
    PhotoDirAdapter adapter;
    public PhotoDirWindow(Context context, List<PhotoDirInfo> dirs) {
        super(context);
        this.context = context;
        this.dirs = dirs;
        setHeight(MATCH_PARENT);
        setWidth(MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x80000000));
        contentView = View.inflate(context, R.layout.photo_ablum_layout, null);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        list = (ListView) contentView.findViewById(R.id.listView);
        adapter= new PhotoDirAdapter(context, dirs);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        setContentView(contentView);
    }


    /**
     * 动画弹出
     *
     * @param v
     */
    public void showWithAnimation(View v) {
        showAtLocation(v, Gravity.TOP, 0, -v.getHeight());
        Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_up_in);
        animation.setDuration(200);
        list.startAnimation(animation);
    }

    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        onSelected(dirs.get(position));
        adapter.select(position);
    }

    protected void onSelected(PhotoDirInfo info) {

    }
}
