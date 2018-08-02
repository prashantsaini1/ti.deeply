/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2017 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.deeply;

import java.util.HashMap;
import java.util.Iterator;

import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;

import android.os.Bundle;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;


@Kroll.module(name="TiDeeply", id="ti.deeply", propertyAccessors = {
	"callback"
})
public class TiDeeplyModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "ti.deeply.TiDeeplyModule";
	private static final boolean DBG = TiConfig.LOGD;

	public static final String INTENT_DATA = "tideeply.data";
	public static final String INTENT_ACTION = "tideeply.action";
	public static final String INTENT_EXTRAS = "tideeply.extras";

	private static TiDeeplyModule module = null;

	private KrollFunction deepLinkCallback = null;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;

	public TiDeeplyModule() {
		super();
		module = this;
	}

	public static TiDeeplyModule getModule() {
		if (module != null) return module;
		else return new TiDeeplyModule();
	}

	public void parseBootIntent() {
		try {
			Intent intent = TiApplication.getAppRootOrCurrentActivity().getIntent();
			String data = intent.getData();
			String action = intent.getAction();
			Bundle extras = intent.getExtras();

			if (data != null) {
				sendDeepLink(data, action, extras, true);
				intent.removeExtra(INTENT_DATA);
				intent.removeExtra(INTENT_ACTION);
				intent.removeExtra(INTENT_EXTRAS);
			} else {
				Log.d(LCAT, "Empty data in Intent");
			}
		} catch (Exception ex) {
			Log.e(LCAT, "parseBootIntent" + ex);
		}
	}

	public void sendDeepLink(String data, String action, Bundle extras, Boolean inBackground) {
		if (deepLinkCallback == null) {
			Log.e(LCAT, "sendMessage invoked but no deepLinkCallback defined");
			return;
		}

		HashMap<String, Object> e = new HashMap<String, Object>();
		e.put("data", data); // to parse on reverse on JS side
		e.put("action", action);
		e.put("extras", convertBundleToMap(extras));
		e.put("inBackground", inBackground);

		deepLinkCallback.callAsync(getKrollObject(), e);
	}

	private static Map<String, Object> convertBundleToMap(Bundle bundle) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Iterator<String> keys = bundle.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			map.put(key, bundle.get(key));
		}
		return map;
	}

	@Kroll.method
	public void setDeepLinkCallback(KrollFunction callback) {
		deepLinkCallback = callback;
	}

}
