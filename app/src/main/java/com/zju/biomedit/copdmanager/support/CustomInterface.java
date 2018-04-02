package com.zju.biomedit.copdmanager.support;

/**
 * Created by ybj on 2015/12/24.
 */
public class CustomInterface {

    public interface ConfirmButtonOnClickListener{
        void onSucceedCompleted(int code, String info);
    }

    public interface UpdateButtonOnClickListener{
        void onClick();
    }
}
