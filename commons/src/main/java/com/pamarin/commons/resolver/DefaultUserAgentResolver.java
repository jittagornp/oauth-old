/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.Manufacturer;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.Version;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Component
public class DefaultUserAgentResolver implements UserAgentResolver {

    @Override
    public UserAgent resolve(HttpServletRequest httpReq) {
        String header = httpReq.getHeader("User-Agent");
        if (!hasText(header)) {
            return null;
        }
        eu.bitwalker.useragentutils.UserAgent ua = eu.bitwalker.useragentutils.UserAgent.parseUserAgentString(header);
        if (ua == null) {
            throw new IllegalArgumentException("Invalid User-Agent http request header.");
        }

        Version agentVersion = ua.getBrowserVersion();
        Browser browser = ua.getBrowser();
        OperatingSystem os = ua.getOperatingSystem();

        BrowserType browserType = null;
        Manufacturer browserManufacturer = null;
        Browser browserGroup = null;
        if (browser != null) {
            browserType = browser.getBrowserType();
            browserManufacturer = browser.getManufacturer();
            browserGroup = browser.getGroup();
        }

        DeviceType deviceType = null;
        Manufacturer osManufacturer = null;
        OperatingSystem osGroup = null;
        if (os != null) {
            deviceType = os.getDeviceType();
            osManufacturer = os.getManufacturer();
            osGroup = os.getGroup();
        }

        UserAgent userAgent = UserAgent.builder().build();
        setDeviceType2Device(deviceType, userAgent);
        setOsGroup2Device(osGroup, userAgent);
        setOs2Device(os, userAgent);
        setOsManufacturer2Device(osManufacturer, userAgent);
        setBrowserGroup2Device(browserGroup, userAgent);
        setBrowser2Device(browser, userAgent);
        setBrowserType2Device(browserType, userAgent);
        setBrowserManufacturer2Device(browserManufacturer, userAgent);
        setAgentVersion2Device(agentVersion, userAgent);

        return userAgent;
    }

    private void setDeviceType2Device(DeviceType deviceType, UserAgent userAgent) {
        if (deviceType != null) {
            userAgent.setDeviceTypeKey(deviceType.name());
            userAgent.setDeviceTypeName(deviceType.getName());
        }
    }

    private void setOsGroup2Device(OperatingSystem osGroup, UserAgent userAgent) {
        if (osGroup != null) {
            userAgent.setOsGroupKey(osGroup.name());
            userAgent.setOsGroupName(osGroup.getName());
        }
    }

    private void setOs2Device(OperatingSystem os, UserAgent userAgent) {
        if (os != null) {
            userAgent.setOsKey(os.name());
            userAgent.setOsName(os.getName());
        }
    }

    private void setOsManufacturer2Device(Manufacturer osManufacturer, UserAgent userAgent) {
        if (osManufacturer != null) {
            userAgent.setOsManufacturerKey(osManufacturer.name());
            userAgent.setOsManufacturerName(osManufacturer.getName());
        }
    }

    private void setBrowserGroup2Device(Browser browserGroup, UserAgent userAgent) {
        if (browserGroup != null) {
            userAgent.setBrowserGroupKey(browserGroup.name());
            userAgent.setBrowserGroupName(browserGroup.getName());
        }
    }

    private void setBrowser2Device(Browser browser, UserAgent userAgent) {
        if (browser != null) {
            userAgent.setBrowserKey(browser.name());
            userAgent.setBrowserName(browser.getName());

            if (browser.getRenderingEngine() != null) {
                userAgent.setBrowserRenderingEngine(browser.getRenderingEngine().name());
            }
        }
    }

    private void setBrowserType2Device(BrowserType browserType, UserAgent userAgent) {
        if (browserType != null) {
            userAgent.setBrowserTypeKey(browserType.name());
            userAgent.setBrowserTypeName(browserType.getName());
        }
    }

    private void setBrowserManufacturer2Device(Manufacturer browserManufacturer, UserAgent userAgent) {
        if (browserManufacturer != null) {
            userAgent.setBrowserManufacturerKey(browserManufacturer.name());
            userAgent.setBrowserManufacturerName(browserManufacturer.getName());
        }
    }

    private void setAgentVersion2Device(Version agentVersion, UserAgent userAgent) {
        if (agentVersion != null) {
            userAgent.setAgentVersion(agentVersion.getVersion());
            userAgent.setAgentMajorVersion(agentVersion.getMajorVersion());
            userAgent.setAgentMinorVersion(agentVersion.getMinorVersion());
        }
    }

}
