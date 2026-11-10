package dev.neetsama.qqemojireply;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Xposed entry point. Intentionally fail-closed until the QQ 9.2.85
 * incoming-message event and reaction API have been verified.
 */
public final class QQEmojiReplyEntry implements IXposedHookLoadPackage {
    private static final String QQ_PACKAGE = "com.tencent.mobileqq";
    private static final String MODULE_PACKAGE = "dev.neetsama.qqemojireply";

    @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!QQ_PACKAGE.equals(lpparam.packageName)) return;

        XposedBridge.log("QQEmojiReply: loaded process=" + lpparam.processName
                + ", host version must be verified as QQ 9.2.85");

        try {
            XSharedPreferences prefs = new XSharedPreferences(MODULE_PACKAGE, "qq_emoji_reply");
            prefs.makeWorldReadable();
            prefs.reload();
            boolean enabled = prefs.getBoolean("enabled", false);
            XposedBridge.log("QQEmojiReply: config read, enabled=" + enabled);

            if (!enabled) return;

            // TODO QQ 9.2.85 only:
            // 1. Resolve the incoming message event using verified host classes.
            // 2. Extract conversation/group ID, sender UIN, and stable message ID.
            // 3. Match targetUins/targetGroups.
            // 4. Call the verified native reaction method with the QQ emoji ID.
            //
            // Do not use guessed class/method names here. Wrong internal hooks
            // can crash QQ or silently send the wrong operation.
            XposedBridge.log("QQEmojiReply: rules are enabled, but reaction adapter "
                    + "is not connected yet; no messages will be modified.");
        } catch (Throwable t) {
            XposedBridge.log("QQEmojiReply config error: " + t);
        }
    }
}
