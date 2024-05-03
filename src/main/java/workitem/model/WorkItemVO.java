package workitem.model;

import java.io.Serializable;
import java.sql.Date;

import member.model.MemberVO;
import plan.model.PlanVO;
import subsys.model.SubSysVO;

public class WorkItemVO implements Serializable{

	private Integer WorkItem_no;
	private String WorkItem_name;
	private String WorkItem_content;
	private Double WorkItem_hour;	// 可能為 1.5 小時之類的
	private Date WorkItem_Date;		// 先暫時填寫日期，完成日期如有需要再加入
	
	// 181224 add 本工作項目隸屬計畫    // 格式 yy-plan num-startDate(MMYY)    18-P24-1217  截止 1228 ， 12天一週期 
//	private String WorkItem_plan;
//	private SubSysVO subsysVO;
//	private MemberVO memberVO;
	
	private String WorkItem_member;
	private String WorkItem_subsys;
	private PlanVO planVO;	// 2018.12.26 重構 改成 跟 計畫做映射
	

	
	
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
	public PlanVO getPlanVO() {
		return planVO;
	}
	public void setPlanVO(PlanVO planVO) {
		this.planVO = planVO;
	}
	public String getWorkItem_member() {
		return WorkItem_member;
	}
	public void setWorkItem_member(String workItem_member) {
		WorkItem_member = workItem_member;
	}
	public String getWorkItem_subsys() {
		return WorkItem_subsys;
	}
	public void setWorkItem_subsys(String workItem_subsys) {
		WorkItem_subsys = workItem_subsys;
	}
	@Override
	public String toString() {
		return "WorkItemVO [WorkItem_no=" + WorkItem_no + ", WorkItem_name=" + WorkItem_name + ", WorkItem_content="
				+ WorkItem_content + ", WorkItem_hour=" + WorkItem_hour + ", WorkItem_Date=" + WorkItem_Date
				+ ", WorkItem_member=" + WorkItem_member + ", WorkItem_subsys=" + WorkItem_subsys + ", planVO=" + planVO
				+ "]";
	}
	
	
	

}
