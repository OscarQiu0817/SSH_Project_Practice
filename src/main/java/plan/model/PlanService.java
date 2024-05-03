package plan.model;

import java.util.List;

import org.hibernate.dialect.MySQLDialect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PlanService {
	
	private PlanDAO_interface dao;
	
	public PlanService() {
		
		MySQLDialect a = null;
		
		ApplicationContext context = new ClassPathXmlApplicationContext("model-config.xml");
		dao = (PlanDAO_interface) context.getBean("planDAO");
	}
	
	public PlanVO add(PlanVO planVO) {
		return dao.insert(planVO);
	}
	
	public void update(PlanVO planVO) {
		dao.update(planVO);
	}
	
	public void delete(Integer plan_no) {
		dao.delete(plan_no);
	}
	
	public PlanVO findByPrimaryKey(Integer plan_no) {
		return dao.findByPrimaryKey(plan_no);
	}
	
	public List<PlanVO> getAllDesc() {
		return dao.getAllDesc();
	}
	
	public List<PlanVO> getAllAsc() {
		return dao.getAllAsc();
	}
	
	public List<PlanVO> getAll(){
		return dao.getAll();
	}
	
	public PlanVO getNewest() {
		return dao.getNewest();
	}
	
}
