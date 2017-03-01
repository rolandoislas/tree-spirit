package com.rolandoislas.treespirit.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.rolandoislas.treespirit.TreeSpirit;
import com.rolandoislas.treespirit.data.spirit.SpiritData;

import java.io.*;

/**
 * Created by Rolando on 2/28/2017.
 */
public class JsonUtil {
	private static File file;
	private static SpiritData spiritData;

	public static void setFile(File file) {
		JsonUtil.file = file;
		if (!JsonUtil.file.isFile()) {
			try {
				JsonUtil.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static SpiritData getSpiritData(boolean forceUpdate) {
		if (spiritData != null && !forceUpdate)
			return spiritData;
		Gson gson = new Gson();
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			TreeSpirit.logger.catching(e);
			return spiritData;
		}
		spiritData = gson.fromJson(reader, SpiritData.class);
		if (spiritData == null)
			spiritData = new SpiritData();
		return spiritData;
	}

	public static SpiritData getSpiritData() {
		return getSpiritData(false);
	}

	static void setSpiritData(SpiritData data) {
		Gson gson = new Gson();
		FileWriter writer;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			TreeSpirit.logger.catching(e);
			return;
		}
		String json = gson.toJson(data);
		try {
			writer.write(json);
		} catch (IOException e) {
			TreeSpirit.logger.catching(e);
		}
		try {
			writer.close();
		} catch (IOException e) {
			TreeSpirit.logger.catching(e);
		}
		getSpiritData(true);
	}
}
