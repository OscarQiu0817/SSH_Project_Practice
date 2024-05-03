package subsys.model;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class SubSysHibDAO implements SubSysDAO_interface{

	public static final String GET_ALL = "from SubSysVO order by SubSys_no";
	
	private HibernateTemplate hibernateTemplate;
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) { 
        this.hibernateTemplate = hibernateTemplate;
    }
	
	@Override
	public SubSysVO insert(SubSysVO subsysVO) {
		hibernateTemplate.saveOrUpdate(subsysVO);
		return subsysVO;
	}

	@Override
	public void update(SubSysVO subsysVO) {
		hibernateTemplate.update(subsysVO);
	}

	@Override
	public void delete(Integer SubSys_no) {
		SubSysVO sub = new SubSysVO();
		sub.setSubSys_no(SubSys_no);
		hibernateTemplate.delete(sub);
	}

	@Override
	public SubSysVO findByPrimaryKey(Integer SubSys_no) {
		SubSysVO sub = null;
		sub = hibernateTemplate.get(SubSysVO.class, SubSys_no);
		return sub;
	}
	
	@Override
	public SubSysVO findByTwName(String twName) {
		SubSysVO sub = null;
		String sql = "from SubSysVO where SubSys_twName = ?";
		List<SubSysVO> list = hibernateTemplate.find(sql, twName);
		
		if(list != null) {
			if(list.size() != 0) {
				sub = list.get(0);
			}
		}
		
		return sub;
	}

	@Override
	public List<SubSysVO> getAll() {

		List<SubSysVO> list = null;
		list = hibernateTemplate.find(GET_ALL);
		return list;
	}



}
