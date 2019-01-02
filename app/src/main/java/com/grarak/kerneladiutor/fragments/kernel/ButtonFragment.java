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
package com.grarak.kerneladiutor.fragments.kernel;

import android.content.Context;
import android.os.Vibrator;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.kernel.button.Button;
import com.grarak.kerneladiutor.views.recyclerview.CardView;
import com.grarak.kerneladiutor.views.recyclerview.DescriptionView;
import com.grarak.kerneladiutor.views.recyclerview.GenericSelectView;
import com.grarak.kerneladiutor.views.recyclerview.RecyclerViewItem;
import com.grarak.kerneladiutor.views.recyclerview.SeekBarView;
import com.grarak.kerneladiutor.views.recyclerview.SelectView;
import com.grarak.kerneladiutor.views.recyclerview.SwitchView;
import com.grarak.kerneladiutor.views.recyclerview.TitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by willi on 26.06.16.
 */
public class ButtonFragment extends RecyclerViewFragment {

    private Button mButton;

    @Override
    protected void init() {
        super.init();

        mButton = Button.getInstance(requireActivity());
    }
	
    @Override
    protected void addItems(List<RecyclerViewItem> items) {
		if (mButton.hasFPWake() || mButton.hasFPBoost()) {
			fpInit(items);
		}	
    }
	
    @Override
    protected void postInit() {
        super.postInit();
		
        addViewPagerFragment(ApplyOnBootFragment.newInstance(this));
    }
	
    private void fpInit(List<RecyclerViewItem> items) {
        CardView fpCard = new CardView(getActivity());
        fpCard.setTitle(getString(R.string.fp));

		if (mButton.hasFPWake()) {
			SwitchView fpWake = new SwitchView();
			fpWake.setTitle(getString(R.string.fp_wake));
			fpWake.setSummary(getString(R.string.fp_wake_summary));
			fpWake.setChecked(mButton.isFPWakeEnabled());
			fpWake.addOnSwitchListener(new SwitchView.OnSwitchListener() {
				@Override
				public void onChanged(SwitchView switchView, boolean isChecked) {
					mButton.enableFPWake(isChecked, getActivity());
				}
			});
			
			fpCard.addItem(fpWake);

		}
		
		if (mButton.hasFPBoost()) {
			SwitchView fpBoost = new SwitchView();
			fpBoost.setTitle(getString(R.string.fp_boost));
			fpBoost.setSummary(getString(R.string.fp_boost_summary));
			fpBoost.setChecked(mButton.isFPBoostEnabled());
			fpBoost.addOnSwitchListener(new SwitchView.OnSwitchListener() {
				@Override
				public void onChanged(SwitchView switchView, boolean isChecked) {
					mButton.enableFPBoost(isChecked, getActivity());
				}
			});
			
			fpCard.addItem(fpBoost);

		}
		
		items.add(fpCard);
    }

}
