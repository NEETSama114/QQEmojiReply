# QQEmojiReply-Xposed

一个面向 Android NTQQ + LSPosed 的自动表情回应模块起始工程。

## 当前状态

**这是可扩展的工程骨架，不是已经完成真实表情回应的成品。** 它包含 Android/Xposed 入口、规则模型和 GitHub Actions 构建流程。由于 NTQQ 的消息对象和消息回应方法属于版本相关的内部实现，当前版本不会猜测内部类名，也不会对消息执行虚假的回应。

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

## 构建

在 GitHub 新建仓库后上传这些文件。打开 **Actions**，运行 `Build Android module`，完成后下载 `QQEmojiReply-debug` artifact。

也可以用 Android Studio 打开仓库并构建 `assembleDebug`。

## LSPosed

安装 APK，在 LSPosed 中启用模块，并将作用域设为 `com.tencent.mobileqq`，随后重启 QQ 进程。

## 下一步：实现真实回应

请先确认实际 QQ 版本号（QQ 设置 → 关于 QQ，提供完整版本号，例如 9.x.x）。之后需要针对该版本分析收到消息事件、消息唯一标识和回应 API，并在 `QQEmojiReplyEntry` 中实现版本适配器。不要直接把其他版本的内部类名照搬过来。

## 安全与兼容性

- 建议先用测试账号和测试群验证。
- 内部接口可能随 QQ 更新变化；接口未匹配时应停止执行并记录日志，而不是崩溃。
- 自动化操作可能触及 QQ 服务条款，请自行评估风险。
