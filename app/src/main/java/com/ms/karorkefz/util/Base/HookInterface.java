package com.ms.karorkefz.util.Base;

import android.content.Context;

public interface HookInterface {
    void init();

    HookInterface setClassLoader(ClassLoader classLoader);

    HookInterface setContext(Context context);
}
