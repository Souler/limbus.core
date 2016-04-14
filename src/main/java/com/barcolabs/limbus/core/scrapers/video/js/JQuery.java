package com.barcolabs.limbus.core.scrapers.video.js;

import org.mozilla.javascript.*;

public class JQuery extends ScriptableObject {
    public NativeArray jsFunction_map(NativeArray arr, BaseFunction callback) {
        for (int i = 0; i < arr.getLength(); i++) {
            NativeObject elem = (NativeObject) arr.get(i, arr);
            Object[] a = {elem, i};
            callback.call(Context.getCurrentContext(), this, elem, a);
        }
        return arr;
    }

    public void jsConstructor() {
    }

    @Override
    public String getClassName() {
        return "JQuery";
    }
}
