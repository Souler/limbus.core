package com.barcolabs.limbus.core.scrapers.video.js;

import org.mozilla.javascript.ScriptableObject;

public class JS_Window  extends ScriptableObject {

    public void jsConstructor() {
        this.defineProperty("href", "", PERMANENT);
    }

    @Override
    public String getClassName() {
        return "Location";
    }
}
