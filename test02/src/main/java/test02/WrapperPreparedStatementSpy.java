package test02;

import java.sql.PreparedStatement;

import net.sf.log4jdbc.ConnectionSpy;
import net.sf.log4jdbc.PreparedStatementSpy;

public class WrapperPreparedStatementSpy extends PreparedStatementSpy {
	public WrapperPreparedStatementSpy(String sql,ConnectionSpy connectionSpy, PreparedStatement realPreparedStatement){
		super(sql,connectionSpy,realPreparedStatement);
	}
	public String toString(){
		return super.toString() +":"+dumpedSql();
	}
}
