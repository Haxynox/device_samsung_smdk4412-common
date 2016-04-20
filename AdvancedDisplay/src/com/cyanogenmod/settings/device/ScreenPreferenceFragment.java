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

package com.cyanogenmod.settings.device;

import android.os.Bundle;
import android.content.res.Resources;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.cyanogenmod.settings.device.preferences.mDNIeBasePreference;
import com.cyanogenmod.settings.device.preferences.mDNIeMode;
import com.cyanogenmod.settings.device.preferences.mDNIeNegative;
import com.cyanogenmod.settings.device.preferences.mDNIeScenario;

import com.cyanogenmod.settings.device.R;

public class ScreenPreferenceFragment extends PreferenceFragment {

    private static final String TAG = "DisplaySettings_Screen";
    private mDNIeScenario mDNIeScenario;
    private mDNIeMode mDNIeMode;
    private mDNIeNegative mDNIeNegative;
    private CheckBoxPreference mTouchKey;
    private TouchkeyTimeout mTouchKeyTimeout;
    private static boolean sTouchkeySupport;

    private static final String FILE_TOUCHKEY_BRIGHTNESS = "/sys/class/sec/sec_touchkey/brightness";
    private static final String FILE_TOUCHKEY_DISABLE = "/sys/class/sec/sec_touchkey/force_disable";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.screen_preferences);
        Resources res = getResources();

        mDNIeScenario = (mDNIeScenario) findPreference(DisplaySettings.KEY_MDNIE_SCENARIO);
        mDNIeScenario.setEnabled(isSupported(mDNIeScenario.getPathArrayResId()));

        mDNIeMode = (mDNIeMode) findPreference(DisplaySettings.KEY_MDNIE_MODE);
        mDNIeMode.setEnabled(isSupported(mDNIeMode.getPathArrayResId()));

        mDNIeNegative = (mDNIeNegative) findPreference(DisplaySettings.KEY_MDNIE_NEGATIVE);
        mDNIeNegative.setEnabled(isSupported(mDNIeNegative.getPathArrayResId()));

        /* Touchkey */
        sTouchkeySupport = res.getBoolean(R.bool.has_touchkey); 
        mTouchKey = (CheckBoxPreference) findPreference(DisplaySettings.KEY_TOUCHKEY_LIGHT);
        mTouchKey.setEnabled(sTouchkeySupport);

        mTouchKeyTimeout = (TouchkeyTimeout) findPreference(DisplaySettings.KEY_TOUCHKEY_TIMEOUT);

        if (mTouchKey.isChecked() && mTouchKey.isEnabled()) {
            mTouchKeyTimeout.setEnabled(mTouchKeyTimeout.isSupported());
        } else {
            mTouchKeyTimeout.setEnabled(false);
        }
    }

    private boolean isSupported(int pathArrayResId) {
        final String[] filePaths = getResources().getStringArray(pathArrayResId);
        final String filePath = mDNIeBasePreference.isSupported(filePaths);
        return !TextUtils.isEmpty(filePath);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
          String key = preference.getKey();
 
          Log.w(TAG, "key: " + key);
  
        if (key.compareTo(DisplaySettings.KEY_TOUCHKEY_LIGHT) == 0) {
            if (((CheckBoxPreference)preference).isChecked()) {
                Utils.writeValue(FILE_TOUCHKEY_DISABLE, "0");
                Utils.writeValue(FILE_TOUCHKEY_BRIGHTNESS, "1");
                mTouchKeyTimeout.setEnabled(mTouchKeyTimeout.isSupported());
            } else {
                Utils.writeValue(FILE_TOUCHKEY_DISABLE, "1");
                Utils.writeValue(FILE_TOUCHKEY_BRIGHTNESS, "2");
                mTouchKeyTimeout.setEnabled(false);
            }
        }
        return true;
    }
    public static boolean isSupported(String FILE) {
        return Utils.fileExists(FILE);
    }
  
    public static void restore(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean light = sharedPrefs.getBoolean(DisplaySettings.KEY_TOUCHKEY_LIGHT, true);

        Utils.writeValue(FILE_TOUCHKEY_DISABLE, light ? "0" : "1");
        Utils.writeValue(FILE_TOUCHKEY_BRIGHTNESS, light ? "1" : "2");
      }
   }
