package plan.model;

import java.util.List;

public interface PlanDAO_interface {
	
	public PlanVO insert(PlanVO planVO);
	public void update(PlanVO planVO);
	public void delete(Integer plan);
	
	public PlanVO findByPrimaryKey(Integer plan);
	
	public PlanVO getNewest();
	
	public List<PlanVO> getAllDesc();
	public List<PlanVO> getAllAsc();
	

	public List<PlanVO> getAll();
}
