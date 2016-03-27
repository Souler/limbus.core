package com.barcolabs.limbus.core.scrapers.video.js;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by barbosa on 29/09/15.
 */
public class JS_Document extends ScriptableObject {
    public NativeObject jsFunction_getElementById(String id) {
        NativeObject o = new NativeObject();
        o.defineProperty("style", new NativeObject(), PERMANENT);
        return o;
    }

    @Override
    public String getClassName() {
        return "Document";
    }
}
