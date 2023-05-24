/*
 * Copyright 2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.xxx.quickgame.servicewidget;

import ohos.aafwk.content.IntentParams;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

/**
 * 宿主包名解析器
 */
public class HostPackageNameParser {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, Constant.HiLogLabelDomain.GENERAL_DOMAIN,
        "HostPackageNameParser");

    /**
     * 传递卡片所在的宿主（服务卡片使用方）应用（如桌面，小艺建议）的包名字段；
     * 如果宿主创建卡片时通过该字段传递了宿主自身包名，则在点击服务卡片打开游戏时，会将该字段作为参数拼接到打开游戏的deeplink中
     */
    private static final String CALLING_PACKAGE_NAME = "calling_package_name";

    /**
     * 负一屏提供的卡片入参
     */
    private final static String FORM_INTENT_INFO = "formIntentInfo";

    /**
     * 负一屏提供的使用方包名字段
     */
    private final static String HW_CHANNEL_ID = "hwChannelId";

    private static final String HOST_PACKAGE_NAME = "hostPackageName";

    /**
     * 从ZSONObject从获取宿主包名
     *
     * @param zsonObject ZSONObject
     * @return 宿主包名
     */
    public String getHostPackageName(ZSONObject zsonObject) {
        if (zsonObject == null) {
            return null;
        }
        return zsonObject.getString(HOST_PACKAGE_NAME);
    }

    /**
     * 如果IntentParams包含宿主包名，则将宿主包名添加到zsonObject中
     *
     * @param intentParams IntentParams
     * @param zsonObject ZSONObject
     */
    public void appendHostPackageNameToZsonObject(IntentParams intentParams, ZSONObject zsonObject) {
        if (zsonObject == null) {
            return;
        }
        String hostPackageName = null;
        if (intentParams != null) {
            Object object = intentParams.getParam(CALLING_PACKAGE_NAME);
            if (object instanceof String) {
                hostPackageName = (String) object;
            }

            if (isEmpty(hostPackageName)) {
                Object info = intentParams.getParam(FORM_INTENT_INFO);
                if (info instanceof String) {
                    try {
                        ZSONObject formIntentInfo = ZSONObject.stringToZSON((String) info);
                        if (formIntentInfo != null) {
                            hostPackageName = formIntentInfo.getString(HW_CHANNEL_ID);
                        }
                    } catch (Throwable t) {
                        HiLog.error(TAG, "appendHostPackageNameToZsonObject throwable: " + t.getMessage());
                    }
                }
            }
        }

        if (isEmpty(hostPackageName)) {
            HiLog.info(TAG, "appendHostPackageNameToZsonObject hostPackageName: " + hostPackageName);
            zsonObject.put(HostPackageNameParser.HOST_PACKAGE_NAME, hostPackageName);
        }
    }

    private boolean isEmpty(String hostPackageName) {
        return hostPackageName != null && hostPackageName.length() != 0;
    }
}