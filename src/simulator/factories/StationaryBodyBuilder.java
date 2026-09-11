package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.StationaryBody;

public class StationaryBodyBuilder extends Builder<Body> {

	public StationaryBodyBuilder() {
		super("st_body", "Stationary body");
	}


	@Override
	protected Body createInstance(JSONObject data) {
		
		if(data == null || !data.has("id") || !data.has("gid") || !data.has("p") || !data.has("m")) {
			throw new IllegalArgumentException("data no valido o faltan cosas");
		}
		
		String id = data.getString("id");
		String gid = data.getString("gid");
		double masa = data.getDouble("m");
				
		JSONArray vPosicion =data.getJSONArray("p");
				
		if(vPosicion.length()!=2 ) {
					throw new IllegalArgumentException();
				}
				
		Vector2D vectorPosicion = new Vector2D (vPosicion.getDouble(0), vPosicion.getDouble(1));
				
		
		return new StationaryBody (id, gid, vectorPosicion, masa );
			
	}
	

}


