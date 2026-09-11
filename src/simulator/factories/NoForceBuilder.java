package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {

	public NoForceBuilder() {
		super("nf", "no force");
		
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		
    if(data == null) {
	    throw new IllegalArgumentException("data no force vacio");
	}

	return new NoForce();
	
	}
	
	@Override //nuevo XXXXXXXXXXXXXXXXXXX
	
	public JSONObject getInfo() {
		
		JSONObject ja = new JSONObject();
		
		ja.put("type","nf");
		ja.put("desc","No force");
	    ja.put("data", "{}");
	    
		return ja;
	}

}
