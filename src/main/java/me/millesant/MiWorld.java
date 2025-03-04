package me.millesant;

import me.millesant.editor.command.EditorCommand;
import me.millesant.editor.listener.EditorListener;
import me.millesant.editor.service.WorldServiceImpl;
import me.millesant.editor.service.WorldService;
import me.millesant.plugin.MiWorldPlugin;
import me.millesant.session.service.SessionServiceImpl;
import me.millesant.session.service.SessionService;
import lombok.Getter;

@Getter
public final class MiWorld
    extends MiWorldPlugin {

    private SessionService sessionService;

    private WorldService worldService;

    @Override
    public void onPluginLoad() {
        this.sessionService = new SessionServiceImpl();
        this.worldService = new WorldServiceImpl(this);
    }

    @Override
    public void onPluginStart() {
        this.getServer().getPluginManager().registerEvents(new EditorListener(this), this);
        this.getServer().getCommandMap().register("editor", new EditorCommand(this));
    }

    @Override
    public void onPluginStop() {
    }

}
