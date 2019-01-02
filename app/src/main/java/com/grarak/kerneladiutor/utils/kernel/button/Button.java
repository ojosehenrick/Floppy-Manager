/*
 * Copyright (C) 2019 Jonas Cardoso <jonascard60@gmail.com>
 *
 * This file is part of Floppy Manager.
 *
 * Kernel Adiutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Adiutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Adiutor.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.grarak.kerneladiutor.utils.kernel.button;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;
import com.grarak.kerneladiutor.utils.root.RootUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by willi on 29.06.16.
 */
public class Button {
	
    private static Button sInstance;

    public static Button getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Button();
        }
        return sInstance;
    }

	private static final String FP_WAKE = "/sys/devices/soc/soc:fpc_fpc1020/enable_wakeup";	
    private static final String FP_BOOST = "/sys/kernel/fp_boost/enabled";	
    private static final String FP_HOME = "/sys/devices/soc/soc:fpc_fpc1020/enable_key_events";
	private static final String CYTTSP_BUTTON = "/proc/buttons/reversed_keys_enable";
	private static final String TOUCHPANEL_BUTTON = "/proc/touchpanel/reversed_keys_enable";
	
	public void enableFPWake(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", FP_WAKE), FP_WAKE, context);
    }

    public boolean isFPWakeEnabled() {
        return Utils.readFile(FP_WAKE).equals("1");
    }

    public boolean hasFPWake() {
        return Utils.existFile(FP_WAKE);
    }
	
	public void enableFPBoost(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", FP_BOOST), FP_BOOST, context);
    }

    public boolean isFPBoostEnabled() {
        return Utils.readFile(FP_BOOST).equals("1");
    }

    public boolean hasFPBoost() {
        return Utils.existFile(FP_BOOST);
    }

	public void enableFPHome(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", FP_HOME), FP_HOME, context);
    }

    public boolean isFPHomeEnabled() {
        return Utils.readFile(FP_HOME).equals("1");
    }

    public boolean hasFPHome() {
        return Utils.existFile(FP_HOME);
    }
	
	public void enableCYTTSPButton(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", CYTTSP_BUTTON), CYTTSP_BUTTON, context);
    }

    public boolean isCYTTSPButtonEnabled() {
        return Utils.readFile(CYTTSP_BUTTON).equals("1");
    }

    public boolean hasCYTTSPButton() {
        return Utils.existFile(CYTTSP_BUTTON);
    }

	public void enableTOUCHPANELButton(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", TOUCHPANEL_BUTTON), TOUCHPANEL_BUTTON, context);
    }

    public boolean isTOUCHPANELButtonEnabled() {
        return Utils.readFile(TOUCHPANEL_BUTTON).equals("1");
    }

    public boolean hasTOUCHPANELButton() {
        return Utils.existFile(TOUCHPANEL_BUTTON);
    }
	
    public boolean supported() {
        return hasFPWake() || hasFPBoost() || hasFPHome() || hasCYTTSPButton() || hasTOUCHPANELButton();
    }
	
    private void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.BUTTON, id, context);
    }

}
