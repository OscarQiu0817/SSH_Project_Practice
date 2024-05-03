package member.model;

import java.util.List;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class MemberHibDAO implements MemberDAO_interface{
	
	public static final String GET_ALL = "from MemberVO order by mem_no";
	
	private HibernateTemplate hibernateTemplate;
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) { 
        this.hibernateTemplate = hibernateTemplate;
    }
    
	@Override
	public void insert(MemberVO memberVO) {
		hibernateTemplate.saveOrUpdate(memberVO);
	}

	@Override
	public void update(MemberVO memberVO) {
		hibernateTemplate.update(memberVO);
	}

	@Override
	public void delete(Integer memno) {
		MemberVO memVO = new MemberVO();
		memVO.setMem_no(memno);
		hibernateTemplate.delete(memVO);
	}

	@Override
	public MemberVO findByPrimaryKey(Integer memno) {
		MemberVO memberVO = null;
		memberVO = (MemberVO) hibernateTemplate.get(MemberVO.class, memno);
		return memberVO;
	}

	@Override
	public MemberVO login(String name, String password) {
		MemberVO memberVO = null;
		String hql = "from MemberVO where mem_name = ? and mem_password = ?";
		List<MemberVO> list = hibernateTemplate.find(hql,new String[] {name,password});
			
		if(list != null) {
			if(list.size()!=0) {
				memberVO = list.get(0);
			}
		}
//		System.out.println(memberVO == null);
		
		return memberVO;
	}

	@Override
	public List<MemberVO> getAll() {
		List<MemberVO> list = null;
		list = hibernateTemplate.find(GET_ALL);
		return list;
	}

}
