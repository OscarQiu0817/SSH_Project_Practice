package plan.model;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class PlanHibDAO implements PlanDAO_interface{

	
	public static final String GET_ALL = "from PlanVO order by plan_no";
	public static final String GET_LAST = "from PlanVO order by plan_start_date desc";
	public static final String GET_ALL_ASC = "from PlanVO order by plan_start_date asc";
	
	private HibernateTemplate hibernateTemplate;
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) { 
        this.hibernateTemplate = hibernateTemplate;
    }
	
	
	@Override
	public PlanVO insert(PlanVO planVO) {
		 hibernateTemplate.saveOrUpdate(planVO);
		 System.out.println(planVO);
		 return planVO;
	}

	@Override
	public void update(PlanVO planVO) {
		hibernateTemplate.update(planVO);
	}

	@Override
	public void delete(Integer plan_no) {
		PlanVO planVO = new PlanVO();
		planVO.setPlan_no(plan_no);
		hibernateTemplate.delete(planVO);
	}

	@Override
	public PlanVO findByPrimaryKey(Integer plan_no) {
		PlanVO planVO = null;
		planVO = hibernateTemplate.get(PlanVO.class, plan_no);
		return planVO;
	}

	@Override
	public List<PlanVO> getAll() {
		List<PlanVO> list = null;
		list = hibernateTemplate.find(GET_ALL);
		return list;
	}
	
	public List<PlanVO> getAllDesc() {
		List<PlanVO> list = null;
		list = hibernateTemplate.find(GET_LAST);
//		System.out.println(list == null);
		
		return list;
	}
	
	public List<PlanVO> getAllAsc() {
		List<PlanVO> list = null;
		list = hibernateTemplate.find(GET_ALL_ASC);
		return list;
	}


	@Override
	public PlanVO getNewest() {
		List<PlanVO> list = null;
		list = hibernateTemplate.find(GET_LAST);
		PlanVO planVO = null;
		
		if(list != null) {
			if(list.size() != 0) {
				planVO = list.get(0);
				return planVO;
			}
		}
		return planVO;
	}

}
