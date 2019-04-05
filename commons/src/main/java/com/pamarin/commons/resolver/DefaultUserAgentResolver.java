/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.Manufacturer;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.RenderingEngine;
import static eu.bitwalker.useragentutils.UserAgent.parseUserAgentString;
import eu.bitwalker.useragentutils.Version;
import java.util.Optional;
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

        UserAgent userAgent = UserAgent.builder().build();
        $(parseUserAgentString(header)).ifPresent(agent -> {
            copyBrowser(agent.getBrowser(), userAgent);
            copyOperatingSystem(agent.getOperatingSystem(), userAgent);
            copyVerion(agent.getBrowserVersion(), userAgent);
        });
        return userAgent;
    }

    private <T> Optional<T> $(T value) {
        return Optional.ofNullable(value);
    }

    private void copyBrowser(Browser browser, UserAgent userAgent) {
        $(browser).ifPresent(bws -> {
            userAgent.setBrowserKey(bws.name());
            userAgent.setBrowserName(bws.getName());
            copyRenderingEngine(bws.getRenderingEngine(), userAgent);
            copyBrowerManufacturer(bws.getManufacturer(), userAgent);
            copyBrowserGroup(bws.getGroup(), userAgent);
            copyBrowserType(bws.getBrowserType(), userAgent);
        });
    }

    private void copyRenderingEngine(RenderingEngine renderingEngine, UserAgent userAgent) {
        $(renderingEngine).ifPresent(engine -> {
            userAgent.setBrowserRenderingEngine(engine.name());
        });
    }

    private void copyBrowerManufacturer(Manufacturer manufacturer, UserAgent userAgent) {
        $(manufacturer).ifPresent(man -> {
            userAgent.setBrowserManufacturerKey(man.name());
            userAgent.setBrowserManufacturerName(man.getName());
        });
    }

    private void copyBrowserGroup(Browser browser, UserAgent userAgent) {
        $(browser).ifPresent(group -> {
            userAgent.setBrowserGroupKey(group.name());
            userAgent.setBrowserGroupName(group.getName());
        });
    }

    private void copyBrowserType(BrowserType browserType, UserAgent userAgent) {
        $(browserType).ifPresent(type -> {
            userAgent.setBrowserTypeKey(type.name());
            userAgent.setBrowserTypeName(type.getName());
        });
    }

    private void copyOperatingSystem(OperatingSystem operatingSystem, UserAgent userAgent) {
        $(operatingSystem).ifPresent(os -> {
            userAgent.setOsKey(os.name());
            userAgent.setOsName(os.getName());
            copyDeviceType(os.getDeviceType(), userAgent);
            copyOsManufacturer(os.getManufacturer(), userAgent);
            copyOsGroup(os.getGroup(), userAgent);
        });
    }

    private void copyDeviceType(DeviceType deviceType, UserAgent userAgent) {
        $(deviceType).ifPresent(type -> {
            userAgent.setDeviceTypeKey(type.name());
            userAgent.setDeviceTypeName(type.getName());
        });
    }

    private void copyOsManufacturer(Manufacturer osManufacturer, UserAgent userAgent) {
        $(osManufacturer).ifPresent(manufacturer -> {
            userAgent.setOsManufacturerKey(manufacturer.name());
            userAgent.setOsManufacturerName(manufacturer.getName());
        });
    }

    private void copyOsGroup(OperatingSystem osGroup, UserAgent userAgent) {
        $(osGroup).ifPresent(group -> {
            userAgent.setOsGroupKey(group.name());
            userAgent.setOsGroupName(group.getName());
        });
    }

    private void copyVerion(Version version, UserAgent userAgent) {
        $(version).ifPresent(vs -> {
            userAgent.setAgentVersion(vs.getVersion());
            userAgent.setAgentMajorVersion(vs.getMajorVersion());
            userAgent.setAgentMinorVersion(vs.getMinorVersion());
        });
    }

}
