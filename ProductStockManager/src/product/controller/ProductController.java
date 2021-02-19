package product.controller;

import java.util.List;

import product.model.exception.InsufficientOutputAmountException;
import product.model.exception.ProductException;
import product.model.service.ProductService;
import product.model.vo.Product;
import product.model.vo.ProductIO;
import product.view.ProductMenu;

public class ProductController {

	private ProductService productService = new ProductService();
	
	public void insertProductIO(ProductIO pio) {
		ProductMenu productMenu = new ProductMenu();
		String status = "I".equals(pio.getStatus())?"상품입고":"상품출고";
		
		try {
			int result = productService.insertProductIO(pio);
			if(result>0)
				productMenu.displaySuccess(status+"완료");
			else
				productMenu.displayError(status+"오류! 출고량보다 재고량이 적습니다. 다시 작성해주세요.");
		} catch (InsufficientOutputAmountException e) {
			e.printStackTrace();
			productMenu.displayError("출고량이 재고량 보다 많을 수 없습니다  - " + e.getMessage());
		} catch (ProductException e) {
			e.printStackTrace();
			productMenu.displayError(status+"오류! 관리자에게 문의하세요.");
		}
	}

	public void selectProductList() {
		ProductMenu productMenu = new ProductMenu();
		try {
			List<Product> list = productService.selectProductList();
			if(!list.isEmpty()){
				productMenu.displayProductList(list);
			}
			else{
				productMenu.displayNoData();
			}

		} catch (ProductException e) {
			e.printStackTrace();
			productMenu.displayError("상품 전체 조회 실패! 관리자에게 문의하세요.");
		}
	}
	
	public void selectProductIOList() {
		ProductMenu productMenu = new ProductMenu();
		try {
			List<ProductIO> list = productService.selectProductIOList();
			if(!list.isEmpty()){
				productMenu.displayProductIOList(list);
			}
			else{
				productMenu.displayNoData();
			}

		} catch (ProductException e) {
			e.printStackTrace();
			productMenu.displayError("입출고내역 전체 조회 실패! 관리자에게 문의하세요.");
		}
	}

	public void selectOne(String productId) {
		ProductMenu productMenu = new ProductMenu();
		try {
			
			Product p = productService.selectOne(productId);
			
			//리턴될 결과에 따라 성공/실패에 대한 뷰가 선택되어 구동됨.
			if(p !=null){
				productMenu.displayProduct(p);
			}
			else{
				productMenu.displayNoData();
			}
		} catch (ProductException e) {
			e.printStackTrace();
			productMenu.displayError("상품 조회 실패! 관리자에게 문의하세요.");
		}
	}

	public void selectByPName(String pName) {
		ProductMenu productMenu = new ProductMenu();
		try {
			List<Product> list = productService.selectByPName(pName);
			if(!list.isEmpty()){
				productMenu.displayProductList(list);
			}
			else{
				productMenu.displayNoData();
			}

		} catch (ProductException e) {
			e.printStackTrace();
			productMenu.displayError("상품명 조회 실패! 관리자에게 문의하세요.");
		}
	}

	public void insertProduct(Product p) {
		ProductMenu productMenu = new ProductMenu();
		try {
			if(productService.insertProduct(p) > 0)
				productMenu.displaySuccess("상품등록 성공!");
			
		} catch (ProductException e) {
			e.printStackTrace();
			productMenu.displayError("상품등록 실패! 관리자에게 문의하세요.");
		}
		
	}

	public void updateProduct(Product p) {
		ProductMenu productMenu = new ProductMenu();
		try {
			if(productService.updateProduct(p) > 0)
				productMenu.displaySuccess("상품 정보 수정 성공!");
			else 
				productMenu.displayNoData();
		} catch (ProductException e) {
			e.printStackTrace();
			productMenu.displayError("상품 정보 수정 실패! 관리자에게 문의하세요.");
		}
		
	}

	public void deleteProduct(String productId) {
		ProductMenu productMenu = new ProductMenu();
		try {
			if(productService.deleteProduct(productId) > 0)
				productMenu.displaySuccess("상품 정보 삭제 성공!");
			else 
				productMenu.displayNoData();
		} catch (ProductException e) {
			e.printStackTrace();
			productMenu.displayError("상품 정보 삭제 실패! 관리자에게 문의하세요.");
		}
	}

}
