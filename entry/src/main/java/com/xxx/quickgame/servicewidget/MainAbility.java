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
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * 主入口Ability 同时也是服务卡片创建、删除等回调的Ability
 */
public class MainAbility extends Ability {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, Constant.HiLogLabelDomain.GENERAL_DOMAIN,
        "MainAbility");

    private static final int INVALID_FORM_ID = -1;

    @Override
    public void onStart(Intent intent) {
        HiLog.info(TAG, "onStart.");
        super.onStart(intent);
        new QuickGameLauncher().openFastGame(this, null);
        terminateAbility();
    }

    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {
        HiLog.info(TAG, "onCreateForm.");
        long formId;
        String formName;
        IntentParams intentParams;
        try {
            formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
            formName = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
            intentParams = intent.getParam(AbilitySlice.PARAM_FORM_CUSTOMIZE_KEY);
        } catch (Throwable t) {
            HiLog.error(TAG, "onCreateForm throwable: " + t.getMessage());
            return null;
        }

        HiLog.info(TAG, "onCreateForm formId: " + formId + ", formName: " + formName);
        if (Constant.FormName.OPEN_GAME_FORM.equals(formName)) {
            return new OpenGameFormController().bindFormData(intentParams);
        } else {
            HiLog.warn(TAG, "onCreateForm unknown formName.");
            return null;
        }
    }
}