package me.millesant.editor.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import me.millesant.MiWorld;
import me.millesant.session.SessionImpl;

public record EditorListener(MiWorld plugin) implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (!(this.plugin().getSessionService().hasSession(event.getPlayer().getName()))) {
            this.plugin().getSessionService().addSession(new SessionImpl(event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (this.plugin().getSessionService().hasSession(event.getPlayer().getName())) {
            this.plugin().getSessionService().delSession(event.getPlayer().getName());
        }
    }

}
