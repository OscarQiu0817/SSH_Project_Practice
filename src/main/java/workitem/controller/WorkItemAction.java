package workitem.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

import plan.model.PlanVO;
import subsys.model.SubSysService;
import subsys.model.SubSysVO;
import tool.HibernateUtil;
import tool.HibernateUtil_CompositeQuery;
import workitem.model.WorkItemService;
import workitem.model.WorkItemVO;

public class WorkItemAction extends ActionSupport{
	
	private WorkItemVO workitemVO;

	public WorkItemVO getWorkitemVO() {
		return workitemVO;
	}

	public void setWorkitemVO(WorkItemVO subsysVO) {
		this.workitemVO = subsysVO;
	}
	

	public String add() {
		
		SubSysService sss = new SubSysService();
		
		if(sss.findByTwName(workitemVO.getWorkItem_subsys()) == null) {
			SubSysVO ssv = new SubSysVO();
			ssv.setSubSys_engName(" ");
			ssv.setSubSys_twName(workitemVO.getWorkItem_subsys());
			sss.add(ssv);
			
			System.out.println("子系統異動，正在更新子系統選單");
			HttpServletRequest req = ServletActionContext.getRequest();
			req.getSession().setAttribute("subsys_list", new SubSysService().getAll());
		}
		
		WorkItemService wiSvc = new WorkItemService();
		
		wiSvc.add(workitemVO);
				
		return "success";
	}
	
	public String update() {
		
		SubSysService sss = new SubSysService();
		
		if(sss.findByTwName(workitemVO.getWorkItem_subsys()) == null) {
			SubSysVO ssv = new SubSysVO();
			ssv.setSubSys_engName(" ");
			ssv.setSubSys_twName(workitemVO.getWorkItem_subsys());
			sss.add(ssv);
			
			System.out.println("子系統異動，正在更新子系統選單");
			HttpServletRequest req = ServletActionContext.getRequest();
			req.getSession().setAttribute("subsys_list", new SubSysService().getAll());
		}
		
		WorkItemService wiSvc = new WorkItemService();
		
		wiSvc.update(workitemVO);
		
		return "success";
	}

	
	public String delete() {
		WorkItemService wiSvc = new WorkItemService();
		
		wiSvc.delete(workitemVO.getWorkItem_no());
		return "success";
	}
	
