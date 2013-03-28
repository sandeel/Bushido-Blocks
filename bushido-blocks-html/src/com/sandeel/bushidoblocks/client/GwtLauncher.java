package com.sandeel.bushidoblocks.client;

import com.sandeel.bushidoblocks.DesktopLeaderboard;
import com.sandeel.bushidoblocks.BushidoBlocks;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(800, 400);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new BushidoBlocks(new DesktopLeaderboard());
	}

	@Override
	public Net getNet() {
		// TODO Auto-generated method stub
		return null;
	}
}