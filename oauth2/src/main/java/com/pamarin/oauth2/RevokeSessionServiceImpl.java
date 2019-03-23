/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2;

import com.pamarin.oauth2.repository.UserSessionRepo;
import com.pamarin.oauth2.service.RevokeSessionService;
import com.pamarin.oauth2.service.RevokeSessionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

/**
 *
 * @author jitta
 */
@Service
@Transactional
public class RevokeSessionServiceImpl implements RevokeSessionService {

    @Autowired
    private UserSessionRepo userSessionRepo;

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public void revokeBySessionId(String sessionId) {
        if (hasText(sessionId)) {
            sessionRepository.delete(sessionId);
            userSessionRepo.delete(sessionId);
        }
    }

    @Override
    public void revokeBySessionIds(List<String> sessionIds) {
        if (!isEmpty(sessionIds)) {
            sessionIds.forEach(sessionId -> revokeBySessionId(sessionId));
        }
    }

    @Override
    public void revokeAllOnSameUserAgentBySessionId(String sessionId) {
        if (hasText(sessionId)) {
            revokeBySessionIds(userSessionRepo.findAllIdsOnSameUserAgentById(sessionId));
        }
    }

    @Override
    public void revokeByUserId(String userId) {
        if (hasText(userId)) {
            revokeBySessionIds(userSessionRepo.findAllIdsByUserId(userId));
        }
    }

    @Override
    public void revokeOthersOnSameUserAgentBySessionId(String sessionId) {
        if (hasText(sessionId)) {
            revokeBySessionIds(userSessionRepo.findOtherIdsOnSameUserAgentById(sessionId));
        }
    }

}
