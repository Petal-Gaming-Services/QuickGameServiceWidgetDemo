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
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

/**
 * 快游戏启动类
 */
public class QuickGameLauncher {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, Constant.HiLogLabelDomain.GENERAL_DOMAIN,
        "QuickGameLauncher");

    /**
     * 快游戏包名，将“com.xxx.game”修改为真正要打开的快游戏包名
     */
    private static final String QUICK_GAME_PACKAGE_NAME = "com.xxx.game";

    /**
     * 快应用中心打开快游戏的deeplink，__QuickGamePackageName__会被替换成快游戏包名
     */
    private static final String OPEN_QUICK_GAME_DEEP_LINK_BY_QUICK_CENTER = "hwfastapp://__QuickGamePackageName__";

    /**
     * 花瓣轻游打开快游戏的deeplink，__QuickGamePackageName__会被替换成快游戏包名
     */
    private static final String OPEN_QUICK_GAME_DEEP_LINK_BY_LITEGAMES = "petallitegames://com.petal.litegames?method=openApp&appType=0&packageName=__QuickGamePackageName__";

    /**
     * 花瓣轻游包名
     */
    private static final String LITEGAMES_APP_NAME = "com.petal.litegames";

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

        Uri uri;
        String bundleName;
        if (checkInstall(ability, LITEGAMES_APP_NAME)) {
            uri = Uri.parse(OPEN_QUICK_GAME_DEEP_LINK_BY_LITEGAMES.replace("__QuickGamePackageName__", QUICK_GAME_PACKAGE_NAME));
            if (hostPackageName != null && hostPackageName.length() != 0) {
                ZSONArray paramsForGameZsonArray = new ZSONArray();
                ZSONObject faCallingPkgZsonObject = new ZSONObject();
                faCallingPkgZsonObject.put("name", FA_CALLING_PKG);
                faCallingPkgZsonObject.put("type", "String");
                faCallingPkgZsonObject.put("value", hostPackageName);
                paramsForGameZsonArray.add(faCallingPkgZsonObject);
                ZSONObject params = new ZSONObject();
                params.put("paramsForGame", paramsForGameZsonArray);
                uri = uri.makeBuilder().appendDecodedQueryParam("params", params.toString()).build();
            }
            bundleName = LITEGAMES_APP_NAME;
        } else {
            uri = Uri.parse(OPEN_QUICK_GAME_DEEP_LINK_BY_QUICK_CENTER.replace("__QuickGamePackageName__", QUICK_GAME_PACKAGE_NAME));
            if (hostPackageName != null && hostPackageName.length() != 0) {
                uri = uri.makeBuilder().appendDecodedQueryParam(FA_CALLING_PKG, hostPackageName).build();
            }
            bundleName = FAST_APP_NAME;
        }
        HiLog.info(TAG, "openFastGame uri: " + uri.toString() + ", bundleName: " + bundleName);
        Operation operation = new Intent.OperationBuilder().withUri(uri)
            .withAction(ACTION_JUMP)
            .withBundleName(bundleName)
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

    /**
     * 判断应用是否安装
     *
     * @param ability     ability
     * @param packageName 包名
     * @return 是否安装
     */
    private boolean checkInstall(Ability ability, String packageName) {
        try {
            IBundleManager bundleManager = ability.getBundleManager();
            boolean isApplicationEnabled = bundleManager.isApplicationEnabled(packageName);
            HiLog.info(TAG, "checkInstall isApplicationEnabled " + isApplicationEnabled);
            return isApplicationEnabled;
        } catch (IllegalArgumentException exception) {
            HiLog.error(TAG, "checkInstall IllegalArgumentException");
        } catch (Exception e) {
            HiLog.error(TAG, "checkInstall Exception");
        }
        return false;
    }
}