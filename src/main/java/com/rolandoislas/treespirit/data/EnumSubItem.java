package com.rolandoislas.treespirit.data;

/**
 * Created by Rolando on 2/27/2017.
 */
public interface EnumSubItem {
	EnumSubItem getFromMeta(int meta);
	int getMeta();
	String getUnlocalizedName();
}
