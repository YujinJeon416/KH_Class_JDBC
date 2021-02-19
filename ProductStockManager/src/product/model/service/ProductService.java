package product.model.service;

import static common.JDBCTemplate.close;
import static common.JDBCTemplate.commit;
import static common.JDBCTemplate.getConnection;
import static common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.util.List;

import product.model.dao.ProductDao;
import product.model.exception.InsufficientOutputAmountException;
import product.model.exception.ProductException;
import product.model.vo.Product;
import product.model.vo.ProductIO;

/**
 * 
 * 
 *
 */
public class ProductService {
	//application의 필요한 상수는 주로 업무로직 담당인 Service단에 작성한다.
	public static final String PRODUCT_INPUT = "I";		//입고
	public static final String PRODUCT_OUTPUT = "O";	//출고
	
	private ProductDao productDao = new ProductDao();
	
	public int insertProductIO(ProductIO pio) {
		Connection conn = getConnection();
		//출고시, 출고량과 재고량을 비교한다.
		if("O".equals(pio.getStatus())) {
			Product p = productDao.selectOne(conn, pio.getProductId());
			//재고량이 출고량보다 적다면, 예외를 던짐
			if(p.getStock()<pio.getAmount())
				throw new InsufficientOutputAmountException("재고량 : " + p.getStock() + ", 출고량 : " + pio.getAmount());
		}
		int result = productDao.insertProduct_IO(conn, pio);
		if(result>0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}

	public List<Product> selectProductList() {
		Connection conn = getConnection();
		List<Product> list = productDao.selectProductList(conn);
		close(conn);
		return list;
	}

	public List<ProductIO> selectProductIOList() {
		Connection conn = getConnection();
		List<ProductIO> list = productDao.selectProductIOList(conn);
		close(conn);
		return list;
	}

	public Product selectOne(String productId){
		Connection conn = getConnection();
		Product p = productDao.selectOne(conn, productId);
		close(conn);
		return p;
	}

	public List<Product> selectByPName(String pName){
		Connection conn = getConnection();
		List<Product> list = productDao.selectByPName(conn,pName);
		close(conn);
		return list;
	}

	public int insertProduct(Product p) {
		Connection conn = getConnection();
		int result = productDao.insertProduct(conn, p);
		if(result>0) commit(conn);
		else rollback(conn);
		return result;
	}

	public int updateProduct(Product p) {
		Connection conn = getConnection();
		int result = productDao.updateProduct(conn, p);
		if(result>0) commit(conn);
		else rollback(conn);
		return result;
	}

	public int deleteProduct(String productId){
		Connection conn = getConnection();
		int result = productDao.deleteProduct(conn, productId);
		if(result>0) commit(conn);
		else rollback(conn);
		return result;
	}
}
