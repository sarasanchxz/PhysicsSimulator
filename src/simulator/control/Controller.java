package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;
import simulator.model.ForceLaws;
import simulator.model.Body;

public class Controller {
	
	Factory <Body> Body;
	Factory <ForceLaws> Forcelaws;
	PhysicsSimulator PhysicsSimulator;
	
	public Controller(Factory<Body> Body,Factory <ForceLaws> Forcelaws, PhysicsSimulator PhysicsSimulator) {
		
		this.Body = Body;
		this.Forcelaws = Forcelaws;
		this.PhysicsSimulator = PhysicsSimulator;
			
	}
	
	public void loadData (InputStream in) {
		
		JSONObject jsonInput = new JSONObject(new JSONTokener(in)); 
		
		JSONArray groups = jsonInput.getJSONArray("groups"); //array de todos los grupos en JSON
		
		for(int i = 0; i < groups.length(); i++) {
			PhysicsSimulator.addGroup(groups.getString(i));	 // se van añadiendo los grupos al PhysicsSimulator 						
		}
		
		if(jsonInput.has("laws")) {		
		JSONArray laws= jsonInput.getJSONArray("laws");	 // igual aqui va un if
		                                                 // array bodies coge los JSON que contienen la clave "laws"
		for(int i=0;i<laws.length();i++) { 
			
			ForceLaws fl = Forcelaws.createInstance(laws.getJSONObject(i).getJSONObject("laws"));  //no se si aqui no iria otro for??
			PhysicsSimulator.setForceLaws(laws.getJSONObject(i).getString("id"), fl);
			
		}
		}
		JSONArray bodies= jsonInput.getJSONArray("bodies");  // array bodies coge los JSON que contienen la clave "bodies"
		
		for(int i=0; i<bodies.length();i++) {
				
			PhysicsSimulator.addBody(Body.createInstance(bodies.getJSONObject(i)));			
		}
			
	}
	
	
      public void run(int n, OutputStream out) {
		
		
		PrintStream p = new PrintStream(out);

		p.println("{");
		p.println("\"states\": [");
		
		for(int i=0;i<n;i++) {
			
			PhysicsSimulator.advance();
			p.println(PhysicsSimulator.getState());
			
			
			if (i<n-1) {
				
				p.print(", ");
			}
			
			
		}
		
		p.println("]");
		p.println("}");

	}
      
     //NUEVOS
      
     public void reset()  {	   
	    PhysicsSimulator.reset();	   
     }
     
     public void setDeltaTime(double dt) {   	 
    	 PhysicsSimulator.setDeltaTime(dt); 
     }
     
     public void addObserver(SimulatorObserver o) {
    	 PhysicsSimulator.addObserver(o);
     }
     
     public void removeObserver(SimulatorObserver o) {
    	 PhysicsSimulator.removeObserver(o);
     }
     
     public List<JSONObject> getForceLawsInfo(){   	 
		return Forcelaws.getInfo(); 	 
     }
     
     public void setForcesLaws(String gId, JSONObject info) {   	 
        ForceLaws fl = Forcelaws.createInstance(info);
    	PhysicsSimulator.setForceLaws(gId, fl);  	 	 
     }
     
     public void run(int n) {
    	 
    	for (int i=0; i<n; i++) {    		
    	PhysicsSimulator.advance(); //sigo sin saber que hace el advance SOS   	
    	}  	   	 
     }
     
     
     
     
     
     
     
     
     
      
      
}
		
		
		
		
		
		
		
		
		
	


