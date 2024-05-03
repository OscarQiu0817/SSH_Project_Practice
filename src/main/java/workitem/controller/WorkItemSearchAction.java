package workitem.controller;

import java.sql.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.ActionSupport;

import member.model.MemberVO;
import subsys.model.SubSysVO;
import tool.HibernateUtil;
import workitem.model.WorkItemVO;

public class WorkItemSearchAction extends ActionSupport{
	
	private Integer WorkItem_no;
	private String WorkItem_name;
	private String WorkItem_content;
	private Double WorkItem_hour;	// 可能為 1.5 小時之類的
	private Date WorkItem_Date;		// 先暫時填寫日期，完成日期如有需要再加入
	private String WorkItem_plane;
	private SubSysVO subsysVO;
	private MemberVO memberVO;
	
	
	public Integer getWorkItem_no() {
		return WorkItem_no;
	}
	public void setWorkItem_no(Integer workItem_no) {
		WorkItem_no = workItem_no;
	}
	public String getWorkItem_name() {
		return WorkItem_name;
	}
	public void setWorkItem_name(String workItem_name) {
		WorkItem_name = workItem_name;
	}
	public String getWorkItem_content() {
		return WorkItem_content;
	}
	public void setWorkItem_content(String workItem_content) {
		WorkItem_content = workItem_content;
	}
	public Double getWorkItem_hour() {
		return WorkItem_hour;
	}
	public void setWorkItem_hour(Double workItem_hour) {
		WorkItem_hour = workItem_hour;
	}
	public Date getWorkItem_Date() {
		return WorkItem_Date;
	}
	public void setWorkItem_Date(Date workItem_Date) {
		WorkItem_Date = workItem_Date;
	}
	public SubSysVO getSubsysVO() {
		return subsysVO;
	}
	public void setSubsysVO(SubSysVO subsysVO) {
		this.subsysVO = subsysVO;
	}
	public MemberVO getMemberVO() {
		return memberVO;
	}
	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	public String getWorkItem_plane() {
		return WorkItem_plane;
	}
	public void setWorkItem_plane(String workItem_plane) {
		WorkItem_plane = workItem_plane;
	}
	
	
	
	
//	public String searchByPlane() {	// 依照計畫，計畫每兩周一次。  預想 : 一年 52周 26個計畫，可以算出每個計劃的起始日。但這樣就忽略了假期因素
//		
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		Transaction tx = session.beginTransaction();
//		List<WorkItemVO> list = null;
//		
//		try {
//			Criteria query = session.createCriteria(WorkItemVO.class);
////			query.add(Restrictions.le("cr_start_date", java.sql.Date.valueOf(s2)));
//			
//			
//			
//			
//		}catch(RuntimeException ex) {
//			if (tx != null)
//				tx.rollback();
//			throw ex; //System.out.println(ex.getMessage());
//		}
//		
//
//		
//		
//	}
	
}
