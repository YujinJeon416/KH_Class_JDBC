package member.view;

import java.util.List;
import java.util.Scanner;

import member.controller.MemberController;
import member.model.vo.Member;


public class MemberMenu {
	
	private Scanner sc = new Scanner (System.in);
	private MemberController memberController = new MemberController();


	
	public void mainMenu() {
		String menu = "========== 회원 관리 프로그램 ==========\n"
					+ "1.회원 전체조회\n"
					+ "2.회원 아이디조회\n" 
					+ "3.회원 이름조회\n" 
					+ "4.회원 가입\n" 
					+ "5.회원 정보변경\n" 
					+ "6.회원 탈퇴\n" 
					+ "0.프로그램 끝내기\n"
					+ "----------------------------------\n"
					+ "선택 : "; 
		
		do {
			System.out.println(menu);
			String choice = sc.next();
			List<Member> list = null;
			int result = 0; 						// DML 처리용
			Member member = null;
			String memberId = null;
			int backToMain = -1000;					// 서브메뉴 뒤로가기에 사용
			
			switch(choice) {
			case "1" : 
				list = memberController.selectAll();			// 전체 조회 메소드
				displayMemberList1(list);						// 리스트 출력
				break;
			case "2" : 
				 //1. 사용자입력값(memberId) -컨트롤러 조회요청
	        	 //2. member객체 혹은 null 화면 출력
	        	 member = memberController.selectOneMember(inputMemberId());
	        	 displayMember(member);
	        	 break;
			case "3" :
				String memberName = inputMemberName();			// 이름 입력
				list = memberController.selectMemberbyName(memberName);	// 조회
				displayMemberList1(list);						// 리스트 출력
				break;
			case "4" :
				result = memberController.insertMember(inputMember());	// 멤버 추가 메소드
				displayMsg(result > 0 ? "회원가입 성공" : "회원가입 실패");
				break;
			case "5" :
				memberId = inputMemberId();
				member = memberController.selectOneMember(memberId);
				if(member == null) { 
					System.out.println("존재하지않은 회원입니다."); 
					break;
				}else {
					displayMember(member);
				}
				member = memberController.selectOneMember(updateMemberId());
				displayUpdate(member);
				break;
			case "6" :
				result = memberController.deleteMember(inputMemberId());	// 아이디 입력 후 삭제 메소드
				displayMsg(result > 0 ? "삭제 성공" : "삭제 실패");
				break;
			case "0" : 
				System.out.println(" 정말 끝내시겠습니까?(y/n): ");
				if(sc.next().charAt(0) == 'y')
					return;
				break;
			default : 
				System.out.println("잘못 입력하셨습니다. ");
			}
			
		} while (true);
		
		

}


/**
 * n행의 회원정보 출력
 * 
 */
	private void displayMemberList1(List<Member> list) {
		if(list != null && !list.isEmpty()) {
			System.out.println("==================================================================");
			for(int i = 0; i < list.size(); i++)
				System.out.println((i + 1) + " : " + list.get(i));
			System.out.println("==================================================================");
		}
		else {
			System.out.println(">>>조회된 회원 정보가 없습니다.");
		}
		
		
	}
	
	//private void displayUpdate(Member member) {
	  
	   private int displayUpdate(Member member) {
		
		   while (true) {
				String subMenu = "======================= 회원 정보 변경 메뉴 =======================\n"
						+ "1. 암호 변경\n"
						+ "2. 이메일 변경\n"
						+ "3. 전화번호 변경\n"
						+ "4. 주소 변경\n"
						+ "9. 메인메뉴 돌아가기\n"
						+ "=============================================================\n"
						+ "선택 : ";
		

				System.out.print(subMenu);
				int choice = sc.nextInt();
				int result =0;
				switch (choice) {
				case 1:
					System.out.print("새비밀번호 : ");
					String password =sc.next();
					result =memberController.newPassword(password,member);
					displayMsg(result > 0 ? "비밀번호 변경 성공!" : "비밀번호 변경 실패!");
					member = memberController.selectOneMember(member.getMemberId());
					displayMember(member);
					break;
				case 2:
					System.out.print("새 이메일 : ");
					String email =sc.next();
					result =memberController.newEmail(email,member);
					displayMsg(result > 0 ? "이메일 변경 성공!" : "이메일 변경 실패!");
					member = memberController.selectOneMember(member.getMemberId());
					displayMember(member);
					break;
				case 3:
					System.out.print("새 전화번호(-빼고 입력) : ");
					String phone =sc.next();
					result =memberController.newPhone(phone,member);
					displayMsg(result > 0 ? "전화번호 변경 성공!" : "전화번호 변경 실패!");
					member = memberController.selectOneMember(member.getMemberId());
					displayMember(member);
					break;
				case 4:
					System.out.print("새 주소 :");
					sc.nextLine();
					String address =sc.nextLine();
					result =memberController.newAddress(address,member);
					displayMsg(result > 0 ? "주소 변경 성공!" : "주소 변경 실패!");
					member = memberController.selectOneMember(member.getMemberId());
					displayMember(member);
					break;
				case 9:
					System.out.println("메인화면으로 돌아갑니다.");
					return 0;
				default:
					System.out.println("잘못입력하셨습니다");
				}
			}
		}



