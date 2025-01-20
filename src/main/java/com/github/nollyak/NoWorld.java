package com.github.nollyak;

import com.github.nollyak.editor.command.DefaultEditorCommand;

import com.github.nollyak.editor.handler.EditorHandler;

import com.github.nollyak.editor.service.DefaultWorldService;
import com.github.nollyak.editor.service.WorldService;

import com.github.nollyak.plugin.NoWorldPlugin;

import com.github.nollyak.session.service.DefaultSessionService;
import com.github.nollyak.session.service.SessionService;

import lombok.Getter;

@Getter
public final class NoWorld extends NoWorldPlugin {

    private SessionService sessionService;

    private WorldService worldService;

    @Override
    public void onPluginLoad() {
        this.sessionService = new DefaultSessionService();
        this.worldService = new DefaultWorldService(this);
    }

    @Override
    public void onPluginStart() {
        this.getServer().getPluginManager().registerEvents(new EditorHandler(this), this);
        this.getServer().getCommandMap().register("editor", new DefaultEditorCommand(this));
    }

    @Override
    public void onPluginStop() {
    }

}
