package product.view;

import static product.model.service.ProductService.PRODUCT_INPUT;
import static product.model.service.ProductService.PRODUCT_OUTPUT;

import java.util.List;
import java.util.Scanner;

import product.controller.ProductController;
import product.model.vo.Product;
import product.model.vo.ProductIO;

public class ProductMenu {
	private Scanner sc = new Scanner(System.in);
	private ProductController productController = new ProductController();
	
	
	public void mainMenu() {
		String menu = "\n***** 상품재고관리프로그램 *****\n"
					+ "1. 전체상품조회\n"
					+ "2. 상품아이디검색\n"
					+ "3. 상품명검색\n"
					+ "4. 상품추가\n"
					+ "5. 상품정보변경\n"
					+ "6. 상품삭제\n"
					+ "7. 상품입/출고메뉴\n"
					+ "9. 프로그램종료\n"
					+ "메뉴선택 : ";
		String choice = null;
		
		while(true){
			//메뉴출력
			System.out.print(menu);
			//사용자메뉴선택
			choice = sc.next();
			Product product = null;
			
			switch (choice) {
			case "1":
				productController.selectProductList();
				break;
			case "2":
				productController.selectOne(inputProductId());
				break;
			case "3":
				productController.selectByPName(inputPName());
				break;
			case "4":
				product = inputProduct();
				productController.insertProduct(product);
				break;
			case "5":
				product = updateProduct();
				productController.updateProduct(product);
				break;
			case "6":
				productController.deleteProduct(inputProductId());
				break;
			case "7":
				ioMenu();
				break;
			case "9":
				System.out.print("정말로 끝내시겠습니까?(y/n) : ");
				if('y'==sc.next().toLowerCase().charAt(0)) return;
				break;
			default:
				System.out.println("번호를 잘못 입력하셨습니다.");
			}
		}
	}
	
	private Product inputProduct() {
		System.out.println("새 상품정보를 입력하세요 :");
		System.out.print("상품아이디 : ");
		String productId = sc.next();
		sc.nextLine();
		System.out.print("상품명 : ");
		String productName = sc.nextLine();
		System.out.print("가격 : ");
		int price = sc.nextInt();
		sc.nextLine();
		System.out.print("상품설명 : ");
		String description = sc.nextLine();
		System.out.print("재고량 : ");
		int stock = sc.nextInt();
		
		return new Product(productId, productName, price, description, stock);
	}
	
	private Product updateProduct() {
		System.out.println(">>> 상품명, 가격, 재고량, 상품설명을 변경가능!");
		System.out.print("수정할 상품명 입력 : ");
		String productId = sc.next(); 
		sc.nextLine();
		System.out.print("상품명 : ");
		String productName = sc.nextLine();
		System.out.print("가격 : ");
		int price = sc.nextInt();
		System.out.print("재고량 : ");
		int stock = sc.nextInt();
		sc.nextLine();
		System.out.print("상품설명 : ");
		String description = sc.nextLine();
		return new Product(productId, productName, price, description, stock);
	}
	
	private String inputPName() {
		System.out.print("조회할 상품명 입력 : ");
		return sc.next();
	}

	private void ioMenu(){
		String menu = "\n***** 상품입출고메뉴*****\n"
					+ "1. 전체입출고내역조회\n"
					+ "2. 상품입고\n"
					+ "3. 상품출고\n"
					+ "9. 메인메뉴로 돌아가기\n"
					+ "메뉴선택 : ";
		String choice = null;
		while(true){
			//io메뉴출력
			System.out.print(menu);
			//사용자메뉴선택
			choice = sc.next();
			
			String productId = null;
			int amount = 0;
			ProductIO pio = null;
			
			switch(choice){
			case "1":
				productController.selectProductIOList();
				break;
			case "2":
				productId = inputProductId();
				amount = inputAmount(PRODUCT_INPUT);//입출고 여부 전달
				pio = new ProductIO();
				pio.setProductId(productId);
				pio.setAmount(amount);
				pio.setStatus(PRODUCT_INPUT);
				productController.insertProductIO(pio);
				break;
			case "3":
				//사용자입력
				productId = inputProductId();
				amount = inputAmount(PRODUCT_OUTPUT);
				//ProductIO객체생성
				pio = new ProductIO();
				pio.setProductId(productId);
				pio.setAmount(amount);
				pio.setStatus(PRODUCT_OUTPUT);
				productController.insertProductIO(pio);
				break;
			case "9":return;
			default:
				System.out.println("번호를 잘못 입력하셨습니다.");
			}
		}
	}
	
	/**
	 * 상품아이디입력
	 * @return
	 */
	private String inputProductId() {
		System.out.print("상품아이디입력 : ");
		return sc.next();
	}

	/**
	 * 입출고 수량입력
	 * @param ioFlag 
	 * @return
	 */
	private int inputAmount(String io_status) {
		System.out.print(io_status.equals(PRODUCT_INPUT)?"입고 수량 입력 : ":"출고 수량 입력 :");
		return sc.nextInt();
	}

	/**
	 * 처리오류메세지 출력메소드
	 * @param message
	 */
	public void displayError(String message) {
		System.err.println("처리오류: "+message);
	}

	/**
	 * 처리성공메세지 출력메소드
	 * @param message
	 */
	public void displaySuccess(String message) {
		System.out.println("처리성공 : "+message);
	}

	public void displayNoData() {
		System.out.println("조회된 데이터가 없습니다.");
		
	}

	public void displayProductList(List<Product> list) {
		System.out.println("\n조회된  상품정보는 다음과 같습니다.");
		System.out.println("상품아이디\t상품명\t가격\t제품설명\t재고량");
		System.out.println("----------------------------------------------------------------------");
		for (Product p : list) {
			System.out.println(p);			
		}
	}

	public void displayProductIOList(List<ProductIO> list) {
		System.out.println("\n조회된  입출고내역정보는 다음과 같습니다.");
		System.out.println("번호\t상품아이디\t날짜\t수량\t입/출고");
		System.out.println("----------------------------------------------------------------------");
		for (ProductIO p : list) {
			System.out.println(p);			
		}
	}

	public void displayProduct(Product p) {
		System.out.println("\n조회된  상품정보는 다음과 같습니다.");
		System.out.println("상품아이디\t상품명\t가격\t제품설명\t재고량");
		System.out.println("----------------------------------------------------------------------");
		System.out.println(p);				
	}

}
