package dev.neetsama.qqemojireply;

/**
 * Version-specific bridge. Implementations own all QQ-internal reflection/Hook details.
 * Keep host objects out of the rule engine.
 */
public interface QQHostAdapter {
    interface MessageListener {
        void onIncomingGroupMessage(IncomingMessage message);
    }

    /** Install the verified incoming-message event hook. */
    boolean installMessageListener(ClassLoader hostClassLoader,
                                   MessageListener listener) throws Throwable;

    /** Invoke QQ's verified add-reaction operation for the original message. */
    boolean addReaction(String conversationId, String messageId,
                        String emojiId) throws Exception;
}
