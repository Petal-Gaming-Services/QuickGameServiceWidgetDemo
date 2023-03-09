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

/**
 * 常量类
 */
public class Constant {
    /**
     * 卡片的名称值必须与config.json中abilities.forms.name值保持一致
     */
    public static class FormName {
        /**
         * 打开游戏服务卡片的卡片名称
         */
        public static final String OPEN_GAME_FORM = "OpenGameForm";
    }

    /**
     * 服务卡片json文件中的参数字段名定义
     */
    public static class FormParam {
        /**
         * actions中的一级字段params，系统定义的常量，不可更改
         */
        public static final String ACTIONS_KEY_PARAM = "params";

        /**
         * actions中的params下自定义的传递卡片名称信息的字段
         */
        public static final String ACTIONS_PARAM_KEY_FORM_NAME = "formName";
    }

    /**
     * 日志标签领域标识（过滤日志使用，取值范围0x0~0xFFFFF）
     */
    public static class HiLogLabelDomain {
        /**
         * 通用领域（如果应用功能复杂，可按功能定义不同的值，方便日志过滤）
         */
        public static final int GENERAL_DOMAIN = 0x00999;
    }
}
