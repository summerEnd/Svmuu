package com.sp.lib.common.support.update;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.sp.lib.common.exception.SlibInitialiseException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * update the app
 */
public abstract class UpdateManager {
    private static Context mContext;
    private static UpdateCallback mCallback;
    private static ExecutorService service= Executors.newSingleThreadExecutor();

    /**
     * init the UpdateManager
     *
     * @param context
     */
    public static void init(Context context, UpdateCallback callback) {
        UpdateManager.mContext = context;
        UpdateManager.mCallback = callback;
    }

    public static void start() {

        if (mContext == null) {
            throw new SlibInitialiseException(UpdateManager.class);
        }


        service.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
        if (!mCallback.checkNewestVersion()) {
            if (mCallback.forceUpdate()) {

            } else {

            }
        }
    }




    /**
     * the callback used when {@link com.sp.lib.common.support.update.UpdateManager} start;
     */
    public abstract class UpdateCallback {
        /**
         * Check if the app is the latest version.do your check here,this method cost time.
         *
         * @return return true the app is newest,false otherwise
         */
        public abstract boolean checkNewestVersion();

        /**
         * @return return true force update,false otherwise
         */
        public abstract boolean forceUpdate();

        public void post(String url,RequestParams params){
        }
    }


}
