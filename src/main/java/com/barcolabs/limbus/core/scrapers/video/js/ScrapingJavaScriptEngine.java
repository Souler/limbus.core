package com.barcolabs.limbus.core.scrapers.video.js;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.InvocationTargetException;

public class ScrapingJavaScriptEngine {

    // JS Engine
    private Context context;
    private Scriptable scope;

    // References to host objetcs
    private JWPlayer jwPlayer;

    public ScrapingJavaScriptEngine() {
        this.context = Context.enter();
        this.scope = this.context.initStandardObjects();

        this.context.setOptimizationLevel(-1);

        // Define the classes to be used inside the scripts
        try {
            ScriptableObject.defineClass(scope, JS_Document.class);
            ScriptableObject.defineClass(scope, JS_Location.class);
            ScriptableObject.defineClass(scope, JS_Window.class);
            ScriptableObject.defineClass(scope, SWFObject.class);
            ScriptableObject.defineClass(scope, JWPlayer.class);
            ScriptableObject.defineClass(scope, JQuery.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Add the standard global variables to the scope
        // Mock document
        Scriptable document = this.context.newObject(scope, "Document");
        scope.put("document", scope, document);
        // Mock window
        Scriptable window = this.context.newObject(scope, "Window");
        scope.put("window", scope, window);
        // Mock jQuery
        Scriptable jquery = this.context.newObject(scope, "JQuery");
        scope.put("jQuery", scope, jquery);
        Scriptable swfobject = this.context.newObject(scope, "SWFObject");
        scope.put("swfobject", scope, swfobject);
        scope.put("primaryCookie", scope, "html5");
        // jwplayer is expected to be a function what returns a JWPlayer instance when called
        final Scriptable warpedJWPlayer = this.context.newObject(scope, "JWPlayer");
        scope.put("jwplayer", scope, new BaseFunction() {
            public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return warpedJWPlayer;
            }
        });
        scope.put("eval", scope, new BaseFunction() {
            public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return ScrapingJavaScriptEngine.this.eval((String) args[0]);
            }
        });
        // Wire the JS objects to the Java ones
        this.jwPlayer = (JWPlayer) Context.jsToJava(warpedJWPlayer, JWPlayer.class);
    }

    public Object eval(String code) {
        return context.evaluateString(scope, code, "SJSE", 1, null);
}

    public JWPlayer getJwPlayer() {
        return jwPlayer;
    }
}
