package bgmap.core.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bgmap.core.AppConfig;
import bgmap.core.model.Maf;
import bgmap.core.model.MafHashKey;
import bgmap.core.model.MafHashMap;
import bgmap.core.model.MafHashValue;
import bgmap.core.model.Map;

public class DBManager {
	static final String JDBC_DRIVER = "org.sqlite.JDBC";
	static final String DB_URL = "jdbc:sqlite:map.db";
	
	public static void createDb() throws SQLException {	
		String sql = "create table if not exists bgmap " +
					 "(x int not null," +
					  "y int not null," +
					  "colNum tinyint not null," +
					  "rowNum tinyint not null," +
					  "subjectName text not null," +
					  "subjectAddress text," +					  
					  "subjectRegNum int," +
					  "telephone text," +
					  "site text," +
					  "purpose text," +
					  "objectAddress text," +
					  "techCharacteristics text," +
					  "passport text," +
					  "personFullName text," +
					  "PRIMARY KEY (x,y,colnum,rownum));";
		Connection conn = null;
		Statement stmt = null;
		
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);				
		} catch (Exception e) {
			AppConfig.lgTRACE.error(e);
            AppConfig.lgWARN.error(e);
            System.exit(1);	
		}
		
		if (stmt!=null) stmt.close();
		if (conn!=null) conn.close();
	}
	
	public static Maf readMaf(short x, short y, byte colNum, byte rowNum) throws SQLException{
		Maf result = null;
		Connection conn = null;
		Statement stmt = null;
		String fromWhere= "from bgmap where x="+x+" and y="+y+" and colNum="+colNum+" and rowNum="+rowNum +" ";
		//String sql = "select count(*) as RECORDCOUNT,null,null,null,null,null,null,null,null,null,null,null,null,null "+ fromWhere+"UNION ALL "+
		String sql = "select * "+fromWhere;
			try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(sql);	
	/*		int rowcount = 0;
			if (rs.next())
				rowcount = rs.getInt("RECORDCOUNT");*/
			while(rs.next())
				result = new Maf(x, y, colNum, rowNum, 
					rs.getString("subjectName"),
					rs.getString("subjectAddress"),
					rs.getInt("subjectRegNum"),
					rs.getString("telephone"),
					rs.getString("site"),
					rs.getString("purpose"),					
					rs.getString("objectAddress"),
					rs.getString("techCharacteristics"),
					rs.getString("passport"),
					rs.getString("personFullName"));
							
		} catch (Exception e) {
			AppConfig.lgTRACE.error(e);
            AppConfig.lgWARN.error(e);
            System.exit(1);	
		}
		
		if (stmt!=null) stmt.close();
		if (conn!=null) conn.close();
		return result;
	}
	
	public static void insertMaf(Maf maf) throws SQLException{
		if (readMaf((short)maf.getX(),(short) maf.getY(), maf.getColNum(), maf.getRowNum()) == null){
			Connection conn = null;
			Statement stmt = null;
			String sql = "INSERT INTO bgmap(x, y, colNum, rowNum, subjectName, subjectAddress, subjectRegNum,"
					+ "telephone, site, purpose, objectAddress, techCharacteristics, passport, personFullName)"
					+ " VALUES('"+ 
					maf.getX() + "','"+
					maf.getY() + "','"+
					maf.getColNum() + "','"+
					maf.getRowNum() + "','"+
					maf.getSubjectName() + "','"+
					maf.getSubjectAddress() + "','"+
					maf.getSubjectRegNum() + "','"+
					maf.getTelephone() + "','"+
					maf.getSite() + "','"+
					maf.getPurpose() + "','"+
					maf.getObjectAddress() + "','"+
					maf.getTechCharacteristics() + "','"+
					maf.getPassport() + "','"+
					maf.getPersonFullName()+"')";	
			try {
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL);
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);				
			} catch (Exception e) {
				AppConfig.lgTRACE.error(e);
	            AppConfig.lgWARN.error(e);
	            System.exit(1);	
			}			
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
		}
	}
	
	
	public static MafHashMap selectMafs(byte colNum1,byte colNum12, byte rowNum1, byte rowNum2) throws SQLException{
		MafHashMap result =  new MafHashMap(); 
		Connection conn = null;
		Statement stmt = null;
		//rs = st.executeQuery("");
		//String sql = "INSERT INTO Employees(inn,name,surname,salary) VALUES(?,?,?,?)";

		String fromWhere= "from bgmap where colNum BETWEEN '"+colNum1+"' and '"+colNum12+"' and rowNum BETWEEN '"+rowNum1 +"' and '"+rowNum2+"'";
		String sql = "select * "+fromWhere;
		System.out.println(sql);
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(sql);	
		
			while(rs.next())	{
				MafHashKey key = new MafHashKey(rs.getByte("colNum"),rs.getByte("rowNum"));
				if (!result.containsKey(key))
					result.put(key, new ArrayList<MafHashValue>());
				ArrayList<MafHashValue> list = result.get(key);
				list.add(new MafHashValue(rs.getShort("x"),rs.getShort("y")));
				result.setMafValue(list);
			}

				/*result.put(
						new MafHashKey(rs.getByte("colNum"),rs.getByte("rowNum")),
						new MafHashValue(rs.getInt("x"),rs.getInt("y"))*/
					/*	new Maf(
					rs.getInt("x"),
					rs.getInt("y"),
					rs.getByte("colNum"),
					rs.getByte("rowNum"),
					rs.getString("subjectName"),
					rs.getString("subjectAddress"),
					rs.getInt("subjectRegNum"),
					rs.getString("telephone"),
					rs.getString("site"),
					rs.getString("purpose"),					
					rs.getString("objectAddress"),
					rs.getString("techCharacteristics"),
					rs.getString("passport"),
					rs.getString("personFullName"))
					);*/
							
		} catch (Exception e) {
			AppConfig.lgTRACE.error(e);
            AppConfig.lgWARN.error(e);
            System.exit(1);	;
		}
		
		if (stmt!=null) stmt.close();
		if (conn!=null) conn.close();
		return result;
	}
	
	public static void updateMaf(Maf maf) throws SQLException{
		if (readMaf((short)maf.getX(),(short) maf.getY(), maf.getColNum(), maf.getRowNum()) != null){
			Connection conn = null;
			Statement stmt = null;
			String where= " where x="+maf.getX()+" and y="+maf.getY()+" and colNum="+maf.getColNum()+" and rowNum="+maf.getRowNum() +" ";
	
			String sql = "UPDATE bgmap SET "+
					"subjectName= '"+ maf.getSubjectName() + "',"+ 
					"subjectAddress='" + maf.getSubjectAddress() + "',"+
					"subjectRegNum='"+ maf.getSubjectAddress() + "',"+
					"telephone='"+ maf.getTelephone() + "',"+
					"site='" + maf.getSite() + "',"+
					"purpose='" + maf.getPurpose() + "',"+
					"objectAddress='" + maf.getObjectAddress() + "',"+ 
					"techCharacteristics='" + maf.getTechCharacteristics() + "',"+				 
					"passport='" + 	maf.getPassport() + "',"+
					"personFullName='"+ maf.getPersonFullName()+"' " + where;
					 
			System.out.println(sql);
			try {
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL);
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);				
			} catch (Exception e) {
				AppConfig.lgTRACE.error(e);
	            AppConfig.lgWARN.error(e);
	            System.exit(1);	
			}			
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
		}
	}
	
	public static void deleteMafs(byte colNum1,byte colNum12, byte rowNum1, byte rowNum2) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		//rs = st.executeQuery("");
		//String sql = "INSERT INTO Employees(inn,name,surname,salary) VALUES(?,?,?,?)";
		String fromWhere= "from bgmap where colNum BETWEEN '"+colNum1+"' and '"+colNum12+"' and rowNum BETWEEN '"+rowNum1 +"' and '"+rowNum2+"'";
		String sql = "delete * "+fromWhere;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);		
			System.out.println(sql);
		} catch (Exception e) {
			AppConfig.lgTRACE.error(e);
            AppConfig.lgWARN.error(e);
            System.exit(1);	
		}
		
		if (stmt!=null) stmt.close();
		if (conn!=null) conn.close();
	}
}
