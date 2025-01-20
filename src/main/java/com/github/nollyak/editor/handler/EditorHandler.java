package com.github.nollyak.editor.handler;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;

import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import com.github.nollyak.NoWorld;

import com.github.nollyak.session.DefaultSession;

public record EditorHandler(
    NoWorld plugin
) implements Listener {

    @EventHandler
    public void onPlayerJoin(
        final PlayerJoinEvent event
    ) {
        if (!(
            this.plugin()
                .getSessionService()
                .hasSession(event.getPlayer().getName())
        )) {
            this.plugin()
                .getSessionService()
                .addSession(new DefaultSession(event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(
        final PlayerQuitEvent event
    ) {
        if (
            this.plugin()
                .getSessionService()
                .hasSession(event.getPlayer().getName())
        ) {
            this.plugin()
                .getSessionService()
                .delSession(event.getPlayer().getName());
        }
    }

}
