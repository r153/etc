package api;

import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.ResourceConfig;

import api.ApplicationConfig.Hoge;

@Path("greeting/{name}")
public class GreetingResource {

//	@Inject
//	private Hoge hoge;
	
	
	@Context
	private ServletContext sc;
	
	
    @GET
    @Produces("text/plain")
    public String sayHello(@PathParam("name") String name) {

    	
    	InputStream is=sc.getResourceAsStream("WEB-INF/myproperties.properites");
    	
    	return "hello," + name + "!";
    }
}