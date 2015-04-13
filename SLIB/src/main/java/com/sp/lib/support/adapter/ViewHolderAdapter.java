package com.sp.lib.support.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 基于ViewHolder对AbsListView进行优化，能大幅提升AbsListView性能
 * 例如：
 * <pre class="prettyprint">
 * class MyAdapter extends ViewHolderAdapter<People, ViewHolder> {
 * public MyAdapter(List<People> data) {
 * super(data);
 * }
 * public View createView() {
 * return getLayoutInflater().inflate(R.layout.test, null);
 * }
 * public ViewHolder createHolder(View convertView) {
 * ViewHolder holder = new ViewHolder();
 * holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
 * holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
 * return holder;
 * }
 * public void setData(ViewHolder holder, People data) {
 * holder.tv_age.setText(data.age);
 * holder.tv_name.setText(data.name);
 * }
 * 通过<pre class="prettyprint">listView.setAdapter(new MyAdapter(data));</pre>就可以使用了
 *
 * @param <H> ViewHolder
 * @param <T> List集合中元素的原型
 */
public abstract class ViewHolderAdapter<T, H> extends BaseAdapter {
    private List<T> data;

    ViewHolderAdapter() {

    }

    public ViewHolderAdapter(List<T> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }


    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            convertView = createView();
            holder = createHolder(convertView);
        } else {
            holder = (H) convertView.getTag();
        }
        setData(convertView, holder, getItem(position));
        convertView.setTag(holder);
        return convertView;
    }

    /**
     * @return View
     */
    public abstract View createView();

    /**
     * 创建一个ViewHolder，这个对象会作为convertView的tag.
     * 你应该在这里创建一个属于你自己的ViewHolder,并将需要的控件调用convertView的findViewById()方法赋值给ViewHolder中的变量，;
     *
     * @param convertView 视图
     */
    public abstract H createHolder(View convertView);

    /**
     * @param holder 对应当前item的holder
     * @param data   对应当前item的数据
     */
    public abstract void setData(View convertView, H holder, T data);


}
