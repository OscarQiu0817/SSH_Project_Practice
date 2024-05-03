package member.model;

import java.util.List;

public interface MemberDAO_interface {
	
	public void insert(MemberVO memberVO);
	public void update(MemberVO memberVO);
	public void delete(Integer memno);
	
	public MemberVO findByPrimaryKey(Integer memno);
	public MemberVO login(String name, String Password);

	public List<MemberVO> getAll();
}

