/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.resolver;

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

        eu.bitwalker.useragentutils.UserAgent agent = eu.bitwalker.useragentutils.UserAgent.parseUserAgentString(header);
        UserAgent userAgent = UserAgent.builder().build();
        
        extractBrowser(agent, userAgent);
        extractOperatingSystem(agent, userAgent);
        extractVerion(agent, userAgent);

        return userAgent;
    }

    private <T> Optional<T> $(T value) {
        return Optional.ofNullable(value);
    }

    private void extractBrowser(eu.bitwalker.useragentutils.UserAgent agent, UserAgent userAgent) {
        $(agent.getBrowser()).ifPresent(browser -> {
            userAgent.setBrowserKey(browser.name());
            userAgent.setBrowserName(browser.getName());

            $(browser.getRenderingEngine()).ifPresent(renderingEngine -> {
                userAgent.setBrowserRenderingEngine(renderingEngine.name());
            });

            $(browser.getManufacturer()).ifPresent(manufacturer -> {
                userAgent.setBrowserManufacturerKey(manufacturer.name());
                userAgent.setBrowserManufacturerName(manufacturer.getName());
            });

            $(browser.getGroup()).ifPresent(group -> {
                userAgent.setBrowserGroupKey(group.name());
                userAgent.setBrowserGroupName(group.getName());
            });

            $(browser.getBrowserType()).ifPresent(type -> {
                userAgent.setBrowserTypeKey(type.name());
                userAgent.setBrowserTypeName(type.getName());
            });
        });
    }

    private void extractOperatingSystem(eu.bitwalker.useragentutils.UserAgent agent, UserAgent userAgent) {
        $(agent.getOperatingSystem()).ifPresent(os -> {
            userAgent.setOsKey(os.name());
            userAgent.setOsName(os.getName());

            $(os.getDeviceType()).ifPresent(type -> {
                userAgent.setDeviceTypeKey(type.name());
                userAgent.setDeviceTypeName(type.getName());
            });

            $(os.getManufacturer()).ifPresent(manufacturer -> {
                userAgent.setOsManufacturerKey(manufacturer.name());
                userAgent.setOsManufacturerName(manufacturer.getName());
            });

            $(os.getGroup()).ifPresent(group -> {
                userAgent.setOsGroupKey(group.name());
                userAgent.setOsGroupName(group.getName());
            });
        });
    }

    private void extractVerion(eu.bitwalker.useragentutils.UserAgent agent, UserAgent userAgent) {
        $(agent.getBrowserVersion()).ifPresent(version -> {
            userAgent.setAgentVersion(version.getVersion());
            userAgent.setAgentMajorVersion(version.getMajorVersion());
            userAgent.setAgentMinorVersion(version.getMinorVersion());
        });
    }

}
