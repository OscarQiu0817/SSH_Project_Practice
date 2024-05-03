package tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import plan.model.PlanVO;
import tool.HibernateUtil;
import workitem.model.WorkItemVO;

// 好像不用用到 ?? 畢竟不是查詢  而是從 planVO 中拿出 workItem List 來顯示
// 19.01.08 再度拿出來用~
public class HibernateUtil_CompositeQuery {
	
	public static int thisListSize = 0;
	
//	public static Criteria get_Criteria_For_AnyDB(Criteria query, String columnName,String value) {
//		
//		if("cr_no".equals(columnName) || "cr_place".equals(columnName) )  // 完全比對 varchar
//			query.add(Restrictions.eq(columnName, value));
//		else if("cr_level".equals(columnName)) 
//			query.add(Restrictions.eq(columnName, new Integer(value)));	  // 完全比對 Integer
//		else if("cr_name".equals(columnName)) // 模糊查詢 varchar
//			query.add(Restrictions.like(columnName, "%" + value + "%"));
//		
//		return query;
//	}
	
	public static List<WorkItemVO> getAllC(PlanVO planVO, int range, int page, String d1, String d2, String sortWay){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = session.beginTransaction();
		List<WorkItemVO> list = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			
			Criteria query = session.createCriteria(WorkItemVO.class); // 創建 Criteria

			query.add(Restrictions.eq("planVO", planVO));	
			
			if(range == 1) {
				java.sql.Date dd = java.sql.Date.valueOf(sdf.format(new Date()));
				System.out.println("dd="+ dd);
				query.add(Restrictions.eq("WorkItem_Date", dd));
				
			}else if (range == 2) {
				query.add(Restrictions.between("WorkItem_Date", java.sql.Date.valueOf(d1), java.sql.Date.valueOf(d2)));
				
			}else if (range == 3) {
				
			}
			
			// 傳 0  進來， 代表目前是1。( 因為傳的值是新的排序法 ) 傳1 代表目前是 0
			// 0 == 升序 1== 降序 
			switch(sortWay){
			 	case "h0":
					query.addOrder(Order.asc("WorkItem_hour"));
			 		break;
			 	case "h1":
					query.addOrder(Order.desc("WorkItem_hour"));
			 		break;
			 	case "d0":
					query.addOrder(Order.asc("WorkItem_Date"));
			 		break;
			 	case "d1":
					query.addOrder(Order.desc("WorkItem_Date"));
			 		break;
			}

			thisListSize = query.list().size();
			
			System.out.println("第 " + page +" 頁");
//			System.out.println(page * 5);
//			System.out.println((page + 1 ) * 5 );	// MaxResult 不受 FirstResult 影響
			
			query.setFirstResult((page-1)*5);
			query.setMaxResults(5);
			
			list = query.list();
			
			tx.commit();
			
		}catch (RuntimeException ex) {
			if (tx != null)
				tx.rollback();
			throw ex; //System.out.println(ex.getMessage());
		}
		
		return list;
	}
	
	public static List<WorkItemVO> getAll_forGetHour(PlanVO planVO, int range){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Transaction tx = session.beginTransaction();
		List<WorkItemVO> list = null;
		
		try {
			
			Criteria query = session.createCriteria(WorkItemVO.class); // 創建 Criteria

			query.add(Restrictions.eq("planVO", planVO));	
			
			if (range == 2) {
				 Calendar cal_mon = Calendar.getInstance();
				 cal_mon.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //當周的禮拜一
				 cal_mon.set(Calendar.HOUR_OF_DAY, 0);
				 cal_mon.set(Calendar.MINUTE, 0);
				 cal_mon.set(Calendar.SECOND, 0);	// 設成 00:00:00
				 
				 Calendar cal_sun = Calendar.getInstance();
				 cal_sun.set(Calendar.DATE, cal_mon.get(cal_mon.DATE) + 6); //當周的禮拜日，因為剛剛設為禮拜一，所以加上6
				 // 不直接用 Calendar.SUNDAY，因時間計算方式不同	
				 
				 String d1 = sdf.format(cal_mon.getTime());	// 當周一
				 String d2 = sdf.format(cal_sun.getTime());	// 當周日
				 query.add(Restrictions.between("WorkItem_Date", java.sql.Date.valueOf(d1), java.sql.Date.valueOf(d2)));
			}
			
			query.addOrder(Order.asc("WorkItem_Date"));
	
			list = query.list();
			
			tx.commit();
			
		}catch (RuntimeException ex) {
			if (tx != null)
				tx.rollback();
			throw ex; //System.out.println(ex.getMessage());
		}
		
		return list;
	}
	
	public static float getHour_ForShow(PlanVO planvo,int range){

		List<WorkItemVO> list = HibernateUtil_CompositeQuery.getAll_forGetHour(planvo, range);
		
		float returnHour = 0;
		
		if(range == 2) {
			returnHour = 40;
			System.out.println("\n本周工作項目共\t" + list.size() + " 項");
			
		}else if(range ==3) {
			
			System.out.println("本計畫工作項目共\t" + list.size() + " 項");
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			
			cal1.setTime(planvo.getPlan_start_date());
			cal2.setTime(planvo.getPlan_end_date());
			
			int endD = cal2.get(Calendar.DAY_OF_YEAR); 
			int workDay = 0;
			
			while(cal1.get(Calendar.DAY_OF_YEAR) <= endD) {
				
//				System.out.println("日期-" + cal1.get(Calendar.MONTH) +" / " + cal1.get(Calendar.DATE) + "("+cal1.get(Calendar.DAY_OF_WEEK) +")");
				
				if(cal1.get(Calendar.DAY_OF_WEEK) != 1 && cal1.get(Calendar.DAY_OF_WEEK) != 7) {
					workDay++;
				}
				
				cal1.set(Calendar.DAY_OF_YEAR,cal1.get(Calendar.DAY_OF_YEAR)+1); // +1天
			}
			System.out.println("計畫總工作天數為\t" + workDay + " 天");
			returnHour = workDay * 8;
		}

		
		for(WorkItemVO wiVO : list) {
			returnHour -= wiVO.getWorkItem_hour();
		}

		return returnHour;
	}
	
		
//	public static void main(String args[]) {
//		// 配合 req.getParameterMap()方法 回傳 java.util.Map<java.lang.String,java.lang.String[]> 之測試
//		Map<String, String[]> map = new TreeMap<String, String[]>();
////		map.put("cr_no", new String[] { "c1" });
////		map.put("cr_name", new String[] { "會" });
//		map.put("cr_date_1", new String[] { "2018-07-16" });
//		map.put("cr_date_2", new String[] { "2018-09-23" });
////		map.put("cr_level", new String[] { "1" });
////		map.put("cr_place", new String[] { "台北市" });
//		map.put("action", new String[] { "getXXX" }); // 注意Map裡面會含有action的key
//		
//		List<ClassroomVO> list = getAllC(map,0);
//		
//		for(ClassroomVO crVO : list) {
//			System.out.println(crVO.getCr_name());
//		}
//	}
}
