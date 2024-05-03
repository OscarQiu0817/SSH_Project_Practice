package member.model;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MemberService {
	private MemberDAO_interface dao;
	
	public MemberService() {
		ApplicationContext context = new ClassPathXmlApplicationContext("model-config.xml");
		dao =(MemberDAO_interface) context.getBean("memberDAO");
	}
	
	public void add(MemberVO memberVO) {
		dao.insert(memberVO);
	}
	
	public void update(MemberVO memberVO) {
		dao.update(memberVO);
	}
	
	public void delete(Integer memno) {
		dao.delete(memno);
	}
	
	public MemberVO findByPrimaryKey(Integer memno) {
		return dao.findByPrimaryKey(memno);
	}
	
	public MemberVO login(String name, String password) {
		return dao.login(name, password);
	}

	public List<MemberVO> getAll(){
		return dao.getAll();
	}
}
