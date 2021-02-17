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
	
	public List<Member> selectByName(String memberName) {
		List<Member> list = new ArrayList<Member>();
		
		//사용후 반납해야할(close)자원들은 try~catch문 바깥에서 선언해야 한다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String query = "select * from member where member_name like ?";
		try {
			//2. 등록된 클래스를 이용해서 db연결함. 
			//통행권 Connection객체 생성
			conn = DriverManager.getConnection(url, user, password);
			
			//3. 쿼리문을 실행할 statement객체 생성
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + memberName + "%");
			
			//4. 쿼리문 전송, 실행결과 받기
			rset = pstmt.executeQuery();
			
			//5. 받은 결과값들을 객체에 옮겨 저장하기
			//rset가 null인경우는 모두 Exception처리된다.
			
			while(rset.next()){
				//커서가 가리키는 다음행에서 컬럼 정보를 읽어온다.
				String memberId = rset.getString("member_id");
				String password = rset.getString("password");
				memberName = rset.getString("member_name");
				String gender = rset.getString("gender");
				int age = rset.getInt("age");
				String email = rset.getString("email");
				String phone = rset.getString("phone");
				String address = rset.getString("address");
				String hobby = rset.getString("hobby");
				Date enrollDate = rset.getDate("enroll_date");
				
				Member m = new Member(memberId, password, memberName, 
									  gender, age, email, phone, 
									  address, hobby, enrollDate);
				list.add(m);
			}
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			//자원반납 순서는 생성의 역순이다.
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	
	public int updateMember(Member m) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		//키워드, 값 사이의 공란주의
		String query = "update member set "
					 + " password=?"
					 + ",email=?"
					 + ",phone=?"
					 + ",address=?"
					 + " where member_id=?";
		
		try {
			//2. 드라이버매니져로부터 connection객체 얻기
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			//3. 미완성쿼리 생성 및 갑대입, 실행
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, m.getPassword());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getMemberId());
			result = pstmt.executeUpdate();
			
			//커밋처리
			if(result>0) conn.commit();
			else conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public int deleteMember(String memberId) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String query = "delete from member where member_id=?";
		try {
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, memberId);
			result = pstmt.executeUpdate();
			
			//커밋처리
			if(result>0) conn.commit();
			else conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}



}
