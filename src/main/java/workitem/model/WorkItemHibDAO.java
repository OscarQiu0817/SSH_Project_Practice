package workitem.model;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class WorkItemHibDAO implements WorkItemDAO_interface{
	
	public static final String GET_ALL = "from WorkItemVO order by WorkItem_no";

	private HibernateTemplate hibernateTemplate;
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) { 
        this.hibernateTemplate = hibernateTemplate;
    }
	
	
	@Override
	public void insert(WorkItemVO workitemVO) {
		hibernateTemplate.saveOrUpdate(workitemVO);
	}

	@Override
	public void update(WorkItemVO workitemVO) {
		hibernateTemplate.update(workitemVO);
	}

	@Override
	public void delete(Integer workitem_no) {
		WorkItemVO wiVO = new WorkItemVO();
		wiVO.setWorkItem_no(workitem_no);
		hibernateTemplate.delete(wiVO);
	}

	@Override
	public WorkItemVO findByPrimaryKey(Integer workitem_no) {
		WorkItemVO wiVO = null;
		wiVO = hibernateTemplate.get(WorkItemVO.class, workitem_no);
		return wiVO;
	}

	@Override
	public List<WorkItemVO> getAll() {
		List<WorkItemVO> list = null;
		list = hibernateTemplate.find(GET_ALL);
		return list;
	}

}
