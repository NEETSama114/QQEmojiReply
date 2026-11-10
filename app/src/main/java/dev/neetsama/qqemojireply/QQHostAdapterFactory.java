package dev.neetsama.qqemojireply;

/**
 * Central adapter lookup. A QQ 9.2.85 implementation is added here only after
 * its event and add-reaction signatures are confirmed from a reference module.
 */
public final class QQHostAdapterFactory {
    private QQHostAdapterFactory() {}

    public static QQHostAdapter create(ClassLoader hostClassLoader) {
        // No fabricated class names or guessed method signatures.
        // The engine and adapter contract are ready for the verified NTQQ bridge.
        return null;
    }
}
