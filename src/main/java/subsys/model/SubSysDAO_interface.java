package subsys.model;

import java.util.List;

public interface SubSysDAO_interface {
	
	public SubSysVO insert(SubSysVO subsysVO);
	public void update(SubSysVO subsysVO);
	public void delete(Integer SubSys_no);
	
	public SubSysVO findByPrimaryKey(Integer SubSys_no);
	
	public SubSysVO findByTwName(String twName);
	
	public List<SubSysVO> getAll();
	
}
