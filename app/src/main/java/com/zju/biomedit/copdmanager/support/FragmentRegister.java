package com.zju.biomedit.copdmanager.support;

import android.os.Bundle;

/**
 * Created by ybj on 2016/5/13.
 */
public interface FragmentRegister {

    /**
     * 上一步的点击事件
     */
    void onPreviousStepButtonClick(String stepName);

    /**
     * 下一步的点击事件
     */
    void onNextStepButtonClick(String info, String stepName);

    /**
     * (进入注册)验证步骤中的点击事件
     */
    void onValidateStepButtonClick(String patientId, String patientName);

    /**
     * 完成的点击事件
     */
    void onFinishButtonClick(Bundle bundle);
}
