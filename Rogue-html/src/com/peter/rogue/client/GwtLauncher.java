package com.peter.rogue.client;

import com.peter.rogue.Rogue;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1280, 720);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new Rogue();
	}
}