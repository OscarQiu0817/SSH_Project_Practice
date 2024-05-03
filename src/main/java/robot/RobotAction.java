package robot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

import member.model.MemberVO;
import plan.model.PlanVO;
import tool.HibernateUtil_CompositeQuery;
import workitem.model.WorkItemVO;

public class RobotAction extends ActionSupport{
	
	
	public void myDispatcher() throws IOException, JSONException {
		
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse res = ServletActionContext.getResponse();
		
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		
		String action = req.getParameter("action");
		JSONObject jsonResult = new JSONObject();
		
		if(action.contains("寄信")) {		// 前端做一次驗證，包含寄信才會送 ajax 。 後端再檢查一次是否包含寄信字串
			
			String toEmail = ((MemberVO)(req.getSession().getAttribute("memVO"))).getMem_email();
			
			String result = sendEmail(toEmail);
			System.out.println(result);
			
			if(result.equals("success")) {
				jsonResult.put("response", "信件已寄出!");
				out.print(jsonResult);
			}
			else {
				jsonResult.put("response", "出現問題! 請檢查系統設定");
				out.print(jsonResult);
			}
			
		}else if(action.contains("統計1")) {
			
			getStatistics(1);
			
		}
			
		else if(action.contains("統計2")){
			getStatistics(2);
		}
		else {
			jsonResult.put("response", "系統無法辨認此指令!");
			out.print(jsonResult);
		}
		
	}
	// 參考 大吳 奇門遁甲 範例
	public static String sendEmail(String toEmail) {
			String result = "success";
		   try {
			   // 設定使用SSL連線至 Gmail smtp Server
			   Properties props = new Properties();
			   props.put("mail.smtp.host", "smtp.gmail.com");
			   props.put("mail.smtp.socketFactory.port", "465");
			   props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			   props.put("mail.smtp.auth", "true");
			   props.put("mail.smtp.port", "465");

	       // ●設定 gmail 的帳號 & 密碼 (將藉由你的Gmail來傳送Email)
	       // ●須將myGmail的【安全性較低的應用程式存取權】打開
		     final String myGmail = "yourGmail@gmail.com";
		     final String myGmail_password = "yourPassword";
			   Session session = Session.getInstance(props, new Authenticator() {
				   protected PasswordAuthentication getPasswordAuthentication() {
					   return new PasswordAuthentication(myGmail, myGmail_password);
				   }
			   });

			   Message message = new MimeMessage(session);
			   message.setFrom(new InternetAddress(myGmail));
			   message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail));
			  
			   
			   // 取得需要的資料 - 本計畫工作清單。 依照日期排序 
			   HttpServletRequest req = ServletActionContext.getRequest();
			   PlanVO planVO = (PlanVO)req.getSession().getAttribute("plan_now");
			   if(planVO == null)
				   return "fail";
			   List<WorkItemVO> list = HibernateUtil_CompositeQuery.getAll_forGetHour(planVO, 3); // 3 = all plan
			   
			   StringBuffer sb = new StringBuffer();
			   
			   String date_record = "";
			   for(WorkItemVO wiVO : list) {
				   String dateOfWork = wiVO.getWorkItem_Date().toString();
				   if(! date_record.equals(dateOfWork)) {
					   date_record = dateOfWork;
					   sb.append("\n").append(dateOfWork).append("\n");	// 換日期的話 就製造一些空間

				   }
				   
				   sb.append(wiVO.getWorkItem_member())
				   .append("@")
				   .append(wiVO.getWorkItem_subsys())
				   .append(" - ")
				   .append(wiVO.getWorkItem_name())
				   .append(" - ")
				   .append(wiVO.getWorkItem_content())
				   .append("@")
				   .append(wiVO.getWorkItem_hour())
				   .append("\n");
				   
			   }
			   
			   //設定信中的主旨  
			   message.setSubject("計畫工作清單");
			   
			   //設定信中的內容 
			   message.setText(sb.toString());

			   Transport.send(message);
			   System.out.println("傳送成功!");
	     }catch (MessagingException e){
		     System.out.println("傳送失敗!");
		     result = "fail";
		     e.printStackTrace();
	     }
		
		return result;
	}
	
	public static String getStatistics(int range) throws IOException {
		String result = "success";
		JSONObject jsonResult = new JSONObject();

		
	   // 取得需要的資料 - 本計畫工作清單。 依照日期排序 
	   HttpServletRequest req = ServletActionContext.getRequest();
	   PlanVO planVO = (PlanVO)req.getSession().getAttribute("plan_now");
	   if(planVO == null)
		   return "fail";
	   List<WorkItemVO> list = HibernateUtil_CompositeQuery.getAll_forGetHour(planVO, range+1); // 2 = week 3 = all plan
		
		HttpServletResponse res = ServletActionContext.getResponse();
		
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
	   
//		1. 花最多時間的工作項目     > 長條  > 直接放到 json 裡面就好了 (工作名稱 + 時數)
//		2. 子系統占比		    > 圓餅  > 計算重複資料
	   
		JSONObject tempj = new JSONObject();
		JSONObject piej = new JSONObject();
		JSONObject timej = new JSONObject();
		
		JSONArray dataj = new JSONArray();
		JSONArray pieArr = new JSONArray();
		JSONArray jArr = new JSONArray();
		
		Map<String,Integer> map = new HashMap();
		String BigWork = "";
		double BigTime = 0;
		
		try {
			for(WorkItemVO wiVO : list) {
				
				// 處理 子系統次數 > 圓餅
				Integer i = map.get(wiVO.getWorkItem_subsys());
				if(i == null) {
					map.put(wiVO.getWorkItem_subsys(), 1);
				}else {
					map.put(wiVO.getWorkItem_subsys(), i+1);
				}
				// 紀錄時數最多值
				if(wiVO.getWorkItem_hour() > BigTime) {
					BigWork = wiVO.getWorkItem_name();
					BigTime = wiVO.getWorkItem_hour();
				}
				
				tempj = new JSONObject();
				tempj.put("name", wiVO.getWorkItem_name());
				tempj.put("hour", wiVO.getWorkItem_hour());
				dataj.put(tempj);
				
//				System.out.println(dataj);
			}
			
			for(String key : map.keySet()) {
				piej = new JSONObject();
				piej.put("name", key);
				piej.put("count",map.get(key));
				pieArr.put(piej);
			}
			
			timej.put("BigWork", BigWork);
			timej.put("BigTime", BigTime);
			
			jArr.put(dataj);	// 存基本資料
			jArr.put(pieArr);	// 存子系統
			jArr.put(timej);	// 存長條圖需要的資料
			
			if(range == 1)
				jsonResult.put("response", "本周資料統計完畢!");
			else
				jsonResult.put("response", "計畫資料統計完畢!");
			
			jsonResult.put("response2", "出現異常，請檢查計畫、工作項目是否已存在");
			
			jArr.put(jsonResult);
			out.print(jArr);
			
		} catch (JSONException e) {
			result = "fail";
			e.printStackTrace();
		}
		
		
	   return result;
	}
}
