# QuickGameServiceWidgetDemo

## Table of Contents

* [Introduction](#Introduction)
* [Installation](#Installation)
* [Supported Environments](#Supported_Environments)
* [Configuration](#Configuration)
* [Sample Code](#Sample_Code)
* [License](#License)

<a id="Introduction"></a>

## Introduction

Developing service widget from scratch means that a lot of knowledge about Harmony's applications/atomic services needs to be dabbled, for example, the meaning of each configuration item needs to be known. Its workload is huge. Aiming at this pain point and considering the specific scenario of the quick game, we completed the creation of the basic project and developed a basic service widget. Based on the sample project, the developer only needs to make some simple modifications to the picture, bundle name, jump link, etc., to have a service widget that can be clicked directly to the quick game.

<a id="Installation"></a>

## Installation

Before running the sample code, please install DevEco Studio 3.0 Release and later. In addition, you need to install Java, JS, and Toolchains under HarmonyOS SDK API Version 6 in DevEco Studio. Then you can use DevEco Studio to open the sample project.

<a id="Supported_Environments"></a>

## Supported Environments

 DevEco Studio 3.0 Release and later version is recommended.

<a id="Configuration"></a>

## Configuration

In order to generate a unique service widget, you may need to modify some of the following resources or content, which can be found through global search.

| resources or content | describes |
|:----- |:-------|
| service_widget_icon.png | Service widget icon. |
| app_icon.png | Quick game icon. |
| app_name | Quick game name. |
| big_image.png | Promotional icon. |
| OpenGameForm-2x2.png | Service widget snapshot, which can be displayed on a negative screen, and the atomic services will be officially downloaded and installed in the background after the user clicks. |
| bundleName | The package name of the atomic services, used to identify the uniqueness of the application, must end with **.hmservice**. |

<a id="Sample_Code"></a>

## Sample Code

After opening the sample project in DevEco Studio, it is recommended to switch the project directory structure to the "Ohos" view, that is, select "Project > Ohos" in the upper left corner. Then you can see the java, js, resources, and configuration folders in the entry module.

java folder :

- Constant.java : Defines constants that are used in multiple classes.
- FormRouterAbility.java : Clicking on the service widget will open the transparent page, which is used to dispatch the click event of the service widget.
- HostPackageNameParser.java : Host (service widget user) package name parsing class.
- MainAbility.java : Atomic services main Ability, which is the container for the home page display, and receives the callback for creating service widget.
- MainAbilitySlice.java : The content displayed on the home page.
- OpenGameFormController.java : Provides the required data for the service widget already in the sample, and handles the click response.
- QuickGameLauncher.java : The quick game startup class is responsible for launching the quick game.

js folder :

- common folder : Store image resource files of service widget.
- i18n folder : String resource file for storing service widget.
- pages/index folder : Define the style, layout, data and click jump configuration of the service widget.

resources folder :

- Store resources other than service widget, including colors, strings, layouts, pictures, etc.

configuration folder  :

- config.json : Configuration information such as bundle name, version number, Ability, service widget, etc..
- other file : Compilation configuration, obfuscation rule configuration and other files.

In addition to the entry module, you can also see the EntryCard and configuration folders. A service widget snapshot is placed in the EntryCard folder, and configuration stores project-level configuration files.

<a id="License"></a>

## License

QuickGameServiceWidgetDemo example is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
