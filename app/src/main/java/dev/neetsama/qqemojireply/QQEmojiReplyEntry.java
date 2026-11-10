package dev.neetsama.qqemojireply;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/** Xposed entry point; host-specific operations are isolated behind QQHostAdapter. */
public final class QQEmojiReplyEntry implements IXposedHookLoadPackage {
    private static final String QQ_PACKAGE = "com.tencent.mobileqq";
    private static final String MODULE_PACKAGE = "dev.neetsama.qqemojireply";

    @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!QQ_PACKAGE.equals(lpparam.packageName)) return;

        XposedBridge.log("QQEmojiReply: loaded process=" + lpparam.processName);
        try {
            XSharedPreferences prefs =
                    new XSharedPreferences(MODULE_PACKAGE, "qq_emoji_reply");
            prefs.reload();
            ReactionConfig config = ReactionConfig.from(prefs);

            XposedBridge.log("QQEmojiReply: config enabled=" + config.enabled
                    + ", targetUins=" + config.targetUins.size()
                    + ", targetGroups=" + config.targetGroups.size()
                    + ", emojiIds=" + config.emojiIds.length
                    + ", delayMs=" + config.delayMillis);
            if (!config.enabled) return;

            QQHostAdapter adapter = QQHostAdapterFactory.create(lpparam.classLoader);
            if (adapter == null) {
                XposedBridge.log("QQEmojiReply: no verified QQ 9.2.85 adapter found; "
                        + "event pipeline is ready but hooks are not installed.");
                return;
            }

            ReactionBackend backend = adapter::addReaction;
            AutoReactionEngine engine = new AutoReactionEngine(config, backend);
            boolean installed = adapter.installMessageListener(
                    lpparam.classLoader, engine::onIncomingMessage);
            XposedBridge.log("QQEmojiReply: incoming-message hook installed=" + installed);
            if (!installed) engine.shutdown();
        } catch (Throwable t) {
            XposedBridge.log("QQEmojiReply init failed: " + t);
        }
    }
}
