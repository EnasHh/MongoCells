package globitel.cellular;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;


import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//POST: localhost:8080/Mongocellular/rest/coordinates
// test JSON :{"name":"Area","coordinates":"35.871122, 32.013006a35.872184, 32.012656a35.871927, 32.012073a35.870859, 32.012414a35.871122, 32.013006"}
//name your DB "cellular" then add cells collection
//creating 2dsphere index is optional
@Path("/coordinates")
public class Area {

	@POST
	
	@Produces(MediaType.APPLICATION_JSON)
	public Response createArea (Coordinates coor) {
		
		 
		String coordinates=coor.getCoordinates();
		System.out.println(coor.getName());
		coor.search(coordinates);
		coor.insertArea();
		
		return null;}
	
	}
