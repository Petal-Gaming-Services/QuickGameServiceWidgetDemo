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
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.IntentParams;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

/**
 * 打开游戏服务卡片控制器
 */
public class OpenGameFormController {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, Constant.HiLogLabelDomain.GENERAL_DOMAIN,
        "OpenGameFormController");

    /**
     * 创建卡片数据
     *
     * @param intentParams IntentParams
     * @return 打开游戏卡片数据
     */
    public ProviderFormInfo bindFormData(IntentParams intentParams) {
        HiLog.info(TAG, "bindFormData.");
        ZSONObject zsonObject = new ZSONObject();
        new HostPackageNameParser().appendHostPackageNameToZsonObject(intentParams, zsonObject);
        ProviderFormInfo providerFormInfo = new ProviderFormInfo();
        providerFormInfo.setJsBindingData(new FormBindingData(zsonObject));
        return providerFormInfo;
    }

    /**
     * 处理卡片的点击
     *
     * @param ability Ability
     * @param zsonObject 参数
     */
    public void doAction(Ability ability, ZSONObject zsonObject) {
        HiLog.info(TAG, "doAction.");
        if (ability == null || ability.isTerminating()) {
            HiLog.warn(TAG, "doAction ability is null or isTerminating.");
            return;
        }

        new QuickGameLauncher().openFastGame(ability, new HostPackageNameParser().getHostPackageName(zsonObject));
        ability.terminateAbility();
    }
}