/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.commons.provider;

import com.pamarin.commons.exception.ServerInformationProviderException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Slf4j
@Component
public class DefaultServerInfomationProvider implements ServerInfomationProvider {

    @Override
    public String provideIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new ServerInformationProviderException("get server id error", e);
        }
    }

}
