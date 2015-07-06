package com.svmuu.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {
    private String title;
    private boolean isRefreshRequest = false;
    private boolean isUIRefreshRequest=false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 这个方法应该在{@link Fragment#onViewCreated(View, android.os.Bundle) onViewCreated }之后调用，否则会返回null
     */
    public View findViewById(int id) {
        if (getView() == null) {
            return null;
        }
        return getView().findViewById(id);
    }

    /**
     * 全部刷新，包括网络请求等等
     */
    protected void refresh() {
       refreshUI();
    }

    /**
     * 仅仅刷新UI
     */
    public void refreshUI() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        if (isRefreshRequest) {
            refresh();
            isRefreshRequest = false;
        }
        if (isUIRefreshRequest){
            refreshUI();
            isUIRefreshRequest=false;
        }
    }

    /**
     * 初始化，这时View已经创建，做一些初始化操作.findId等等
     */
    protected void initialize() {

    }

    /**
     * 请求刷新页面，如果Fragment还没有创建View，则当View创建成功时刷新
     */
    public void requestRefresh() {
        if (getActivity() != null) {
            refresh();
        } else {
            isRefreshRequest = true;
        }
    }

    public void requestRefreshUI(){
        if (getView()!=null){
            refreshUI();
        }else{
            isUIRefreshRequest=true;
        }
    }
}
