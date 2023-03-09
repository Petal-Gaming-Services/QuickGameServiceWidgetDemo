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

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.window.service.WindowManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * 主Ability的Slice
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, Constant.HiLogLabelDomain.GENERAL_DOMAIN,
        "MainAbilitySlice");

    private static final int CORNER_RADIUS_8 = 8;

    private static final int CORNER_RADIUS_16 = 16;

    @Override
    public void onStart(Intent intent) {
        HiLog.info(TAG, "onStart.");
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        WindowManager.getInstance()
            .getTopWindow()
            .get()
            .setStatusBarColor(getColor(ResourceTable.Color_entry_ability_background_color));
        Image appIconImage = (Image) findComponentById(ResourceTable.Id_app_icon_image);
        appIconImage.setCornerRadius(AttrHelper.vp2px(CORNER_RADIUS_8, getContext()));
        Image centerBigImage = (Image) findComponentById(ResourceTable.Id_center_big_image);
        centerBigImage.setCornerRadius(AttrHelper.vp2px(CORNER_RADIUS_16, getContext()));
        Button openGameBtn = (Button) findComponentById(ResourceTable.Id_open_game_btn);
        openGameBtn.setClickedListener(new SingleClickedListener() {
            @Override
            public void onSingleClick(Component component) {
                new QuickGameLauncher().openFastGame(MainAbilitySlice.this.getAbility(), null);
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    /**
     * 避免重复点击类
     */
    private abstract static class SingleClickedListener implements Component.ClickedListener {
        /**
         * 两次点击最小时间间隔 单位毫秒
         */
        private static final long MINIMUM_TIME_BETWEEN_CLICKS = 1000L;

        private long lastClickTime = 0L;

        @Override
        public void onClick(Component component) {
            if (component == null) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - lastClickTime) > MINIMUM_TIME_BETWEEN_CLICKS) {
                lastClickTime = currentTime;
                onSingleClick(component);
            }
        }

        /**
         * 单次点击
         *
         * @param component 点击的组件
         */
        public abstract void onSingleClick(Component component);
    }
}
