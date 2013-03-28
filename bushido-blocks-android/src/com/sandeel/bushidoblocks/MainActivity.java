/*
	Copyright © 2012 Sandeel Software

	This file is part of Bushido Blocks.

	Bushido Blocks is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sandeel.bushidoblocks;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.sandeel.bushidoblocks.BushidoBlocks;
import com.swarmconnect.Swarm;

public class MainActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Swarm.setActive(this);
        Swarm.init(this, ID, "KEY");

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        cfg.useAccelerometer = false;
        cfg.useCompass = false;

        super.onCreate(savedInstanceState);

        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(new BushidoBlocks(new AndroidLeaderboard(), new ActionResolverAndroid(this)), cfg);

        // Create and setup the AdMob view
        AdView adView = new AdView(this, AdSize.BANNER, "KEY"); // Put in your secret key here
        adView.loadAd(new AdRequest());

        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams =
        		new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
        				RelativeLayout.LayoutParams.WRAP_CONTENT);
        				adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        				adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layout.addView(adView, adParams);

        // Hook it all up
        setContentView(layout);

    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        Swarm.setActive(this);
    }

    public void onPause() {
        super.onPause();
        Swarm.setInactive(this);
    }
}