		private String updateMemberId() {
			System.out.print("수정당할 회원의 아이디 입력 : ");
			return sc.next();
		}



		private void displayMember(List<Member> list) {
			System.out.println("=======================================");
			//조회된 회원정보가 있을 때
			if(list != null && !list.isEmpty()) {
				System.out.println("memberId\tMemberName\tGender\tAge\tEmail\tPhone\tAddress\tHobby\tEnrollDate");
				System.out.println("--------------------------------------------------------------------------------");
				for(Member member : list)
					System.out.println(member);
			
			}
			//조회된 회원정보가 없을 때
			else {
				System.out.println("조회된 회원이 없습니다.");
			}
			System.out.println("=======================================");
		}



		private String inputMemberName() {
			System.out.print("조회할 이름 입력 : ");
			return sc.next();
		}



		private void displayMemberList(List<Member> list) {
			   System.out.println("=======================================");
			      
			   //조회된 회원정보가 있을 때
			   
			   if(list != null && !list.isEmpty()) {
				   System.out.println("MemberId\t"
			                        + "MemberName\t"
			                        + "Gender\t"
			                        + "Age\t"
			                        + "Email\t"
			                        + "Phone\t"
			                        + "Address\t"
			                        + "Hobby\t"
			                        + "Enroll Date");
			       System.out.println("-----------------------------------------------------------------------------------------------");
			       for(Member member : list)
			    	   System.out.println(member);
			    }
			      //조회된 회원정보가 없을 때
			    else {
			       System.out.println("조회된 회원이 없습니다.");
			    }
			   System.out.println("=======================================");
		}


		private void displayMember(Member member) {
			      if(member != null) {
			         System.out.println("---------------------------------------------------------------------");
			         System.out.println(member);
			         System.out.println("---------------------------------------------------------------------");
			      }
			      else
			         System.out.println("조회된 정보가 없습니다.");
		}


		private String inputMemberId() {
			   System.out.print("조회할 아이디 입력 : ");
			   return sc.next();
		}

		/**
	    * DML처리 후에 사용자에게 피드백을 주는 메소드
	    * @param msg
	    */
		private void displayMsg(String msg) {
		    System.out.println(msg);
		}

	    /**
	    * 회원가입 메소드
	    * 사용자 입력처리
	    * @return
	    */
	   
		private Member inputMember() {
			System.out.println("새로운 회원정보를 입력하세요.");
			System.out.println("-------------------------------");
			System.out.print("아이디 : ");
			String memberId = sc.next();
			System.out.println("비밀번호 : ");
			String password = sc.next();
			System.out.println("이름 : ");
			String memberName = sc.next();
			System.out.println("나이 : ");
			int age = sc.nextInt();
			System.out.println("성별(M/F) : ");
			String gender = sc.next().toUpperCase();
			System.out.println("이메일 : ");
			String email = sc.next();
			System.out.println("전화번호(-빼고 입력) : ");
			String phone = sc.next();
			sc.nextLine(); //개행문자 날리기용
			System.out.println("주소 : ");
			String address = sc.nextLine();
			System.out.println("취미(,으로 나열) : ");
			String hobby = sc.nextLine();
			return new Member(memberId, password, memberName, gender, 
	                          age, email, phone, address, hobby, null);
		}
	}
	
