package com.github.karthyks.gitexplore.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Preconditions {
    private Preconditions() {
        throw new AssertionError("No instances");
    }

    public static void assertNotNull(@Nullable Object obj, @NonNull String message) {
        if (obj == null)
            throw new NullPointerException(message);
    }

    public static void assertNonEmpty(@Nullable String obj, @NonNull String message) {
        if (TextUtils.isEmpty(obj))
            throw new NullPointerException(message);
    }
}
