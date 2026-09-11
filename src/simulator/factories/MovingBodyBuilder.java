package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MovingBody;

public class MovingBodyBuilder extends Builder<Body> {

	public MovingBodyBuilder() {
		super("mv_body", "Moving body");
	}

	@Override
	protected Body createInstance(JSONObject data) {
		
		if(data == null || !data.has("id") || !data.has("gid") || !data.has("p") || !data.has("v") ||  !data.has("m")) {
			throw new IllegalArgumentException("data no valido o faltan cosas");
		}
		
		String id = data.getString("id");
		String gid = data.getString("gid");
		double masa = data.getDouble("m");
		
		JSONArray vPosicion =data.getJSONArray("p");
		JSONArray vVelocidad =data.getJSONArray("v");
		
		if (vPosicion.length()!=2 || vVelocidad.length()!=2 ) {
			throw new IllegalArgumentException(" No es un vector 2D");
		}
		
		Vector2D vectorPosicion = new Vector2D (vPosicion.getDouble(0), vPosicion.getDouble(1)); //array pos
		Vector2D vectorVelocidad =  new Vector2D (vVelocidad.getDouble(0), vVelocidad.getDouble(1));
			
		return new MovingBody( id, gid ,vectorPosicion, vectorVelocidad, masa); //cambiar masa
		 
	}

}
