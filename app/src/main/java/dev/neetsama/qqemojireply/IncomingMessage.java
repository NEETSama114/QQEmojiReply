package dev.neetsama.qqemojireply;

/** Normalized incoming message; host-specific QQ objects must be converted by the adapter. */
public final class IncomingMessage {
    public final String conversationId;
    public final String groupId;
    public final String senderUin;
    public final String messageId;
    public final boolean groupMessage;
    public final boolean fromSelf;

    public IncomingMessage(String conversationId, String groupId, String senderUin,
                           String messageId, boolean groupMessage, boolean fromSelf) {
        this.conversationId = safe(conversationId);
        this.groupId = safe(groupId);
        this.senderUin = safe(senderUin);
        this.messageId = safe(messageId);
        this.groupMessage = groupMessage;
        this.fromSelf = fromSelf;
    }

    private static String safe(String value) {
        return value == null ? "" : value.trim();
    }

    public boolean isUsable() {
        return !conversationId.isEmpty() && !messageId.isEmpty()
                && !senderUin.isEmpty() && groupMessage && !fromSelf;
    }
}
