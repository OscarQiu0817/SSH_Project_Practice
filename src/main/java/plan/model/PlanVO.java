package plan.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import workitem.model.WorkItemVO;

public class PlanVO {
	
	private Integer plan_no;
	private Integer plan_year;
	private Date plan_start_date;
	private Date plan_end_date;
	private String plan_name;
	
	private Set<WorkItemVO> workItems = new HashSet<WorkItemVO>();
	
	public Integer getPlan_no() {
		return plan_no;
	}
	public void setPlan_no(Integer plan_no) {
		this.plan_no = plan_no;
	}
	public Integer getPlan_year() {
		return plan_year;
	}
	public void setPlan_year(Integer plan_year) {
		this.plan_year = plan_year;
	}
	public Date getPlan_start_date() {
		return plan_start_date;
	}
	public void setPlan_start_date(Date plan_start_date) {
		this.plan_start_date = plan_start_date;
	}
	public Date getPlan_end_date() {
		return plan_end_date;
	}
	public void setPlan_end_date(Date plan_end_date) {
		this.plan_end_date = plan_end_date;
	}
	public String getPlan_name() {
		return plan_name;
	}
	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
	}
	public Set<WorkItemVO> getWorkItems() {
		return workItems;
	}
	public void setWorkItems(Set<WorkItemVO> workItems) {
		this.workItems = workItems;
	}
	@Override
	public String toString() {
		return "PlanVO [plan_no=" + plan_no + ", plan_year=" + plan_year + ", plan_start_date=" + plan_start_date
				+ ", plan_end_date=" + plan_end_date + ", plan_name=" + plan_name + ", workItems=" + workItems + "]";
	}
	
	
	
	


	
	

	
	

	

	
	
	
	
}
