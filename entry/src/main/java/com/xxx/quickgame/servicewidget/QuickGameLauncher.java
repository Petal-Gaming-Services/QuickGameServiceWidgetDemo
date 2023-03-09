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

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;

/**
 * 快游戏启动类
 */
public class QuickGameLauncher {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, Constant.HiLogLabelDomain.GENERAL_DOMAIN,
        "QuickGameLauncher");

    /**
     * 打开快游戏的deepLink，其中“com.xxx.game”需修改为真正的快游戏包名
     */
    private static final String OPEN_QUICK_GAME_DEEP_LINK = "hwfastapp://com.xxx.game";

    /**
     * 快应用中心包名
     */
    private static final String FAST_APP_NAME = "com.huawei.fastapp";

    /**
     * 跳转的action
     */
    private static final String ACTION_JUMP = "android.intent.action.VIEW";

    /**
     * 上次响应跳转时间
     */
    private static long lastResponseTime = 0L;

    /**
     * 跳转外部最小时间间隔，规避快速点击操作
     */
    private static final long MIN_RESPONSE_TIME = 1000L;

    /**
     * 打开快游戏deeplink将携带的卡片宿主包名的字段
     */
    private static final String FA_CALLING_PKG = "faCallingPkg";

    /**
     * 通过快应用中心打开快游戏
     *
     * @param ability Ability上下文
     * @param hostPackageName 卡片所在宿主的包名
     */
    public void openFastGame(Ability ability, String hostPackageName) {
        HiLog.info(TAG, "openFastGame start.");
        if (interceptJump()) {
            HiLog.info(TAG, "openFastGame clicks too fast.");
            return;
        }

        if (ability == null || ability.isTerminating()) {
            HiLog.warn(TAG, "openFastGame ability is null or isTerminating.");
            return;
        }

        Uri uri = Uri.parse(OPEN_QUICK_GAME_DEEP_LINK);
        if (hostPackageName != null && hostPackageName.length() != 0) {
            uri = uri.makeBuilder().appendDecodedQueryParam(FA_CALLING_PKG, Uri.encode(hostPackageName)).build();
        }
        HiLog.info(TAG, "openFastGame uri: " + uri.toString());
        Operation operation = new Intent.OperationBuilder().withUri(uri)
            .withAction(ACTION_JUMP)
            .withBundleName(FAST_APP_NAME)
            .withFlags(Intent.CLONE_BUNDLE | Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_ABILITY_CLEAR_MISSION)
            .build();
        Intent intent = new Intent();
        intent.setOperation(operation);
        try {
            ability.startAbility(intent);
        } catch (Exception e) {
            HiLog.error(TAG, "openFastGame startAbility exception: " + e.getMessage());
        }

        HiLog.info(TAG, "openFastGame end.");
    }

    /**
     * 拦截此次跳转动作
     *
     * @return true 如果1s内响应跳转频次过高需要拦截
     */
    private static boolean interceptJump() {
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - lastResponseTime) > MIN_RESPONSE_TIME) {
            lastResponseTime = currentTime;
            return false;
        }
        return true;
    }
}