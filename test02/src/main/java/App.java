import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.log4jdbc.ConnectionSpy;
import net.sf.log4jdbc.PreparedStatementSpy;
import net.sf.log4jdbc.StatementSpy;
import test02.WrapperPreparedStatementSpy;

public class App {

	public  class Wrd {
		private String name;
		public String getName(){return name;}
		public Object getChildren(){return null;}
	}

	public  class Cond extends Wrd {
		public List<? extends Wrd> lst=null; 
		public Object getChildren(){return lst;}
	}

	public  class Exp extends Wrd {
		public Object obj=null; 
		public Object getChildren(){return obj;}
	}
	public  class IntExp extends Wrd {
		public Integer obj=null; 
		public Integer getChildren(){return obj;}
	}
	public  class DecimalExp extends Wrd {
		public BigDecimal obj=null; 
		public BigDecimal getChildren(){return obj;}
	}
	public  class StringExp extends Wrd {
		public String obj=null; 
		public String getChildren(){return obj;}
	}

	
	
	public static void main(String[] args) {
		try{
		new App().test();

		}catch(Exception e){
			e.getMessage();
		}
		
	}
	public void test() throws Exception {
		String json="{\"header\":{\"a1\":\"b1\"},\"body\":{\"where\":{\"and\":[{\"x1\":\"y1\"},{\"x2\":\"y2\"}]}}}";
		
		ObjectMapper mapper = new ObjectMapper();
       
		Map map= mapper.readValue(json, Map.class);
		System.out.println(map.get("header"));
		
	}
	public void test01() throws Exception {

		
		
		
		Connection con=null;
		Connection pcon;

		pcon=App.getProxy(con);

		pcon.prepareStatement("select * from hoge");
		
		
	}
	public static Connection getProxy(final Connection origin) {
		
		final ConnectionSpy cspy=new ConnectionSpy(origin);
		
		ClassLoader cl = Connection.class.getClassLoader();
		Class<?>[] interfaces = new Class<?>[]{Connection.class};

		InvocationHandler handler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] param) throws Throwable {
				
				if("prepareStatement".equals(method.getName())){
					if(param.length==1){
						return new WrapperPreparedStatementSpy((String)param[0],cspy,origin.prepareStatement((String)param[0]));
					}else if(param.length==2){
						if(param[1].getClass()==Integer.class){
							if(param[1].getClass().isArray()){
								return new WrapperPreparedStatementSpy((String)param[0],cspy,origin.prepareStatement((String)param[0],(int[])param[1]));
							}else{
								return new WrapperPreparedStatementSpy((String)param[0],cspy,origin.prepareStatement((String)param[0],(int)param[1]));
							}
						}else if(param[1].getClass()==String.class){
							if(param[1].getClass().isArray()){
								return new WrapperPreparedStatementSpy((String)param[0],cspy,origin.prepareStatement((String)param[0],(String[])param[1]));
							}
						}
					}else if(param.length==3){
						if(param[1].getClass()==Integer.class && param[2].getClass()==Integer.class){
							return new WrapperPreparedStatementSpy((String)param[0],cspy,origin.prepareStatement((String)param[0],(int)param[1],(int)param[2]));
						}
					}else if(param.length==4){
						if(param[1].getClass()==Integer.class && param[2].getClass()==Integer.class && param[3].getClass()==Integer.class){
							return new WrapperPreparedStatementSpy((String)param[0],cspy,origin.prepareStatement((String)param[0],(int)param[1],(int)param[2],(int)param[3]));
						}
					}	
					throw new RuntimeException("unsupport methood !! ");
					
				} else {
					return method.invoke(proxy, param);
				}
			}
		};

		Connection c = (Connection) Proxy.newProxyInstance(cl, interfaces, handler);
		return c;
	}

}
