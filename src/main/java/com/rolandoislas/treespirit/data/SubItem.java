package com.rolandoislas.treespirit.data;

/**
 * Created by Rolando on 2/27/2017.
 */
public interface SubItem {
	SubItem getFromMeta(int meta);
	int getMeta();
	String getUnlocalizedName();
}
