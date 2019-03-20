/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jitta
 */
@Getter
@Setter
@Builder
public class UserAgent {

    private String deviceTypeKey; //COMPUTER, MOBILE, TABLET, WEARABLE
    private String deviceTypeName; //Computer

    private String browserTypeKey; //WEB_BROWSER, MOBILE_BROWSER
    private String browserTypeName; //Browser
    private String browserKey; //CHROME
    private String browserName; //Chrome
    private String browserGroupKey; //CHROME
    private String browserGroupName; //Chrome
    private String browserRenderingEngine; //WEBKIT
    private String browserManufacturerKey; //GOOGLE
    private String browserManufacturerName; //Google Inc.

    private String osManufacturerKey; //MICROSOFT, GOOGLE, APPLE
    private String osManufacturerName; //Microsoft Corporation
    private String osGroupKey; //WINDOWS, LINUX, MAC_OS
    private String osGroupName; //Windows
    private String osKey; //WINDOWS_10
    private String osName; //Windows 10

    private String agentVersion; //52.0.2743.116
    private String agentMajorVersion; //52
    private String agentMinorVersion; //0

    @Override
    public String toString() {
        return "UserAgent{" + "deviceTypeKey=" + deviceTypeKey + ", deviceTypeName=" + deviceTypeName + ", browserTypeKey=" + browserTypeKey + ", browserTypeName=" + browserTypeName + ", browserKey=" + browserKey + ", browserName=" + browserName + ", browserGroupKey=" + browserGroupKey + ", browserGroupName=" + browserGroupName + ", browserRenderingEngine=" + browserRenderingEngine + ", browserManufacturerKey=" + browserManufacturerKey + ", browserManufacturerName=" + browserManufacturerName + ", osManufacturerKey=" + osManufacturerKey + ", osManufacturerName=" + osManufacturerName + ", osGroupKey=" + osGroupKey + ", osGroupName=" + osGroupName + ", osKey=" + osKey + ", osName=" + osName + ", agentVersion=" + agentVersion + ", agentMajorVersion=" + agentMajorVersion + ", agentMinorVersion=" + agentMinorVersion + '}';
    }

}
