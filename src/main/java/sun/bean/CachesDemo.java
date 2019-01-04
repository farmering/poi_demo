package sun.bean;

import java.util.List;
import java.util.Map;

public class CachesDemo {
	
	private String id;
	
	private Map<String, List<String>> map;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, List<String>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<String>> map) {
		this.map = map;
	}

}
