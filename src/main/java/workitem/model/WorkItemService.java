package workitem.model;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WorkItemService {
	private WorkItemDAO_interface dao;
	
	public WorkItemService() {
		ApplicationContext context = new ClassPathXmlApplicationContext("model-config.xml");
		dao =(WorkItemDAO_interface) context.getBean("workitemDAO");
	}
	
	public void add(WorkItemVO workitemVO) {
		dao.insert(workitemVO);
	}
	
	public void update(WorkItemVO workitemVO) {
		dao.update(workitemVO);
	}
	
	public void delete(Integer workitem_no) {
		dao.delete(workitem_no);
	}
	
	public WorkItemVO findByPrimaryKey(Integer workitem_no) {
		return dao.findByPrimaryKey(workitem_no);
	}

	public List<WorkItemVO> getAll(){
		return dao.getAll();
	}
}
