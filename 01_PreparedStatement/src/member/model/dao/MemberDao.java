package member.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import member.model.vo.Member;

/**
 * DAO
 * Data Access Object
 * DB에 접근하는 클래스
 * 
 * 1. 드라이버클래스 등록(최초1회)
 * 2. Connection객체 생성(url, user, password) 
 * 3. 자동커밋여부 설정 : true(기본값)/false -> app에서 직접 트랜잭션 제어
 * 4. PreparedStatement객체생성(미완성쿼리) 및 값대입
 * 5. Statement객체 실행. DB에 쿼리 요청
 * 6. 응답처리 DML:int리턴, DQL:ResultSet리턴->자바객체로 전환
 * 7. 트랜잭션처리(DML)
 * 8. 자원반납(생성의 역순)
 *
 */
public class MemberDao {
	String driverClass = "oracle.jdbc.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String user = "student";
	String password = "student";

	public int insertMember(Member member) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "insert into member values(?, ?, ?, ?, ?, ?, ?, ?, ?, default)";
		int result = 0;
		
		try {
			//1. 드라이버클래스 등록(최초1회)
			Class.forName(driverClass);
			//2. Connection객체 생성(url, user, password)
			conn = DriverManager.getConnection(url, user, password);
			//3. 자동커밋여부 설정(DML) : true(기본값)/false -> app에서 직접 트랜잭션 제어
			conn.setAutoCommit(false);
			//4. PreparedStatement객체생성(미완성쿼리) 및 값대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender());
			pstmt.setInt(5, member.getAge());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getAddress());
			pstmt.setString(9, member.getHobby());
			
			//5. Statement객체 실행. DB에 쿼리 요청
			//6. 응답처리 DML:int리턴, DQL:ResultSet리턴->자바객체로 전환
			result = pstmt.executeUpdate();
			
