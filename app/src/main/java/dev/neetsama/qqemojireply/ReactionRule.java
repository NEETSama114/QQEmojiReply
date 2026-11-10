package dev.neetsama.qqemojireply;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Version-independent rule model; the QQ hook adapter should call matches(). */
public final class ReactionRule {
    public final boolean enabled;
    public final Set<String> targetUins;
    public final Set<String> targetGroupIds;
    public final String[] emojiIds;
    public final long delayMillis;

    public ReactionRule(boolean enabled, Set<String> targetUins,
                        Set<String> targetGroupIds, String[] emojiIds,
                        long delayMillis) {
        this.enabled = enabled;
        this.targetUins = targetUins == null ? Collections.emptySet()
                : Collections.unmodifiableSet(new HashSet<>(targetUins));
        this.targetGroupIds = targetGroupIds == null ? Collections.emptySet()
                : Collections.unmodifiableSet(new HashSet<>(targetGroupIds));
        this.emojiIds = emojiIds == null ? new String[0] : Arrays.copyOf(emojiIds, emojiIds.length);
        this.delayMillis = Math.max(0L, Math.min(delayMillis, 10_000L));
    }

    public boolean matches(String senderUin, String groupId) {
        if (!enabled) return false;
        boolean userMatch = targetUins.isEmpty() || targetUins.contains(senderUin);
        boolean groupMatch = targetGroupIds.isEmpty() || targetGroupIds.contains(groupId);
        return userMatch && groupMatch && emojiIds.length > 0;
    }
}
