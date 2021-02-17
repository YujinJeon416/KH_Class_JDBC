package member.controller;

import java.util.List;

import member.model.service.MemberService;
import member.model.vo.Member;

public class MemberController {

	private MemberService memberService = new MemberService();

	public List<Member> selectAll() {
		return memberService.selectAll();
	}
	
	public int insertMember(Member m) {
		return memberService.insertMember(m);
	}

	public Member selectOne(String memberId) {
		return memberService.selectOneMember(memberId); 
	}

	public int deleteMember(String memberId) {
		return memberService.deleteMember(memberId);
	}
	
	public int updateMember(Member m) {
		return memberService.updateMember(m);
	}
	
	public List<Member> selectByName(String memberName) {
		return memberService.selectByName(memberName);
	}
		
}
