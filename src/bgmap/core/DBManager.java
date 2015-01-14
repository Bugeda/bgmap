package bgmap.core;

import java.sql.*;
import java.util.HashSet;

import bgmap.core.entity.Maf;

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
					  "subjectAddress text not null," +					  
					  "subjectRegNum int not null," +
					  "telephone text not null," +
					  "site text not null," +
					  "purpose text not null," +
					  "objectAddress text not null," +
					  "techCharacteristics text not null," +
					  "passport text not null," +
					  "personFullName text not null," +
					  "PRIMARY KEY (x,y,colnum,rownum));";
		Connection conn = null;
		Statement stmt = null;
		
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);				
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		if (stmt!=null) stmt.close();
		if (conn!=null) conn.close();
	}
	
	public static Maf readMaf(int x, int y, byte colNum, byte rowNum) throws SQLException{
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
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		if (stmt!=null) stmt.close();
		if (conn!=null) conn.close();
		return result;
	}
	
	public static void insertMaf(Maf maf) throws SQLException{
		if (readMaf(maf.getX(), maf.getY(), maf.getColNum(), maf.getRowNum()) == null){
			Maf result = null;
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
				System.out.println(sql);
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				e.printStackTrace();				
				System.exit(1);
			}			
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
		}
	}
	
	
	public static HashSet<Maf> selectMafs(byte colNum1,byte colNum12, byte rowNum1, byte rowNum2) throws SQLException{
		HashSet<Maf> result = new HashSet<Maf>();
		Connection conn = null;
		Statement stmt = null;
		//rs = st.executeQuery("");
		//String sql = "INSERT INTO Employees(inn,name,surname,salary) VALUES(?,?,?,?)";
		String fromWhere= "from bgmap where colNum BETWEEN '"+colNum1+"' and '"+colNum12+"' and rowNum BETWEEN '"+rowNum1 +"' and '"+rowNum2+"'";
		String sql = "select * "+fromWhere;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(sql);	
			while(rs.next())
				result.add(new Maf(
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
					);
							
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		if (stmt!=null) stmt.close();
		if (conn!=null) conn.close();
		return result;
	}
}
