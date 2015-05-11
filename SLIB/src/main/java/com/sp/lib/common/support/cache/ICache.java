package com.sp.lib.common.support.cache;


import java.util.List;

public interface ICache<K, D> {


    /**
     * 从缓存中读取一个
     *
     * @param key
     * @return
     */
    public D read(K key);

    /**
     * 讲一个object写入缓存
     *
     * @param key
     * @param data
     * @return
     */
    public Object write(K key, D data);

    /**
     * 清空缓存
     */
    public void clear();

    /**
     * 删除指定缓存
     *
     * @param key
     * @return
     */
    public D remove(K key);

    /**
     * 将缓存中的数据列出来
     * @return
     */
    public List<D> listObjects();

    /**
     * 将缓存中的key列出来
     * @return
     */
    public List<K> listKeys();

    /**
     * 缓存的大小
     * @return
     */
    public long size();

}
