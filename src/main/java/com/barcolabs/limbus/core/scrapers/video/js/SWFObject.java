package com.barcolabs.limbus.core.scrapers.video.js;

import org.mozilla.javascript.ScriptableObject;

/**
 * Created by barbosa on 29/09/15.
 */

public class SWFObject extends ScriptableObject {
    @Override
    public String getClassName() {
        return "SWFObject";
    }

    public void jsFunction_createCSS() {}
    public void jsFunction_embedSWF() {}
}
