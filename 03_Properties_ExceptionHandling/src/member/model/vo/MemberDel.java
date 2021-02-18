package member.model.vo;

import java.io.Serializable;
import java.sql.Date;

public class MemberDel extends Member implements Serializable{
	/**
	 * 삭제회원용 필드 delDate 추가
	 */
	private Date delDate;
	
	public MemberDel() {}

	/**
	 * 탈퇴회원용 생성자 추가
	 * 
	 * @param memberId
	 * @param password
	 * @param memberName
	 * @param gender
	 * @param age
	 * @param email
	 * @param phone
	 * @param address
	 * @param hobby
	 * @param enrollDate
	 * @param delDate
	 */
	public MemberDel(String memberId, String password, String memberName, String gender, int age, String email,
			String phone, String address, String hobby, Date enrollDate, Date delDate) {
		super(memberId, password, memberName, gender, age, email, phone, address, hobby, enrollDate);		
		this.delDate = delDate;
	}
	
	public Date getDelDate() {
		return delDate;
	}
	public void setDelDate(Date delDate) {
		this.delDate = delDate;
	}
	

	@Override
	public String toString(){
		return super.toString()+"\t"+delDate;
	}
}
