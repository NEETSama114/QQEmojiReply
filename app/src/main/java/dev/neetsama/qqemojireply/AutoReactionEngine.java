package dev.neetsama.qqemojireply;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Host-independent event/rule pipeline. QQ-specific code only supplies normalized
 * IncomingMessage events and the ReactionBackend implementation.
 */
public final class AutoReactionEngine {
    private final ReactionConfig config;
    private final ReactionBackend backend;
    private final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private final Set<String> handled = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());

    public AutoReactionEngine(ReactionConfig config, ReactionBackend backend) {
        this.config = config;
        this.backend = backend;
    }

    public void onIncomingMessage(IncomingMessage message) {
        if (message == null || !message.isUsable()) return;
        if (!config.matches(message.senderUin,
                message.groupMessage ? message.groupId : "")) return;

        final String dedupeKey = message.conversationId + ":" + message.messageId;
        if (!handled.add(dedupeKey)) return;

        // Bound memory use; dedupe is process-local and intentionally conservative.
        if (handled.size() > 2048) handled.clear();
        for (int i = 0; i < config.emojiIds.length; i++) {
            final String emojiId = config.emojiIds[i];
            final long delay = config.delayMillis * i;
            worker.schedule(() -> {
                try {
                    boolean ok = backend.addReaction(
                            message.conversationId, message.messageId, emojiId);
                    android.util.Log.i("QQEmojiReply",
                            "reaction result=" + ok + ", emojiId=" + emojiId);
                } catch (Throwable t) {
                    android.util.Log.e("QQEmojiReply",
                            "reaction failed, emojiId=" + emojiId, t);
                }
            }, delay, TimeUnit.MILLISECONDS);
        }
    }

    public void shutdown() {
        worker.shutdownNow();
        handled.clear();
    }
}
