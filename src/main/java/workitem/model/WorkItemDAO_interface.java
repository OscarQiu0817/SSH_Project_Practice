package workitem.model;

import java.util.List;


public interface WorkItemDAO_interface {
	
	public void insert(WorkItemVO workitemVO);
	public void update(WorkItemVO workitemVO);
	public void delete(Integer workitem_no);
	
	public WorkItemVO findByPrimaryKey(Integer workitem_no);
	public List<WorkItemVO> getAll();
}
