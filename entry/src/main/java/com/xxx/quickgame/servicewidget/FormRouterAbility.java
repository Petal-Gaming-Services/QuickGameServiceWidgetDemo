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
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

/**
 * 卡片路由（处理点击）的Ability
 */
public class FormRouterAbility extends Ability {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, Constant.HiLogLabelDomain.GENERAL_DOMAIN,
        "FormRouterAbility");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        HiLog.info(TAG, "onStart.");

        String formName = "";
        ZSONObject zsonObject = null;
        try {
            if (intent == null) {
                HiLog.error(TAG, "onStart intent is null.");
                terminateAbility();
                return;
            }

            String params = intent.getStringParam(Constant.FormParam.ACTIONS_KEY_PARAM);
            if (params == null || params.length() == 0) {
                HiLog.warn(TAG, "onStart params is empty.");
                terminateAbility();
                return;
            }

            zsonObject = ZSONObject.stringToZSON(params);
            if (zsonObject == null) {
                HiLog.warn(TAG, "onStart zsonObject is null.");
                terminateAbility();
                return;
            }

            formName = zsonObject.getString(Constant.FormParam.ACTIONS_PARAM_KEY_FORM_NAME);
        } catch (Throwable t) {
            HiLog.error(TAG, "onStart throwable: " + t.getMessage());
            terminateAbility();
        }

        if (Constant.FormName.OPEN_GAME_FORM.equals(formName)) {
            new OpenGameFormController().doAction(this, zsonObject);
        } else {
            HiLog.warn(TAG, "onStart unknown formName.");
            terminateAbility();
        }
    }
}
