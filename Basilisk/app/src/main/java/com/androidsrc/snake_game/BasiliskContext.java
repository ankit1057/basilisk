package com.androidsrc.snake_game;

import android.app.Application;
import android.content.Context;

public class BasiliskContext extends Application {

    private Context currentContext;

    public Context getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(Context context) {
        this.currentContext = context;
    }
}
