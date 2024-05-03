package plan.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import plan.model.PlanService;
import plan.model.PlanVO;
import tool.HibernateUtil_CompositeQuery;

public class PlanAction  extends ActionSupport{
	
	private PlanVO planVO;
	
	public PlanVO getPlanVO() {
		return planVO;
	}

	public void setPlanVO(PlanVO planVO) {
		this.planVO = planVO;
	}
	
	public String add() {
		
		PlanService PlanSvc = new PlanService();
		
//		System.out.println(planVO.toString());
		
		PlanSvc.add(planVO);
		
		
//		System.out.println(planVO.getPlan_no());   // 此時已經有 PK 了 
//		
		HttpServletRequest request = ServletActionContext.getRequest();
		request.getSession().setAttribute("plan_now", planVO); 	// 把當前 計畫 以 剛剛新增的蓋掉

		return "success";
		
	}
	
	public String update() {
		
		PlanService PlanSvc = new PlanService();
		
		PlanSvc.update(planVO);
		
		HttpServletRequest request = ServletActionContext.getRequest();
		request.getSession().setAttribute("plan_now", planVO); 	// 把當前 計畫 以 剛剛修正的蓋掉
		
		return "success";
		
	}
	
	public String changeP() {
		
		PlanService PlanSvc = new PlanService();
		
		planVO = PlanSvc.findByPrimaryKey(planVO.getPlan_no());
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		request.getSession().setAttribute("plan_now", planVO); 	// 把當前 計畫 以 新查詢的蓋掉
		
// 改變計畫的話~ 一定要重抓時數 因此取消 IF 條件
		float weekHour = HibernateUtil_CompositeQuery.getHour_ForShow(planVO, 2); // 2 = week, 3 = plan
		session.setAttribute("weekHour",weekHour);


		float planHour = HibernateUtil_CompositeQuery.getHour_ForShow(planVO, 3); // 2 = week, 3 = plan
		session.setAttribute("planHour",planHour);
		
		return "success";
	}
	
//	public String getAll() {
//		
//		PlanService PlanSvc = new PlanService();
//		
//		List<PlanVO> list = null;
//		
//		list = PlanSvc.getAll();
//		
//		if(list != null) {
//			HttpServletRequest request = ServletActionContext.getRequest();
//			request.getSession().setAttribute("Plan_list", list);
//			return "success";
//		}else {
//			HttpServletRequest request = ServletActionContext.getRequest();
//			request.getSession().setAttribute("error_Msg", "尚無任何計畫 ! ");
//			return "success";
//		}
//		
//	}
	
	
}
