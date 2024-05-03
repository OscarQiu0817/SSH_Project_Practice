package subsys.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import workitem.model.WorkItemVO;

public class SubSysVO implements Serializable{
	private Integer SubSys_no;
	private String SubSys_engName;
	private String SubSys_twName;
	

	
	public Integer getSubSys_no() {
		return SubSys_no;
	}
	public void setSubSys_no(Integer subSys_no) {
		SubSys_no = subSys_no;
	}
	public String getSubSys_engName() {
		return SubSys_engName;
	}
	public void setSubSys_engName(String subSys_engName) {
		SubSys_engName = subSys_engName;
	}
	public String getSubSys_twName() {
		return SubSys_twName;
	}
	public void setSubSys_twName(String subSys_twName) {
		SubSys_twName = subSys_twName;
	}

	
}
