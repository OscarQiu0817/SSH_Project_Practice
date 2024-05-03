package member.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import member.model.MemberService;
import member.model.MemberVO;

public class MemNoAction extends ActionSupport{
	
	private Integer memno;
	private String mem_name;
	private String mem_password;
	
	public Integer getMemno() {
		return memno;
	}
	public void setMemno(Integer memno) {
		this.memno = memno;
	}

	
	public String getMem_name() {
		return mem_name;
	}
	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}
	public String getMem_password() {
		return mem_password;
	}
	public void setMem_password(String mem_password) {
		this.mem_password = mem_password;
	}
	
	
	public String login() {
		MemberService memSvc = new MemberService();
		MemberVO memVO = memSvc.login(mem_name,mem_password);
		System.out.println("login trigger");
		if(memVO != null) {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.getSession().setAttribute("memVO", memVO);
			return "success";
		}else {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("errorMsg","查無此人 ! ");
			return "failure";
		}
	}
	
	
}
