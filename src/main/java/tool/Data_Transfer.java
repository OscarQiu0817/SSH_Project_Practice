package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import member.model.MemberService;
import member.model.MemberVO;
import plan.model.PlanService;
import plan.model.PlanVO;
import workitem.model.WorkItemService;
import workitem.model.WorkItemVO;

// 此工具用來進行資料的移出與讀取，因為尚處於開發階段，隨時會異動資料庫欄位。
// 透過此工具先將資料輸出，待資料庫重設過後再讀取並寫回資料。
// 照片更新 屬於 檔案上傳 也一併放在這裡

public class Data_Transfer extends ActionSupport{
	
	//上傳檔案
	private static File upload;
	//上傳檔案型別
	private static String uploadContentType;
	//上傳檔名
	private static String uploadFileName;
	
	
	public void myDispatcher() throws IOException {
		
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse res = ServletActionContext.getResponse();
		
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		
		PlanService planSvc = new PlanService();
		
		String action = req.getParameter("action");
		System.out.println("---action = " + action + " ---");
		
		if(action.equals("輸出本次計畫")) {
			PlanVO planVO = (PlanVO)req.getSession().getAttribute("plan_now");
			
			if(planVO == null) {
				out.println("資料輸出失敗 ! 請檢查計畫狀態");
			}else {
				writeDataOut(planVO,req,res,"one");
				out.println("計畫輸出成功 ! ");
			}

			
		}else if (action.equals("輸出整份專案")) {
			
			List<PlanVO> planList = planSvc.getAllAsc();	// 以 日期排 愈早愈前
			
			if(planList == null || planList.size() == 0) {
				out.println("資料輸出失敗 ! 請檢查計畫狀態");
			}else {
				
				for(PlanVO plan : planList ) {
					writeDataOut(plan,req,res,"all");
				}
				
				out.println("專案輸出成功 ! ");
			}
		}
	}

	
	// 轉移 以 計畫為單位
	public static void writeDataOut(PlanVO plan,HttpServletRequest req, HttpServletResponse res,String way) {
		
		try {
			
			// 透過查詢工具取得 傳入計畫 的 所有工作項目
			List<WorkItemVO> list = HibernateUtil_CompositeQuery.getAll_forGetHour(plan, 3);
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			String today = sdf.format(new Date());
			
			String filePath = "C:\\Users\\user\\Desktop\\MySelf\\Project01_Struts2-Spring-Hibernate\\";
			String planName = plan.getPlan_name();
			String fileName = "";
			
			if(way.equals("one")) {
				
				fileName = filePath + planName + "_" + today + ".txt";
				
			}else if (way.equals("all")) {
				fileName = filePath + "All_Plan" + "_" + today + ".txt";
				
			}
			
			
			File file = new File(fileName);
			
			FileWriter fwriter=new FileWriter(file,true);
			  
			StringBuffer sb = new StringBuffer();
			
			sb.append("plan=");
			sb.append(plan.getPlan_year() + ",");
			sb.append(plan.getPlan_start_date() + ",");
			sb.append(plan.getPlan_end_date()+",");
			sb.append(plan.getPlan_name()+"\n");
			
			
			for(WorkItemVO wiVO : list) {
				  
				sb.append(wiVO.getWorkItem_subsys() + ",");
				sb.append(wiVO.getWorkItem_name() + ",");
				sb.append(wiVO.getWorkItem_content() + ",");
				sb.append(wiVO.getWorkItem_hour() + ",");
				sb.append(wiVO.getWorkItem_Date() + ",");
				sb.append(wiVO.getWorkItem_member() + ",");
				sb.append(wiVO.getPlanVO().getPlan_no() + "\n");
				  
			}
			  
			fwriter.write(sb.toString());
			fwriter.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static String readData() {
		
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpSession session = req.getSession();
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			
			System.out.println("------檔案傳輸------\n檔名 : " + uploadFileName);		// example : 19-P01-0102_01-10.txt
			System.out.println("類型 : " + uploadContentType);	// example : text/plain
			
			FileInputStream in = new FileInputStream(upload);
			
			if(uploadContentType.contains("text")) {
				
				BufferedReader br = new BufferedReader( new InputStreamReader(in, "UTF-8" ));
	
		        StringBuilder sb = new StringBuilder();
		        String line;
		        
		        System.out.println("正在導入以下工作項目...\n");
		        
		        PlanService planSvc = new PlanService(); 
		        WorkItemService wiSvc = new WorkItemService();
		        PlanVO planVO = null; 
		        WorkItemVO wiVO = null;
		        
		        List<PlanVO> plan_list = new ArrayList<PlanVO>();
		        
		        // 傳入的workItem 一定是同一個計劃的。 但這個計畫是否等於當前計畫 ?
		        // 且導入計畫，所存的是 plan_no，這個 plan_no 和 DB 重設後的不一定一樣
		        // 所以 !! 應該連 Plan 都要一併導出.. 
		        // 且架構應是 ， 寫出 plan > workItems
		        // 導入時，先 add Plan ， 然後 add 其中的 workItems
		        
	//	        List<WorkItemVO> list = new ArrayList<WorkItemVO>();
		        
		        while(( line = br.readLine()) != null ) {
		        	
		        	if(line.contains("plan=")) {	// 這一行代表 一個計畫開始導入
		        		
		        		String[] plan_data = line.replace("plan=", "").split(","); 	// 去掉開頭後，以逗號分隔
		        		planVO = new PlanVO();
		        		planVO.setPlan_year(Integer.valueOf(plan_data[0]));
		        		planVO.setPlan_start_date(java.sql.Date.valueOf(plan_data[1]));
		        		planVO.setPlan_end_date(java.sql.Date.valueOf(plan_data[2]));
		        		planVO.setPlan_name(plan_data[3]);
		        		
		        		planVO = planSvc.add(planVO);
		        		
		        		plan_list.add(planVO);
		        		
		        		System.out.println("\n計畫開始");
	
		        	}else {
		        		
		        		if(planVO != null) {
		        			
		    	        	// 拆解 line ， 並把他們塞到 工作項目之中。
		    	        	String[] strs = line.split(",");
		    	        	
		    	        	wiVO = new WorkItemVO();
		    	        	wiVO.setWorkItem_subsys(strs[0]);
		    	        	wiVO.setWorkItem_name(strs[1]);
		    	        	wiVO.setWorkItem_content(strs[2]);
		    	        	wiVO.setWorkItem_hour(Double.valueOf(strs[3]));
		    	        	wiVO.setWorkItem_Date(java.sql.Date.valueOf(strs[4]));
		    	        	wiVO.setWorkItem_member(strs[5]);
		         	
		    	        	wiVO.setPlanVO(planVO);
		        			
		    	        	wiSvc.add(wiVO);
		        		}
		        	}
	//	            sb.append( line );
	//	            sb.append( '\n' );
		        	
		        	System.out.println(line);
		        }
		        
		        // list 也順便放一放
		        session.setAttribute("plan_list", plan_list);
		        
		        // 到這邊 目前的 planVO 理論上是最後一個 planVO 了，所以直接放入 session 中 。 一併計算時數
		        session.setAttribute("plan_now", planVO);

				if(session.getAttribute("weekHour") == null) {
					float weekHour = HibernateUtil_CompositeQuery.getHour_ForShow(planVO, 2); // 2 = week, 3 = plan
					session.setAttribute("weekHour",weekHour);
				}
				
				if(session.getAttribute("planHour") == null) {
					float planHour = HibernateUtil_CompositeQuery.getHour_ForShow(planVO, 3); // 2 = week, 3 = plan
					session.setAttribute("planHour",planHour);
				}
					
				
		        
		        System.out.println("\n\n----------導入完成");
		        
		        br.close();
		        
			}else if(uploadContentType.contains("image")) {
				
				MemberService memSvc = new MemberService();
				MemberVO memVO = (MemberVO)session.getAttribute("memVO");
				
				byte[] photo = new byte[in.available()];
				in.read(photo);
				
				memVO.setMem_photo(photo);
				
				memSvc.update(memVO);
				
				System.out.println("照片更新完成!");
			}
			
	        in.close();

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "success";
	}


	public File getUpload() {
		return upload;
	}


	public void setUpload(File upload) {
		this.upload = upload;
	}


	public String getUploadContentType() {
		return uploadContentType;
	}


	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}


	public String getUploadFileName() {
		return uploadFileName;
	}


	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	
}
