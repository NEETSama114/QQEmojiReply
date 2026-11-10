# QQEmojiReply-Xposed

一个面向 Android NTQQ + LSPosed 的自动表情回应模块起始工程。

## 目标功能

- 监听收到的群聊消息
- 按 QQ 号、群号筛选
- 对原消息添加一个或多个表情回应
- 按配置顺序回应，并支持延迟
- 全局开关与调试日志

## 工程

- `app/src/main/java/.../QQEmojiReplyEntry.java`：Xposed 入口
- `ReactionRule.java`：与 QQ 版本无关的规则模型
- `.github/workflows/build.yml`：推送代码后自动构建 debug APK

## 安全与兼容性

- 内部接口可能随 QQ 更新变化；接口未匹配时应停止执行并记录日志，而不是崩溃。
- 自动化操作可能触及 QQ 服务条款，请自行评估风险。
