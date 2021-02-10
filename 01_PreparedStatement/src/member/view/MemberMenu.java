package member.view;

import java.util.List;
import java.util.Scanner;

import member.controller.MemberController;
import member.model.vo.Member;

public class MemberMenu {
	
	private MemberController memberController = new MemberController();
	private Scanner sc = new Scanner(System.in);
	
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
		
		while(true) {
			System.out.print(menu);
			int choice = sc.nextInt();
			Member member = null;
			int result = 0;
			String msg = null;
			List<Member> list = null;
			String memberId = null;
					
			String memberName;
			switch(choice) {
				case 1: 
					list = memberController.selectAll();
					displayMemberList(list);
					break;
				case 2: 
					memberId = inputMemberId();
					member = memberController.selectOne(memberId);
					displayMember(member);
					break;
				case 3: //회원 이름 검색
					//사용자 입력값
					sc.nextLine();
					System.out.print("이름을 입력해주세요 : ");
					memberName = sc.nextLine();
					//controller요청
					list = memberController.searchMemberName(memberName);
					displayMemberList(list);break;
				case 4: 
					//1.신규회원정보 입력 -> Member객체
					member = inputMember();
					System.out.println(">>> 신규회원 확인 : " + member);
					//2.controller에 회원가입 요청(메소드호출) -> int리턴(처리된 행의 개수)
					result = memberController.insertMember(member);
					//3.int에 따른 분기처리
					msg = result > 0 ? "회원 가입 성공!" : "회원 가입 실패!";
					displayMsg(msg);
					break;
				case 5: //1.사용자 입력값 -> member객체 생성
					member = updateMember();
					//2.controller
					result = memberController.updateMember(member);
					displayMsg(result == 1 ? "회원정보 수정 성공!" : "회원정보 수정 실패!");
					break;
				case 6: //1.사용자 입력값 -> member객체 생성
					sc.nextLine();
					System.out.print("Id를 입력해주세요 : ");
					memberId = sc.nextLine();
					//2.controller에 delete요청
					result = memberController.deleteMember(memberId);
					displayMsg(result == 1 ? "회원탈퇴 성공!" : "회원탈퇴 실패!");break;
				case 0: 
					System.out.print("정말로 끝내시겠습니까?(y/n) : ");
					if(sc.next().charAt(0) == 'y')
						return;//현재메소드(mainMenu)를 호출한 곳
					break;
				default: 
					System.out.println("잘못 입력하셨습니다.");
			}	
		}
	}







	private Member updateMember() {
		System.out.println("변경할 회원정보를 입력하세요.");
		System.out.println("-------------------------------");
		System.out.print("아이디 : ");
		String memberId = sc.next();
		System.out.print("비밀번호 : ");
		String password = sc.next();
		System.out.print("이름 : ");
		String memberName = sc.next();
		System.out.print("나이 : ");
		int age = sc.nextInt();
		System.out.print("성별(M/F) : ");
		String gender = sc.next().toUpperCase(); //소문자로 입력해도 대문자로 바꾸어 줌
		System.out.print("이메일 : ");
		String email = sc.next();
		System.out.print("전화번호(-빼고 입력) : ");
		String phone = sc.next();
		sc.nextLine(); //개행문자 날리기용
//      next 다음에 nextLine을 쓰면 사이에 개행문자 날리기용 nextLine이 필요
		System.out.print("주소 : ");
		String address = sc.nextLine();
		System.out.print("취미(,으로 나열) : ");
		String hobby = sc.nextLine();
		return new Member(memberId, password, memberName, gender, 
						  age, email, phone, address, hobby, null);
	}







	/**
	 * DB에서 조회한 1명의 회원 출력
	 * @param member
	 */
	private void displayMember(Member member) {
		if(member == null)
			System.out.println(">>>> 조회된 회원이 없습니다.");
		else {
			System.out.println("****************************************************************");
			System.out.println(member);
			System.out.println("****************************************************************");
		}
	}

	/**
	 * 조회할 회원아이디 입력
	 * @return
	 */
	private String inputMemberId() {
		System.out.print("조회할 아이디 입력 : ");
		return sc.next();
	}

	/**
	 * DB에서 조회된 회원객체 n개를 출력
	 * @param list
	 */
	private void displayMemberList(List<Member> list) {
		if(list == null || list.isEmpty()) {
			System.out.println(">>>> 조회된 행이 없습니다.");	
		}
		else {
			System.out.println("*********************************************************");
			for(Member m : list) {
				System.out.println(m);
			}
			System.out.println("*********************************************************");
		}
	}

	/**
	 * DML처리결과 통보용 
	 * @param msg
	 */
	private void displayMsg(String msg) {
		System.out.println(">>> 처리결과 : " + msg);
	}

	/**
	 * 신규회원 정보 입력
	 * @return
	 */
	private Member inputMember() {
		System.out.println("새로운 회원정보를 입력하세요.");
		Member member = new Member();
		System.out.print("아이디 : ");
		member.setMemberId(sc.next());
		System.out.print("이름 : ");
		member.setMemberName(sc.next());
		System.out.print("비밀번호 : ");
		member.setPassword(sc.next());
		System.out.print("나이 : ");
		member.setAge(sc.nextInt());
		System.out.print("성별(M/F) : ");//m, f
		member.setGender(String.valueOf(sc.next().toUpperCase().charAt(0)));
		System.out.print("이메일: ");
		member.setEmail(sc.next());
		System.out.print("전화번호(-빼고 입력) : ");
		member.setPhone(sc.next());
		sc.nextLine();//버퍼에 남은 개행문자 날리기용 (next계열 - nextLine)
		System.out.print("주소 : ");
		member.setAddress(sc.nextLine());
		System.out.print("취미(,로 나열할것) : ");
		member.setHobby(sc.nextLine());
		return member;
	}

}








