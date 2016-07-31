package com.barcolabs.limbus.core.scrapers.video.js;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import sun.org.mozilla.javascript.internal.NativeArray;

public class JS_Document extends ScriptableObject {

    public NativeObject jsFunction_createElement(String tag) {
        return new JS_Node(tag);
    }

    public NativeObject jsFunction_getElementById(String id) {
        return new JS_Node("a");
    }

    public NativeObject[] jsFunction_getElementsByTagName(String tag) {
        return new NativeObject[] {
                new JS_Node(tag)
        };
    }

    public void jsConstructor() {
        this.defineProperty("location", new JS_Location(), PERMANENT);
    }

    @Override
    public String getClassName() {
        return "Document";
    }
}
