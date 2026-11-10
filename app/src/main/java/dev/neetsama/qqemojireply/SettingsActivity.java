package dev.neetsama.qqemojireply;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.io.File;

/** Small no-dependency configuration screen for the starter module. */
public final class SettingsActivity extends Activity {
    private static final String PREFS = "qq_emoji_reply";
    private CheckBox enabled;
    private EditText targetUins, targetGroups, emojiIds, delayMs;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        SharedPreferences p = getSharedPreferences(PREFS, MODE_PRIVATE);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(18);
        root.setPadding(pad, pad, pad, pad);
        root.setBackgroundColor(0xFFF7F7F7);

        TextView title = new TextView(this);
        title.setText("QQ Emoji Reply · 0.2.2");
        title.setTextSize(22);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView warning = new TextView(this);
        warning.setText("QQ 9.2.85 适配开发版：此界面可保存规则，但消息事件与回应接口采用独立适配层，QQ 9.2.85 适配器仍待接入。");
        warning.setTextSize(14);
        warning.setPadding(0, dp(10), 0, dp(10));
        root.addView(warning);

        enabled = new CheckBox(this);
        enabled.setText("启用自动表情回应");
        enabled.setChecked(p.getBoolean("enabled", false));
        root.addView(enabled);

        targetUins = field(root, "目标 QQ 号（逗号分隔；留空表示不限）",
                p.getString("target_uins", ""));
        targetGroups = field(root, "目标群号（逗号分隔；留空表示不限）",
                p.getString("target_groups", ""));
        emojiIds = field(root, "QQ 内部表情 ID（逗号分隔，暂不支持直接填 Emoji 字符）",
                p.getString("emoji_ids", ""));
        delayMs = field(root, "每个表情间隔（毫秒，0–10000）",
                String.valueOf(p.getLong("delay_ms", 300L)));

        Button save = new Button(this);
        save.setText("保存配置");
        save.setOnClickListener(v -> {
            long delay;
            try { delay = Long.parseLong(delayMs.getText().toString().trim()); }
            catch (Exception e) { delay = 300L; }
            delay = Math.max(0L, Math.min(delay, 10000L));
            SharedPreferences.Editor ed = getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                    .putBoolean("enabled", enabled.isChecked())
                    .putString("target_uins", targetUins.getText().toString().trim())
                    .putString("target_groups", targetGroups.getText().toString().trim())
                    .putString("emoji_ids", emojiIds.getText().toString().trim())
                    .putLong("delay_ms", delay);
            ed.apply();
            // Legacy XSharedPreferences reads a module-owned preference file.
            // Best-effort readability is used for this starter; newer Android/ROM
            // restrictions may require a different config transport.
            File prefFile = new File(getApplicationInfo().dataDir, "shared_prefs/" + PREFS + ".xml");
            // apply() is asynchronous, so readability is retried on next launch/hook.
            prefFile.setReadable(true, false);
            Toast.makeText(this, "已保存。请在 LSPosed 中启用模块并重启 QQ。", Toast.LENGTH_LONG).show();
        });
        root.addView(save);
        TextView footer = new TextView(this);
        footer.setText("提示：填入表情 ID 前，需要先完成 QQ 9.2.85 的内部接口验证。当前版本不会自动执行未知接口。");
        footer.setPadding(0, dp(12), 0, 0);
        root.addView(footer);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);
        setContentView(scroll);
    }

    private EditText field(LinearLayout root, String label, String value) {
        TextView t = new TextView(this);
        t.setText(label);
        t.setTextSize(14);
        t.setPadding(0, dp(12), 0, dp(4));
        root.addView(t);
        EditText e = new EditText(this);
        e.setSingleLine(false);
        e.setMinLines(1);
        e.setText(value);
        root.addView(e);
        return e;
    }

    private int dp(int n) {
        return (int) (n * getResources().getDisplayMetrics().density + 0.5f);
    }
}
