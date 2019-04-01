/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.client.sdk;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import static org.springframework.util.StringUtils.hasText;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jitta
 */
@Controller
public class CallbackCtrl {

    @GetMapping("/callback")
    public void callback(@RequestParam(name = "code", required = false) String code, HttpServletResponse httpResp) throws IOException {
        if (hasText(code)) {
            httpResp.sendRedirect("/");
        }
    }

}
