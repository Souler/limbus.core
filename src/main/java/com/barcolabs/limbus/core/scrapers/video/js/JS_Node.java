package com.barcolabs.limbus.core.scrapers.video.js;

import org.mozilla.javascript.NativeObject;

public class JS_Node extends NativeObject {

    private static JS_Node root = new JS_Node("html");

    public JS_Node(String tag) {
        this.defineProperty("style", new NativeObject(), PERMANENT);
        this.defineProperty("parentNode", root, PERMANENT);
        this.defineProperty("tagName", tag, PERMANENT);
    }

    public JS_Node jsFunction_insertBefore(JS_Node newNode, JS_Node existingNode) {
        return this;
    }

    @Override
    public String getClassName() {
        return "Node";
    }
}
