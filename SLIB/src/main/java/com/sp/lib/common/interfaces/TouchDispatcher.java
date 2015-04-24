package com.sp.lib.common.interfaces;

/**
 * <pre>
 * 触屏事件分发，主要是为Fragment提供触屏拦截功能。
 * </pre>
 */
public interface TouchDispatcher {

    public void register(TouchObserver dispatcher);

    public TouchObserver unRegister(TouchObserver dispatcher);


}
