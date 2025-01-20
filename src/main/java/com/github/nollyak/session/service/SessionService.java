package com.github.nollyak.session.service;

import com.github.nollyak.session.Session;

import java.util.Map;
import java.util.Optional;

public interface SessionService {

    Map<String, Session> sessionMap();

    Optional<Session> getSession(
        final String name
    );

    void addSession(
        final Session session
    );

    void delSession(
        final String name
    );

    boolean hasSession(
        final String name
    );

}
