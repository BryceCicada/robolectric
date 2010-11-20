package com.xtremelabs.robolectric.shadows;

import android.content.ContentResolver;
import android.provider.Settings;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(Settings.class)
public class ShadowSettings {
    @Implements(Settings.class)
    private static class SettingsImpl {
        private static final WeakHashMap<ContentResolver, Map<String, Integer>> dataMap = new WeakHashMap<ContentResolver, Map<String, Integer>>();

        @Implementation
        public static boolean putInt(ContentResolver cr, String name, int value) {
            get(cr).put(name, value);
            return true;
        }

        @Implementation
        public static int getInt(ContentResolver cr, String name, int def) {
            Integer value = get(cr).get(name);
            return value == null ? def : value;
        }

        @Implementation
        private static Map<String, Integer> get(ContentResolver cr) {
            Map<String, Integer> map = dataMap.get(cr);
            if (map == null) {
                map = new HashMap<String, Integer>();
                dataMap.put(cr, map);
            }
            return map;
        }
    }

    @Implements(Settings.System.class)
    public static class ShadowSystem extends SettingsImpl {
    }

    @Implements(Settings.Secure.class)
    public static class ShadowSecure extends SettingsImpl {
    }

    public static void setAirplaneMode(boolean isAirplaneMode) {
        Settings.System.putInt(Robolectric.application.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, isAirplaneMode ? 1 : 0);
    }

    public static void setWifiOn(boolean isOn) {
        Settings.Secure.putInt(Robolectric.application.getContentResolver(), Settings.Secure.WIFI_ON, isOn ? 1 : 0);
    }

    public static void set24HourTimeFormat(boolean use24HourTimeFormat) {
        Settings.System.putInt(Robolectric.application.getContentResolver(), Settings.System.TIME_12_24, use24HourTimeFormat ? 24 : 12);
    }
}