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
import android.util.AttributeSet;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

import com.cyanogenmod.settings.device.DisplaySettings;
import com.cyanogenmod.settings.device.Utils;

import com.cyanogenmod.settings.device.R;

public class mDNIeScenario extends mDNIeBasePreference {

    @Override
    public int getFileStringResId() {
        return R.string.mdnie_scenario_sysfs_file;
    }

    public mDNIeScenario(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static void restore(Context context) {
        mDNIeBasePreference.restore(context, DisplaySettings.KEY_MDNIE_SCENARIO, R.string.mdnie_scenario_sysfs_file);
    }

}
