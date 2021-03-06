package com.barcolabs.limbus.core.scrapers.js;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class JWPlayer extends ScriptableObject {

    public static final String NAME = "jwplayer";

    private List<String> files;

    public JWPlayer() {
        this.files = new ArrayList<>();
    }

    public List<String> getFiles() {
        return this.files;
    }

    public String getFile() {
        int len = this.files.size();

        if (len > 1) {
            for (String file : this.files) {
                Pattern p = Pattern.compile("^http[s]*:");
                if (p.matcher(file).find())
                    return file;
            }
        }

        return len > 0 ? this.files.get(0) : null;
    }

    @Override
    public String getClassName() {
        return "JWPlayer";
    }

    public void jsConstructor() {
    }

    public JWPlayer jsFunction_onBufferChange(Object o) {
        return this;
    }

    public JWPlayer jsFunction_onReady(Object o) {
        return this;
    }

    public JWPlayer jsFunction_onError(Object o) {
        return this;
    }

    public JWPlayer jsFunction_onPlaylist(Object o) {
        return this;
    }

    public JWPlayer jsFunction_onBeforePlay(Object o) {
        return this;
    }

    public JWPlayer jsFunction_onPlay(Object o) {
        return this;
    }

    public JWPlayer jsFunction_onTime(Object o) {
        return this;
    }

    public JWPlayer jsFunction_onSeek(Object o) {
        return this;
    }

    public JWPlayer jsFunction_onComplete(Object o) {
        return this;
    }

    public JWPlayer jsFunction_addButton(Object o) {
        return this;
    }


    private ArrayList<String> map(NativeArray arr, String field) {
        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < arr.getLength(); i++) {
            NativeObject elem = (NativeObject) arr.get(i, arr);
            Object elemRaw = elem.get(field, elem);

            if (elemRaw != NOT_FOUND && elemRaw instanceof String)
                result.add((String) elemRaw);
        }

        return result;
    }

    public JWPlayer jsFunction_setup(NativeObject config) {
        Object fileRaw = NativeObject.getProperty(config, "file");
        Object sourcesRaw = NativeObject.getProperty(config, "sources");
        Object playlistRaw = NativeObject.getProperty(config, "playlist");

        if (fileRaw != NOT_FOUND)
            files.add((String) fileRaw);

        if (sourcesRaw != NOT_FOUND)
            files.addAll(map((NativeArray) sourcesRaw, "file"));

        if (playlistRaw != NOT_FOUND) {
            NativeArray playlist = (NativeArray) playlistRaw;

            for (int i = 0; i < playlist.getLength(); i++) {
                NativeObject playlistElem = (NativeObject) playlist.get(i, playlist);
                Object playlistSources = playlistElem.get("sources", playlistElem);
                if (playlistSources != NOT_FOUND)
                    files.addAll(map((NativeArray) playlistSources, "file"));

            }
        }

        return this;
    }
}
