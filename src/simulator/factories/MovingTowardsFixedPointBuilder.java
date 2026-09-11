package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws>{

	public MovingTowardsFixedPointBuilder() {
		super("mtfp", "Moving towards a fixed point");
	
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		
		Vector2D c = new Vector2D();
		double g = 9.8;
			
		if (data.has("c")) {
			
			JSONArray vc =data.getJSONArray("c");
			c = new Vector2D (vc.getDouble(0), vc.getDouble(1));				
		}
		
		if (data.has("g")){			
				
			g = data.getDouble("g");
		}
		
		return new MovingTowardsFixedPoint (c,g);
	}
	
	@Override  //nuevo XXXXXXXXXXXXXXXXXXX
	
	public JSONObject getInfo() {
		
		JSONObject ja = new JSONObject();
		JSONObject ja1 = new JSONObject();
		
		ja.put("type","mtfp");
		ja.put("desc","Moving towards a fixed point");
		ja1.put("c", "the point towards which bodies move (e.g., [100.0,50.0])");
		ja1.put("g", "the length of the acceleration vector (a number)");
	    ja.put("data", ja1);
	    
		return ja;
	}

}
