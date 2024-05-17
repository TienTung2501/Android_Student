package com.example.student;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

public class BatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = (level / (float) scale) * 100;

            if (batteryPct < 20) {
                Toast.makeText(context, "Pin yếu: " + (int) batteryPct + "%", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Mức pin: " + (int) batteryPct + "%", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
