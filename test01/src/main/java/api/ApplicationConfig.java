package api;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;



@ApplicationPath("api")
public class ApplicationConfig extends ResourceConfig {
/*
	public static class Hoge {
		public Properties props = new Properties();
		public Hoge(){
			System.out.print(this.hashCode());
			InputStream is = this.getClass().getResourceAsStream("myproperties.properites");
	        try {
	        	props.load(is);
        		is.close();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
		}
	}
*/
	public ApplicationConfig(@Context ServletContext servletContext) {
        packages(this.getClass().getPackage().getName());
        
		InputStream is = servletContext.getResourceAsStream("WEB-INF/myproperties.properites");
		servletContext.setAttribute(null, null);
		System.out.println(is);
/*
        register(new AbstractBinder() {
            @Override
            protected void configure() {
            	bindAsContract(Hoge.class).in(Singleton.class);
//            	this.bind(Hoge.class).in(Singleton.class);
            }
        });
*/        
//        ServletContext.getResourceAsStream("WEB-INF/myproperties.properites");
//      packages("com.github.kamegu.first");
    }


}
