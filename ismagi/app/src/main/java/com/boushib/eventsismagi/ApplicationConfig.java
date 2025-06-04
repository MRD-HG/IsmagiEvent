package com.boushib.eventsismagi;

import android.app.Application;
import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConfig extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "di36kbm3p");
        config.put("api_key", "177629361138436");
        config.put("api_secret", "ZTB6il43K9lHyWnfok4OUMj8OtE");

        MediaManager.init(this,config);
    }
}
