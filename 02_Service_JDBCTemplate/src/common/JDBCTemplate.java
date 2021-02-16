package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Service, Dao클래스의 공통부분을 static 메소드 제공
 * 예외처리를 공통부분에서 작성하므로 사용(호출)하는 쪽의 코드를 간결히 할 수 있다.
 * 
 *
 */
public class JDBCTemplate {
	static String driverClass = "oracle.jdbc.OracleDriver";
    static String url = "jdbc:oracle:thin:localhost:1521:xe";
    //@khmclass.iptime.org:1521:xe -> 선생님컴퓨터로 접속가능한 주소 
    static String user = "student";
    static String password = "student";

	public static Connection getConnection() {
		Connection conn = null;
		//1. DriverClass등록(최초1회)
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e1) {
			
			e1.printStackTrace();
		}
		try {
			
			
		
			//2. connection객체 생성 url, user, password
			conn = DriverManager.getConnection(url, user, password);
			 //2.1 자동커밋 false 설정
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return conn;
	}
	public static void close(Connection conn) {
		 //7. 자원반납(conn) 생성 역순 
		 try {
			if(conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rset) {
		try {
			if(rset != null)
				rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if(pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

		public static void commit(Connection conn) {
			try {
				if(conn != null && !conn.isClosed())	
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		public static void rollback(Connection conn) {
			try {
				if(conn != null && !conn.isClosed())
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
}
