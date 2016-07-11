package model;

import java.util.ArrayList;

/**
 * Enumeration of region name
 */
public enum RegionName {
    COAST,
    HILLS,
    MOUNTAINS;
	
	public static ArrayList<String> getRegionNames() {
		ArrayList<String> names = new ArrayList<String>();
		RegionName[] values = RegionName.values();
		for(int i=0;i<RegionName.values().length;i++) {
			names.add(String.valueOf(values[i]));
		}
		return names;
	}
}