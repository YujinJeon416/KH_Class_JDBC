package member.controller;

import java.util.List;

import member.model.exception.MemberException;
import member.model.service.MemberService;
import member.model.vo.Member;
import member.view.MemberMenu;

public class MemberController {

	private MemberService memberService = new MemberService();

	public List<Member> selectAll() {
		List<Member> list = null;
		try {
			list = memberService.selectAll();
		} catch(MemberException e) {
			//서버로깅
			//관리자 이메일 알림
			new MemberMenu().displayError(e.getMessage() + " : 관리자에게 문의하세요.");
		}
		return list;
	}
	
	public int insertMember(Member m) {
		int result = 0;
		try {
			result = memberService.insertMember(m);
		} catch (MemberException e) {
			e.printStackTrace();
			new MemberMenu().displayError(e.getMessage());
		}
		
		return result;
	}

	public Member selectOne(String memberId) {
		Member member = null; 
		try {
			member = memberService.selectOne(memberId);
		} catch (MemberException e) {
			e.printStackTrace();
			//사용자한테 오류 알림
			new MemberMenu().displayError(e.getMessage());
		}
		return member;
	}
	
	public int deleteMember(String memberId) {
		int result = 0;
		try {
			result = memberService.deleteMember(memberId);
		} catch (MemberException e) {
			e.printStackTrace();
			new MemberMenu().displayError(e.getMessage());
		}
		
		return result;
	}
	
	public int updateMember(Member m) {
		int result = 0;
		try {
			result = memberService.updateMember(m);
		} catch (MemberException e) {
			e.printStackTrace();
			new MemberMenu().displayError(e.getMessage());
		}
		
		return result;
	}
	
	public List<Member> selectByName(String memberName) {
		List<Member> list = null;
		try {
			list = memberService.selectByName(memberName);
		} catch (MemberException e) {
			e.printStackTrace();
			//사용자한테 오류 알림
			new MemberMenu().displayError(e.getMessage());
		}
		return list;
	}

	public List<Member> selectAllDeletedMember() {
		List<Member> list = null;
		try {
			list = memberService.selectAllDeletedMember();
		} catch (MemberException e) {
			e.printStackTrace();
			new MemberMenu().displayError(e.getMessage());
		}
		return list;
	}

		
}
