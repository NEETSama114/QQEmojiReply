package dev.neetsama.qqemojireply;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Xposed-compatible entry point. This starter intentionally does not hook
 * undocumented NTQQ internals until the exact QQ version is known.
 */
public final class QQEmojiReplyEntry implements IXposedHookLoadPackage {
    private static final String QQ_PACKAGE = "com.tencent.mobileqq";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!QQ_PACKAGE.equals(lpparam.packageName)) return;

        XposedBridge.log("QQEmojiReply: loaded in " + lpparam.packageName
                + " process=" + lpparam.processName);

        // TODO(version-specific): register a hook for incoming NTQQ messages.
        // TODO(version-specific): extract conversation id, sender UIN, and msg id.
        // TODO(version-specific): invoke QQ's native message-reaction operation.
        // Never guess class/method names: NTQQ internals vary by version.
    }
}
