package com.rolandoislas.treespirit.data.spirit;

import java.util.HashMap;

/**
 * Created by Rolando on 2/28/2017.
 */
public class SpiritData {
	private HashMap<String, SpiritWorldData> worlds = new HashMap<String, SpiritWorldData>();

	public SpiritWorldData getWorld(String worldId) {
		if (!worlds.containsKey(worldId))
			worlds.put(worldId, new SpiritWorldData());
		return worlds.get(worldId);
	}
}
