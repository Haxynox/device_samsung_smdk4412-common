/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.cyanogenmod.settings.device.Utils;

public abstract class mDNIeBasePreference extends ListPreference implements OnPreferenceChangeListener {
    private String mFile;

    public abstract int getFileStringResId();

    public mDNIeBasePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFile = context.getResources().getString(getFileStringResId());

        this.setOnPreferenceChangeListener(this);
    }

    public static boolean isSupported(String filePath) {
        return Utils.fileExists(filePath);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Utils.writeValue(mFile, (String) newValue);
        return true;
    }

    /**
     * Restore mdnie user mode setting from SharedPreferences. (Write to kernel.)
     *
     * @param context         The context to read the SharedPreferences from
     * @param key             The key of the shared preference
     * @param fileStringResId The resource id of the string containing the sysfs path
     */
    /* package */ static void restore(Context context, String key, int fileStringResId) {
        final String file = context.getResources().getString(fileStringResId);
        if (!isSupported(file)) {
            return;
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Utils.writeValue(file, sharedPrefs.getString(key, "0"));
    }

}
