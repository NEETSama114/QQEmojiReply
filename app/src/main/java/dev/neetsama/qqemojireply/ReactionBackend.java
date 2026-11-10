package dev.neetsama.qqemojireply;

/**
 * Boundary for the version-specific host adapter.
 * A real implementation must be written only after confirming QQ 9.2.85 signatures.
 */
public interface ReactionBackend {
    boolean addReaction(String conversationId, String messageId, String emojiId) throws Exception;
}