	public String query() throws IOException {
		System.out.println("---query start---");
		
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse res = ServletActionContext.getResponse();
		
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		PlanVO plan_now = ((PlanVO)req.getSession().getAttribute("plan_now"));
				
		int range = Integer.parseInt(req.getParameter("range"));
		int page =  Integer.parseInt(req.getParameter("page"));
		String sort = req.getParameter("sort");
		
		
		
		String d1 = null, d2 = null;
		
//		System.out.println(range);
//		System.out.println(page);
		
		if(range == 2) {
			
			 Calendar cal_mon = Calendar.getInstance();
			 cal_mon.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //當周的禮拜一
			 cal_mon.set(Calendar.HOUR_OF_DAY, 0);
			 cal_mon.set(Calendar.MINUTE, 0);
			 cal_mon.set(Calendar.SECOND, 0);	// 設成 00:00:00
			 
			 Calendar cal_sun = Calendar.getInstance();
			 cal_sun.set(Calendar.DATE, cal_mon.get(cal_mon.DATE) + 6); //當周的禮拜日，因為剛剛設為禮拜一，所以加上6
			 // 不直接用 Calendar.SUNDAY，因時間計算方式不同	
			 
			 d1 = sdf.format(cal_mon.getTime());	// 當周一
			 d2 = sdf.format(cal_sun.getTime());	// 當周日

		}
		
		List<WorkItemVO> list = HibernateUtil_CompositeQuery.getAllC(plan_now, range, page, d1, d2, sort);
		System.out.println("本次查詢共查出 " + list.size() + " 筆");
		
		// 存一些屬性
		int totalPage = HibernateUtil_CompositeQuery.thisListSize;
		
		if(totalPage % 5 != 0) {
			totalPage/=5;
			totalPage++;
		}else
			totalPage/=5;
		
		if(totalPage == 0 )
			totalPage = 1;
		
		
		
		
		HttpSession session = req.getSession();
							
		session.setAttribute("range", range);		// 放在 session 層級， 因為 request 到 interceptor 攔截器時就掉了
		session.setAttribute("page", page);		    // 對 ajax 來說還是沒用 因為屬性的改變需要重整頁面
		session.setAttribute("sort", sort);		    // 對 ajax 來說還是沒用 因為屬性的改變需要重整頁面
		session.setAttribute("totalPage", totalPage); // 19.01.09 目前存起來不是給 ajax 用的 而是給其他 增改刪的 action
		
		JSONObject tempj = new JSONObject();
		JSONObject dataj = new JSONObject();
		JSONArray jArr = new JSONArray();
		
		try {
			for(WorkItemVO wiVO : list) {
				tempj = new JSONObject();	// 若沒有這行，則 tempj 雖然不會互相蓋掉，但參照到同一個記憶體位置，全部數據都一樣
				tempj.put("workItem_no", wiVO.getWorkItem_no());
				tempj.put("subsys", wiVO.getWorkItem_subsys());
				tempj.put("name", wiVO.getWorkItem_name());
				tempj.put("content", wiVO.getWorkItem_content());
				tempj.put("hour", wiVO.getWorkItem_hour().toString());
				tempj.put("date", wiVO.getWorkItem_Date().toString());
				tempj.put("member", wiVO.getWorkItem_member());
				tempj.put("plan_no",wiVO.getPlanVO().getPlan_no());
				
				jArr.put(tempj);
			}
			dataj.put("range", range);
			dataj.put("page", page);
			dataj.put("totalPage", totalPage);
			
			jArr.put(dataj);
			
			out.print(jArr);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}

//Set<WorkItemVO> workItemSet = plan_now.getWorkItems();  // 不能直接過去，因為等等會用 iterator 刪除 項目，達到條件搜尋的效果
//但在此情況下，參照同一位置，原本的 workItem 的內容也會被移除
//System.out.println(workItemSet);

//Set<WorkItemVO> workItemSet = new HashSet<WorkItemVO>();
//workItemSet.addAll(plan_now.getWorkItems());	// 創一個新的記憶位置，再把內容轉移過去

//  set json

//if(range != null) {
//	workItemSet = SetFilter(plan_now,workItemSet,range);
////	System.out.println("range = " + range);
//	
//	JSONObject tempj = new JSONObject();
//	JSONArray jArr = new JSONArray();
//	
//
//		try {
//			for(WorkItemVO wiVO : workItemSet) {
//				tempj = new JSONObject();	// 若沒有這行，則 tempj 雖然不會互相蓋掉，但參照到同一個記憶體位置，全部數據都一樣
//				tempj.put("workItem_no", wiVO.getWorkItem_no());
//				tempj.put("subsys", wiVO.getWorkItem_subsys());
//				tempj.put("name", wiVO.getWorkItem_name());
//				tempj.put("content", wiVO.getWorkItem_content());
//				tempj.put("hour", wiVO.getWorkItem_hour().toString());
//				tempj.put("date", wiVO.getWorkItem_Date().toString());
//				tempj.put("member", wiVO.getWorkItem_member());
//				tempj.put("plan_no",wiVO.getPlanVO().getPlan_no());
//				
//				jArr.put(tempj);
//			}
//			
//			out.print(jArr);
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	
//
//}

// 改成 criteria query 。 回傳 list，不必操作 set ， 19.01.08 修改 //

//public static Set<WorkItemVO> SetFilter(PlanVO plan_now,Set<WorkItemVO> set,String range){
//	
//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	Date plan_start_date = plan_now.getPlan_start_date();
//	Iterator<WorkItemVO> it = set.iterator();
//	
//	switch(range) {					// date example : 2019-01-02
//	case "1" :		// today
//		String today = sdf.format(new Date());
//		
//		for (; it.hasNext();) {
//			WorkItemVO wiVO = it.next();
//		    if (! wiVO.getWorkItem_Date().toString().equals(today)) {
//		    	it.remove();
//		    }
//		}
//
//		break;
//		
//	case "2" :		// this week
//		
//		 Calendar cal_mon = Calendar.getInstance();
//		 cal_mon.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //當周的禮拜一
//		 cal_mon.set(Calendar.HOUR_OF_DAY, 0);
//		 cal_mon.set(Calendar.MINUTE, 0);
//		 cal_mon.set(Calendar.SECOND, 0);	// 設成 00:00:00
//		 
//		 Calendar cal_sun = Calendar.getInstance();
//		 cal_sun.set(Calendar.DATE, cal_mon.get(cal_mon.DATE) + 6); //當周的禮拜日，因為剛剛設為禮拜一，所以加上6
//		 // 不直接用 Calendar.SUNDAY，因時間計算方式不同	
//		 
//		 Date d1 = cal_mon.getTime();	// 當周一
//		 Date d2 = cal_sun.getTime(); // 當周日
//		
//		 
//		for (; it.hasNext();) {
//			WorkItemVO wiVO = it.next();
//			Date wiVO_d = wiVO.getWorkItem_Date();
//			
//			if( ! (wiVO_d.after(d1) && wiVO_d.before(d2))) {
//				it.remove();
//			}
//		}
//		 			
//		break;
//		
//	case "3" :		// this plan
//		
////		for(WorkItemVO wiVO : set) {	// 不用做任何操作
////			
////		}
//
//		break;
//}
//	
//	
//	return set;
//}

// 18.12.26 移除子系統與工作項目之間的關聯
//public String add() {
//	// 因為傳進來的值並非PK，所以workitemVO 
//	// 所帶有的 subsys無法被看成一個完整的物件
//	// 也因此，用twName 反查 subsysVO是否存在後，要再將其設定回 workitemVO才行。
//	
//	String twName = workitemVO.getSubsysVO().getSubSys_twName();  
//	SubSysService subsysSvc = new SubSysService();
//	
//	SubSysVO subsysVO = subsysSvc.findByTwName(twName);
//	if(subsysVO == null) {	// 若 此子系統為新的
//		subsysVO = new SubSysVO();
//		subsysVO.setSubSys_engName(" ");	// 不能為 null
//		subsysVO.setSubSys_twName(twName);  // 新傳入的名子
//		subsysVO = subsysSvc.add(subsysVO); // add() 並回傳
//		workitemVO.setSubsysVO(subsysVO);   // set()
//	}else {
//		workitemVO.setSubsysVO(subsysVO);	// set()
//	}
//	
//	WorkItemService workitemSvc = new WorkItemService();
//	workitemSvc.add(workitemVO);
//	
//	return "success"; //  應該改成 ajax 作法
//}
