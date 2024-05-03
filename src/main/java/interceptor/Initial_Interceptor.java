package interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import plan.model.PlanService;
import plan.model.PlanVO;
import subsys.model.SubSysService;
import subsys.model.SubSysVO;
import tool.HibernateUtil_CompositeQuery;
import workitem.model.WorkItemService;
import workitem.model.WorkItemVO;

public class Initial_Interceptor  extends AbstractInterceptor implements PreResultListener{

	
	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
	
		// 從 ActionInvocation 取得 ActionContext
		ActionContext ctx = invocation.getInvocationContext();

		// 從 ActionContext 取得 Session
		Map<String,Object> session = ctx.getSession();
		
		// 註冊監聽器監聽自己 再去到 JSP 前 會去執行特定方法
	    invocation.addPreResultListener(this);
		
		List<PlanVO> plan_list = (List<PlanVO>)session.get("plan_list");

		
		System.out.println("\n1.初始化攔截器");
		
		PlanVO planVO = null;
		
		if(plan_list == null) {
			PlanService planSvc = new PlanService();
			plan_list = planSvc.getAllDesc();
			
			System.out.println("初始化計畫清單...");

			if(plan_list != null && plan_list.size() != 0) {
				session.put("plan_list", plan_list);
				planVO = plan_list.get(0); // 依照plan_start_date來排，愈後面的理論上愈大
				session.put("plan_now", planVO);
			}
			else
				session.put("errorMsg_plan", "尚未建立任何計畫!");
		}else{	// 存在 list 的情況
			
			// 存在 list ， 但還沒有放 plan_now ， 且有計畫
			if(session.get("plan_now") == null && plan_list.size() != 0) {  //如果 list 不為 null 才判斷 plan_now
				planVO = plan_list.get(0); // 依照plan_start_date來排，愈後面的理論上愈大
				session.put("plan_now", planVO);
			// 存在 list ， 還沒有放 plan_now ， 且無計畫，代表沒有任何計畫
			}else if(session.get("plan_now") == null && plan_list.size() == 0) {
				session.put("errorMsg_plan", "尚未建立任何計畫!");
			// plan_now 已存在
			}else {
				planVO = (PlanVO)session.get("plan_now");
			}

		}
		
		HttpServletRequest request = (HttpServletRequest)ctx.get(StrutsStatics.HTTP_REQUEST);
		String path = request.getServletPath();
		System.out.println("出發來源 : " + path);  // example /workitem/queryWorkItemAction!query.action add
		
		
		// ▼ 很慢  不要讓判定變複雜
		
//		SubSysService SvcBean = new SubSysService(); 
//		List<SubSysVO> subsys_list = (List<SubSysVO>) session.get("subsys_list");
//		if(subsys_list == null) {	// 若子系統為null 重新抓一個
//			System.out.println("初始化子系統選單...");
//			subsys_list = SvcBean.getAll();
//			session.put("subsys_list", subsys_list);
//		}
		
		List<SubSysVO> subsys_list = (List<SubSysVO>) session.get("subsys_list");
		if(subsys_list == null) {
			System.out.println("初始化子系統選單...");
			subsys_list = new SubSysService().getAll();
			session.put("subsys_list", subsys_list);
		}

		// 放剩餘時數用的
		if(planVO != null) {
			
			if(session.get("weekHour") == null ) {
				float weekHour = HibernateUtil_CompositeQuery.getHour_ForShow(planVO, 2); // 2 = week, 3 = plan
				session.put("weekHour",weekHour);
			}
			
			if(session.get("planHour") == null ) {
				float planHour = HibernateUtil_CompositeQuery.getHour_ForShow(planVO, 3); // 2 = week, 3 = plan
				session.put("planHour",planHour);
			}
						
		}

		// --------------------- start -------------------------
//		String result = invocation.invoke();  // 先往下跑，讓 insert into 或其他操作先執行成功後，再重新抓取 planVO
		//  --------------------- end -------------------------
		
