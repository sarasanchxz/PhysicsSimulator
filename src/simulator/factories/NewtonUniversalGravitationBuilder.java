package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {

	public NewtonUniversalGravitationBuilder() {
		
		super("nlug", "Newton’s law of universal gravitation");
		
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) throws IllegalArgumentException { // porque hay que poner eso aqui y en las otras no
		
		double G = 6.67E-11 ;
	
		if(data.has("G")) {
			G=data.getDouble("G");
		}
			
		return new NewtonUniversalGravitation (G);
	}

	@Override  //nuevo XXXXXXXXXXXXXXXXXXX
	public JSONObject getInfo() {
		
		JSONObject ja = new JSONObject();
		JSONObject ja1 = new JSONObject();
		
		ja.put("type","nlug");
		ja.put("desc","Newton’s law of universal gravitation");
		ja1.put("G", "the gravitational constant (a number)");
	    ja.put("data", ja1);
	    
		return ja;
	}
	
	

}
