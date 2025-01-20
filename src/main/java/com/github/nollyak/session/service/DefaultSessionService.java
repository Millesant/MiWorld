package com.github.nollyak.session.service;

import com.github.nollyak.session.Session;

import java.util.Map;

import java.util.Optional;

import java.util.concurrent.ConcurrentHashMap;

public record DefaultSessionService(
    Map<String, Session> sessionMap
) implements SessionService {

    public DefaultSessionService() {
        this(new ConcurrentHashMap<>());
    }

    @Override
    public Optional<Session> getSession(
        final String name
    ) {
        return Optional.ofNullable(this.sessionMap()
            .get(name));
    }

    @Override
    public void addSession(
        final Session session
    ) {
        this.sessionMap()
            .put(session.name(), session);
    }

    @Override
    public void delSession(
        final String name
    ) {
        this.sessionMap()
            .keySet()
            .removeIf(sessionName -> sessionName.equals(name));
    }

    @Override
    public boolean hasSession(
        final String name
    ) {
        return this.sessionMap()
            .containsKey(name);
    }

}
