package com.android.systemui.statusbar.powerwidget;

import com.android.systemui.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlashlightButton extends PowerButton {

    private Torch mTorch;
    private static final List<Uri> OBSERVED_URIS = new ArrayList<Uri>();
    static {
        OBSERVED_URIS.add(Settings.System.getUriFor(Settings.System.TORCH_STATE));
    }

    public FlashlightButton() { mType = BUTTON_FLASHLIGHT; }

    @Override
    protected void updateState(Context context) {
        boolean enabled = Settings.System.getInt(context.getContentResolver(), Settings.System.TORCH_STATE, 0) == 1;
        if(enabled) {
            mIcon = R.drawable.stat_flashlight_on;
            mState = STATE_ENABLED;
        } else {
            mIcon = R.drawable.stat_flashlight_off;
            mState = STATE_DISABLED;
        }
    }

    @Override
    protected void toggleState(Context context) {
        boolean torchOn = (mState == STATE_ENABLED);
        if (torchOn) {
        	Settings.System.putInt(context.getContentResolver(), Settings.System.TORCH_STATE, 0);
        	mTorch = Torch.getTorch();
            mTorch.finish();
        } else {
        	Settings.System.putInt(context.getContentResolver(), Settings.System.TORCH_STATE, 1);
        	Intent i = new Intent(context, Torch.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    @Override
    protected boolean handleLongClick(Context context) {
        // it may be better to make an Intent action for the Torch
        // we may want to look at that option later
        // Intent intent = new Intent(Intent.ACTION_MAIN);
        // intent.setClassName("net.cactii.flash2", "net.cactii.flash2.MainActivity");
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // context.startActivity(intent);
        return false;
    }

    @Override
    protected List<Uri> getObservedUris() {
        return OBSERVED_URIS;
    }
}
