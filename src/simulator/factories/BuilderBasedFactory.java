package simulator.factories;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T>{
	private Map<String,Builder<T>> _builders;
	private List<JSONObject> _buildersInfo;
	
	public BuilderBasedFactory() {
		_builders= new HashMap<> ();
		_buildersInfo =new LinkedList<>();
	}
	
	public BuilderBasedFactory(List<Builder<T>> builders) {
		
		this();		
		for(Builder<T> bu: builders) {
			
			addBuilder(bu);
		}
		
	}

	public void addBuilder(Builder<T> b) {
		 _builders.put(b.getTypeTag(), b);
		 _buildersInfo.add(b.getInfo());

	}
	
	//FALTAN COSAS AQUI
	@Override
	public T createInstance(JSONObject info) {
		JSONObject data;
		
		if (info == null) {
			throw new IllegalArgumentException("Invalid value for createInstance: null");
		}
		else if(!_builders.containsKey(info.getString("type")) ) {
			
			throw new IllegalArgumentException("Invalid value for createInstance: " + info.toString());	
			
		}
		
		if(info.has("data")) {
			data=info.getJSONObject("data");
		}
		else {
			data=new JSONObject();
		}
		
		return _builders.get(info.getString("type")).createInstance(data);
	
	}
	
	@Override
	public List<JSONObject> getInfo() {
		return Collections.unmodifiableList(_buildersInfo);
	}
	
}

