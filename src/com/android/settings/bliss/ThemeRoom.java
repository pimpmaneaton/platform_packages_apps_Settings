package com.android.settings.bliss;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.ServiceManager;
import android.os.UserHandle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;

import java.util.Arrays;
import java.util.HashSet;

import com.android.settings.SettingsPreferenceFragment;
import com.bliss.support.colorpicker.ColorPickerPreference;

public class ThemeRoom extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String ACCENT_COLOR = "accent_color";
    private static final String ACCENT_COLOR_PROP = "accent_color_prop";
    private static final String GRADIENT_COLOR = "gradient_color";
    private static final String GRADIENT_COLOR_PROP = "gradient_color_prop";

    private ColorPickerPreference mAccentColor;
    private ColorPickerPreference mGradientColor;


    static final int ACCENT = 0xFF1A73E8;
    static final int GRADIENT = 0xFF1AD8E8;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        ContentResolver resolver = getActivity().getContentResolver();

        addPreferencesFromResource(R.xml.theme_room);
        mAccentColor = (ColorPickerPreference) findPreference(ACCENT_COLOR);
        mAccentColor.setOnPreferenceChangeListener(this);
        int accentColor = Settings.System.getIntForUser(resolver,
                Settings.System.ACCENT_COLOR_PROP, ACCENT, UserHandle.USER_CURRENT);
        String accColor = String.format("#%08x", (0xFFFFFFFF & accentColor));
        if (accColor.equals("#ff1a73e8")) {
            mAccentColor.setSummary(R.string.default_string);
        } else {
            mAccentColor.setSummary(accColor);
        }
        mAccentColor.setNewPreviewColor(accentColor);

        mGradientColor = (ColorPickerPreference) findPreference(GRADIENT_COLOR);
        mGradientColor.setOnPreferenceChangeListener(this);
        int gradientColor = Settings.System.getIntForUser(resolver,
                Settings.System.GRADIENT_COLOR_PROP, GRADIENT, UserHandle.USER_CURRENT);
        String gradColor = String.format("#%08x", (0xFFFFFFFF & gradientColor));
        if (gradColor.equals("#ff1ad8e8")) {
            mGradientColor.setSummary(R.string.default_string);
        } else {
            mGradientColor.setSummary(gradColor);
        }
        mGradientColor.setNewPreviewColor(gradientColor);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mAccentColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#ff1a73e8")) {
                mAccentColor.setSummary(R.string.default_string);
            } else {
                mAccentColor.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putIntForUser(resolver,
                    Settings.System.ACCENT_COLOR_PROP, intHex, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mGradientColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#ff1ad8e8")) {
                mGradientColor.setSummary(R.string.default_string);
            } else {
                mGradientColor.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putIntForUser(resolver,
                    Settings.System.GRADIENT_COLOR_PROP, intHex, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.BLISSIFY;
    }
}


