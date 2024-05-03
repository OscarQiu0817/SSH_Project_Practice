package member.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import member.model.MemberService;
import member.model.MemberVO;

public class MemberAction extends ActionSupport{
	
	private MemberVO memVO;
	
	public MemberVO getMemVO() {
		return memVO;
	}

	public void setMemVO(MemberVO memVO) {
		this.memVO = memVO;
	}
	
	public String add() {
		MemberService memSvc = new MemberService();
		memSvc.add(memVO);
		System.out.println("add() 方法被呼叫，資料庫 insert 成功");
		return "success";
	}
	
	public String update() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String requestURL = request.getParameter("requestURL");
		System.out.println("requestURL = " + requestURL);
		
		MemberService memSvc = new MemberService();
		memSvc.update(memVO);
		System.out.println("資料庫  Update 成功");
		
		if(requestURL.equals("")) {
			return "Success";
		}else
			return null;
	}
}
