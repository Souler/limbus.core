package com.barcolabs.limbus.core.scrapers.js;

import org.mozilla.javascript.ScriptableObject;

public class JS_Location extends ScriptableObject {

    public void jsConstructor() {
        this.defineProperty("location", new JS_Location(), PERMANENT);
    }

    @Override
    public String getClassName() {
        return "Window";
    }
}
