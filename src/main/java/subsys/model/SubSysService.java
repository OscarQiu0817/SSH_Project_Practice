package subsys.model;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SubSysService {
	private SubSysDAO_interface dao;
	
	public SubSysService() {
		ApplicationContext context = new ClassPathXmlApplicationContext("model-config.xml");
		dao =(SubSysDAO_interface) context.getBean("subsysDAO");
	}
	
	public SubSysVO add(SubSysVO subsysVO) {
		return dao.insert(subsysVO);
	}
	
	public void update(SubSysVO subsysVO) {
		dao.update(subsysVO);
	}
	
	public void delete(Integer SubSys_no) {
		dao.delete(SubSys_no);
	}
	
	public SubSysVO findByPrimaryKey(Integer SubSys_no) {
		return dao.findByPrimaryKey(SubSys_no);
	}
	
	public SubSysVO findByTwName(String  twName) {
		return dao.findByTwName(twName);
	}

	public List<SubSysVO> getAll(){
//		System.out.println("get all subsys");
		return dao.getAll();
	}
}
