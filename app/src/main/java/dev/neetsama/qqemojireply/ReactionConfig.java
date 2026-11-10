package dev.neetsama.qqemojireply;
import de.robv.android.xposed.XSharedPreferences;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
public final class ReactionConfig {
    public final boolean enabled;
    public final Set<String> targetUins, targetGroups;
    public final String[] emojiIds;
    public final long delayMillis;
    private ReactionConfig(boolean e, Set<String> u, Set<String> g, String[] ids, long d) {
        enabled=e; targetUins=Collections.unmodifiableSet(u);
        targetGroups=Collections.unmodifiableSet(g); emojiIds=ids;
        delayMillis=Math.max(0L,Math.min(d,10000L));
    }
    public static ReactionConfig from(XSharedPreferences p) {
        return new ReactionConfig(p.getBoolean("enabled",false),
            parse(p.getString("target_uins","")),
            parse(p.getString("target_groups","")),
            parse(p.getString("emoji_ids","")).toArray(new String[0]),
            p.getLong("delay_ms",300L));
    }
    private static Set<String> parse(String s) {
        Set<String> r=new LinkedHashSet<>();
        if(s!=null) for(String x:s.split("[,，\\n\\r]+")) if(!x.trim().isEmpty()) r.add(x.trim());
        return r;
    }
    public boolean matches(String u,String g) {
        return enabled && emojiIds.length>0
            && (targetUins.isEmpty() || targetUins.contains(u))
            && (targetGroups.isEmpty() || targetGroups.contains(g));
    }
}
