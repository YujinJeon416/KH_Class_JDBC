package product.model.dao;

import static common.JDBCTemplate.close;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import product.model.exception.ProductException;
import product.model.vo.Product;
import product.model.vo.ProductIO;

public class ProductDao {
	private Properties prop = new Properties();
	
	public ProductDao(){
		try {
			prop.load(new FileReader("resources/query.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int insertProduct_IO(Connection conn, ProductIO pio) throws ProductException {
		int result = 0;
		PreparedStatement pstmt = null;
		
		String query = prop.getProperty("insertProduct_IO");
		try {
			
			//1. 미완성쿼리문을 가지고 PreparedStatement객체생성
			pstmt = conn.prepareStatement(query);
			//객체생성후 ? 부분 값대입.
			pstmt.setString(1, pio.getProductId());
			pstmt.setInt(2, pio.getAmount());
			pstmt.setString(3, pio.getStatus());
			
			//2. 쿼리문 실행, 실행결과 받기
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			//사용자 정의 예외 던짐.
			throw new ProductException("insertProduct_IO 입고메소드  요류 : "+e.getMessage(), e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	
	public List<Product> selectProductList (Connection conn) throws ProductException {
		ArrayList<Product> list = null;
		
		//사용후 반납해야할(close)자원들은 try~catch문 바깥에서 선언해야 한다.
		Statement stmt = null;
		ResultSet rset = null;
		
		String query = prop.getProperty("selectProductList");
		try {
			//1. 쿼리문을 실행할 statement객체 생성
			stmt = conn.createStatement();
			
			//2. 쿼리문 전송, 실행결과 받기
			rset = stmt.executeQuery(query);
			
			//3. 받은 결과값들을 객체에 옮겨 저장하기
			list = new ArrayList<Product>();
			
			while(rset.next()){
				Product p = new Product();
				//컬럼명은 대소문자 구분이 없다.
				p.setProductId(rset.getString("product_id"));
				p.setProductName(rset.getString("product_name"));
				p.setPrice(rset.getInt("price"));
				p.setDescription(rset.getString("description"));
				p.setStock(rset.getInt("stock"));
				list.add(p);
			}
			
		} catch (Exception e){
			//사용자 정의 예외 던짐.
			throw new ProductException("selectProductList 메소드 에러! : "+e.getMessage(), e);
			
		} finally {
			close(rset);
			close(stmt);
		}
		
		return list;
	}
	
	public List<ProductIO> selectProductIOList (Connection conn) throws ProductException {
		ArrayList<ProductIO> list = null;
		
		//사용후 반납해야할(close)자원들은 try~catch문 바깥에서 선언해야 한다.
		Statement stmt = null;
		ResultSet rset = null;
		
		String query = prop.getProperty("selectProductIOList");
		try {
			//1. 쿼리문을 실행할 statement객체 생성
			stmt = conn.createStatement();
			
			//2. 쿼리문 전송, 실행결과 받기
			rset = stmt.executeQuery(query);
			
			//3. 받은 결과값들을 객체에 옮겨 저장하기
			list = new ArrayList<ProductIO>();
			
			while(rset.next()){
				ProductIO pio = new ProductIO();
				//컬럼명은 대소문자 구분이 없다.
				pio.setIoNo(rset.getInt("io_no"));
				pio.setProductId(rset.getString("product_id"));
				pio.setAmount(rset.getInt("amount"));
				pio.setIoDate(rset.getDate("iodate"));
				pio.setStatus(rset.getString("status"));
				list.add(pio);
			}
			
		} catch (Exception e){
			//사용자 정의 예외 던짐.
			throw new ProductException("selectProductIOList 메소드 에러! : "+e.getMessage(), e);
			
		} finally {
			close(rset);
			close(stmt);
		}
		
		return list;
	}
	
	
	public Product selectOne(Connection conn, String productId) throws ProductException {
		Product p = null;
		
		//사용후 반납해야할(close)자원들은 try~catch문 바깥에서 선언해야 한다.
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String query = prop.getProperty("selectOne");
		try {
			//1. 쿼리문을 실행할 statement객체 생성
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, productId);
			//2. 쿼리문 전송, 실행결과 받기
			rset = pstmt.executeQuery();
			
			//3. 받은 결과값들을 객체에 옮겨 저장하기
			while(rset.next()){
				p = new Product();
				//컬럼명은 대소문자 구분이 없다.
				p.setProductId(rset.getString("product_id"));
				p.setProductName(rset.getString("product_name"));
				p.setPrice(rset.getInt("price"));
				p.setDescription(rset.getString("description"));
				p.setStock(rset.getInt("stock"));
			}
			
		} catch (Exception e){
			//사용자 정의 예외 던짐.
			throw new ProductException("selectOne 메소드 에러! : "+e.getMessage(), e);
			
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return p;
	}
	public List<Product> selectByPName(Connection conn, String pName) throws ProductException {
		ArrayList<Product> list = null;
		
		//사용후 반납해야할(close)자원들은 try~catch문 바깥에서 선언해야 한다.
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String query = prop.getProperty("selectByPName");
		try {
			//1. 쿼리문을 실행할 statement객체 생성
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%"+pName+"%");
			
			//2. 쿼리문 전송, 실행결과 받기
			rset = pstmt.executeQuery();
			
			//3. 받은 결과값들을 객체에 옮겨 저장하기
			list = new ArrayList<>();
			
			while(rset.next()){
				Product p = new Product();
				//컬럼명은 대소문자 구분이 없다.
				p.setProductId(rset.getString("product_id"));
				p.setProductName(rset.getString("product_name"));
				p.setPrice(rset.getInt("price"));
				p.setDescription(rset.getString("description"));
				p.setStock(rset.getInt("stock"));
				list.add(p);
			}
			
		} catch (Exception e){
			//사용자 정의 예외 던짐.
			throw new ProductException("selectByPName 메소드 에러! : "+e.getMessage(), e);
			
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}
	public int insertProduct(Connection conn, Product p) throws ProductException {
		int result = 0;
		PreparedStatement pstmt = null;
		String query = prop.getProperty("insertProduct");
		
		try {
			
			//1. 미완성쿼리문을 가지고 PreparedStatement객체생성
			pstmt = conn.prepareStatement(query);
			//객체생성후 ? 부분 값대입.
			pstmt.setString(1, p.getProductId());
			pstmt.setString(2, p.getProductName());
			pstmt.setInt(3, p.getPrice());
			pstmt.setInt(4, p.getStock());
			pstmt.setString(5, p.getDescription());

			//2. 쿼리문 실행, 실행결과 받기
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			//사용자 정의 예외 던짐.
			throw new ProductException("insertProduct 메소드 에러! : "+e.getMessage(), e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	public int updateProduct(Connection conn, Product p) throws ProductException {
		int result = 0;
		PreparedStatement pstmt = null;
		String query = prop.getProperty("updateProduct");
		
		try {
			
			//1. 미완성쿼리문을 가지고 PreparedStatement객체생성
			pstmt = conn.prepareStatement(query);
			//객체생성후 ? 부분 값대입.
			pstmt.setString(1, p.getProductName());
			pstmt.setInt(2, p.getPrice());
			pstmt.setString(3, p.getDescription());
			pstmt.setInt(4, p.getStock());
			pstmt.setString(5, p.getProductId());

			//2. 쿼리문 실행, 실행결과 받기
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			//사용자 정의 예외 던짐.
			throw new ProductException("updateProduct 메소드 에러! : "+e.getMessage(), e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	public int deleteProduct(Connection conn, String productId) throws ProductException {
		int result = 0;
		PreparedStatement pstmt = null;
		String query = prop.getProperty("deleteProduct");
		
		try {
			
			//1. 미완성쿼리문을 가지고 PreparedStatement객체생성
			pstmt = conn.prepareStatement(query);
			//객체생성후 ? 부분 값대입.
			pstmt.setString(1, productId);

			//2. 쿼리문 실행, 실행결과 받기
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			//사용자 정의 예외 던짐.
			throw new ProductException("deleteProduct 메소드 에러! : "+e.getMessage(), e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	
}