		// interceptor 很怪 ， 沒辦法這樣用   == > 後來改用 PreResultListener
		
//		// --------------------- start -------------------------
//				String result = invocation.invoke();  // 不用先往下跑，改成 ajax 和 Criteria Query。 且先往跑會有無法設質的問題，等於根本沒用
//				// 原本想先往下跑就是想要更新 attribute ，但似乎沒辦法?
		
//		似乎是因為 interceptor 設計的關係，設值得 defalut 攔截器 在 自訂攔截器後面。
//		且攔截器 stack 是先進先出。 所以當我使用的 invocation 時 。 變成 自訂 > 設值 > 設值完畢 > 自訂結束 (我在這邊才設值，所以都要到下一次才套用)
//		//  --------------------- end -------------------------
		
		System.out.println("init 結束");
		return invocation.invoke();
		
		// Test 有 plan_now 了，理論上可以從 plan 中的 set 取得關聯的工作項目
		
		// planVO 不等於 null 的話，代表一定放到 plan_now裡面了，所以可以查出對應的工作項目。
//		if(planVO != null) {	// 如果有最新的planVO 再用其查出該計畫之工作項目， jsp 顯示 用 c:if 判斷日期來顯示。
//			List<WorkItemVO> list = (List<WorkItemVO>)session.get("list");
//			
//			if(list == null) { // 存在 plan_now ，但還沒有查 list 出來
//				System.out.println("init fired");
//				WorkItemService wiSvc = new WorkItemService();
//				list = wiSvc.getAll();
//				session.put("list", list);
//				
//			}else {	// 有 plan_now 也有 list，不做事
//				System.out.println(list.size());
//			}
//		}
		

	}

	@Override
	public void beforeResult(ActionInvocation invocation, String result) {

		// 從 ActionInvocation 取得 ActionContext
		ActionContext ctx = invocation.getInvocationContext();

		// 從 ActionContext 取得 Session
		Map<String,Object> session = ctx.getSession();
		
		// 從 ActionContext 取得 request
		HttpServletRequest request = (HttpServletRequest)ctx.get(StrutsStatics.HTTP_REQUEST);
		
		// 取得出發來源
		String path = request.getServletPath();
		
		if(path.contains("WorkItem") && !path.contains("query")) {	// 如果是 來自 工作項目 action 的請求
//			
//			
//			// 變超慢
//			
////			if(path.contains("addWorkItemAction")) { // 如果是新增工作項目項目  判斷是否異動子系統  若有異動，則重抓 subsys 否則不動作
////				if(request.getSession().getAttribute("subsys_add") != null) {
////					System.out.println("子系統異動，重新獲取子系統選單...");
////					SvcBean = new SubSysService();
////					session.put("subsys_list", SvcBean.getAll());
////					request.getSession().removeAttribute("subsys_add");
////				}
////			}
//			
			PlanVO Plan_update = (PlanVO) session.get("plan_now");	// 取出目前的 PlanVO
			
			PlanService planSvc = new PlanService();
			
			Plan_update = planSvc.findByPrimaryKey(Plan_update.getPlan_no());	// 重新抓一個  !! 會先重抓，才 insert into 難怪少一個
			session.put("plan_now", Plan_update);  // 工作項目 增改刪查，重新查詢一個 planVO 更新其狀態
			System.out.println("---偵測到 workItem 異動，自動更新 plan_now---");
			
			// 因為 planVO 改變了 時數也必須跟著變化
			float weekHour = HibernateUtil_CompositeQuery.getHour_ForShow(Plan_update, 2); // 2 = week, 3 = plan
			session.put("weekHour",weekHour);
			
			float planHour = HibernateUtil_CompositeQuery.getHour_ForShow(Plan_update, 3); // 2 = week, 3 = plan
			session.put("planHour",planHour);

		}
	}
}
