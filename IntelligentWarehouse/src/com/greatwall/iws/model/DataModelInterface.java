package com.greatwall.iws.model;

import java.util.List;
import java.util.Map;

public abstract class DataModelInterface {
	
	public abstract List<Map<String, String>> onAnalysis(String datas);
}
