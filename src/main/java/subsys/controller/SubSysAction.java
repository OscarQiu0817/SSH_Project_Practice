package subsys.controller;

import com.opensymphony.xwork2.ActionSupport;

import subsys.model.SubSysService;
import subsys.model.SubSysVO;

public class SubSysAction extends ActionSupport{
	private SubSysVO subsysVO;

	public SubSysVO getSubsysVO() {
		return subsysVO;
	}

	public void setSubsysVO(SubSysVO subsysVO) {
		this.subsysVO = subsysVO;
	}
	
	public String add() {
		SubSysService subsysSvc = new SubSysService();
		subsysSvc.add(subsysVO);
		
		return "suscess";
	}
		
	
}