			//7. 트랜잭션처리(DML)
			if(result > 0)
				conn.commit();
			else 
				conn.rollback();

		} catch (ClassNotFoundException e) {
			//ojdbc6.jar 프로젝트 연동실패!
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//8. 자원반납(생성의 역순)
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public List<Member> selectAll() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member order by enroll_date desc";
		List<Member> list = null;
		
		try {
			//1. 드라이버클래스 등록(최초1회)
			Class.forName(driverClass);
			//2. Connection객체 생성(url, user, password) 
			//3. 자동커밋여부 설정 : true(기본값)/false -> app에서 직접 트랜잭션 제어
			conn = DriverManager.getConnection(url, user, password);
			//4. PreparedStatement객체생성(미완성쿼리) 및 값대입
			pstmt = conn.prepareStatement(sql); 
			//5. Statement객체 실행. DB에 쿼리 요청
			rset = pstmt.executeQuery();
			//6. 응답처리 DML:int리턴, DQL:ResultSet리턴->자바객체로 전환
			//다음행 존재여부리턴. 커서가 다음행에 접근.
			list = new ArrayList<>();
			while(rset.next()) {
				//컬럼명은 대소문자를 구분하지 않는다.
				String memberId = rset.getString("member_id");
				String password = rset.getString("password");
				String memberName = rset.getString("member_name");
				String gender = rset.getString("gender");
				int age = rset.getInt("age");
				String email = rset.getString("email");
				String phone = rset.getString("phone");
				String address = rset.getString("address");
				String hobby = rset.getString("hobby");
				Date enrollDate = rset.getDate("enroll_date");
				
				Member member = new Member(memberId, password, memberName, gender, age, email, phone, address, hobby, enrollDate);
				list.add(member);
			}
			//7. 트랜잭션처리(DML)
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//8. 자원반납(생성의 역순)
			try {
				if(rset != null)
					rset.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public Member selectOne(String memberId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "select * from member where member_id = ?";
		Member member = null;
		
		try {
			//1. 드라이버클래스 등록(최초1회)
			Class.forName(driverClass);
			//2. Connection객체 생성(url, user, password) 
			//3. 자동커밋여부 설정 : true(기본값)/false -> app에서 직접 트랜잭션 제어
			conn = DriverManager.getConnection(url, user, password);
			//4. PreparedStatement객체생성(미완성쿼리) 및 값대입
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, memberId);//select * from member where member_id = 'honggd'
			//5. Statement객체 실행. DB에 쿼리 요청
			rset = pstmt.executeQuery();
			//6. 응답처리 DML:int리턴, DQL:ResultSet리턴->자바객체로 전환
			//다음행 존재여부리턴. 커서가 다음행에 접근.
			while(rset.next()) {
				//컬럼명은 대소문자를 구분하지 않는다.
				memberId = rset.getString("member_id");
				String password = rset.getString("password");
				String memberName = rset.getString("member_name");
				String gender = rset.getString("gender");
				int age = rset.getInt("age");
				String email = rset.getString("email");
				String phone = rset.getString("phone");
				String address = rset.getString("address");
				String hobby = rset.getString("hobby");
				Date enrollDate = rset.getDate("enroll_date");
				
				member = new Member(memberId, password, memberName, gender, age, email, phone, address, hobby, enrollDate);
			}
			//7. 트랜잭션처리(DML)
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//8. 자원반납(생성의 역순)
			try {
				if(rset != null)
					rset.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return member;
	}
	
	public List<Member> searchMemberName(String memberName) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "select * from member where member_name = '" + memberName +"'";
		ResultSet rset = null;
		List<Member> list = new ArrayList<>();
		
		try {
			// 2. db connection객체 생성 : dbserver url, user, password
			conn = DriverManager.getConnection(url, user, password);
			// 3. 쿼리문 생성 및 Statement객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql);
			//DQL select문인 경우에는 executeQuery()호출 -> ResultSet
			rset = pstmt.executeQuery();
			
			// 4.1 select문인 경우 결과집합을 자바객체(list)에 옮겨담기
			while(rset.next()) {
				Member member = new Member();
				member.setMemberId(rset.getString("member_id"));
				member.setPassword(rset.getString("password"));
				member.setMemberName(rset.getString("member_name"));
				member.setGender(rset.getString("gender"));
				member.setAge(rset.getInt("age"));
				member.setEmail(rset.getString("email"));
				member.setPhone(rset.getString("phone"));
				member.setAddress(rset.getString("address"));
				member.setHobby(rset.getString("hobby"));
				member.setEnrollDate(rset.getDate("enroll_date"));
				
				list.add(member);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				//* 6. 자원 반납
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}
	public int updateMember(Member member) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update member set "
				   + "password = ?, "
				   + "member_name = ?, "
				   + "gender = ?, "
				   + "age = ?, "
				   + "email = ?, "
				   + "phone = ?, "
				   + "address = ?, "
				   + "hobby = ? "
				   + "where member_id = ?";
		int result = 0;
		
		try {
			//2. db connection객체 생성 : dbserver url, user, password
			conn = DriverManager.getConnection(url, user, password);
			//자동커밋 사용안함
			conn.setAutoCommit(false);
			
			//3. 쿼리문 생성 및 Statement객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getPassword());
			pstmt.setString(2, member.getMemberName());
			pstmt.setString(3, member.getGender());
			pstmt.setInt(4, member.getAge());
			pstmt.setString(5, member.getEmail());
			pstmt.setString(6, member.getPhone());
			pstmt.setString(7, member.getAddress());
			pstmt.setString(8, member.getHobby());
			pstmt.setString(9, member.getMemberId());
			//4. 쿼리전송(실행) - 결과값
			result = pstmt.executeUpdate();
			
			//5. 트랜잭션처리(commit, rollback)
			if(result > 0) conn.commit();
			else conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//6. 자원반납
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	public int deleteMember(String memberId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete from member where member_id = '" + memberId + "'";
		int result = 0;
		
		try {
			//2. DB connection객체 생성 : dbserver url, user, password
			conn = DriverManager.getConnection(url,user,password);
			//3. 쿼리문 생성 및 Statement객체(PreparedStatement) 생성
			pstmt = conn.prepareStatement(sql);
			//4. 쿼리전송(실행) - 결과값 
			result = pstmt.executeUpdate();
			
			if(result > 0) conn.commit();
	         else conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

}
