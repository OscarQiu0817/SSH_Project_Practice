package interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import member.model.MemberVO;

public class MemberInSessionInterceptor  extends AbstractInterceptor{

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
	
		// 從 ActionInvocation 取得 ActionContext
		ActionContext ctx = invocation.getInvocationContext();

		// 從 ActionContext 取得 Session
		Map<String,Object> session = ctx.getSession();
		
		// 從 Session內 取得 (key) memVO 的值，判斷是否已登入
		MemberVO memVO = (MemberVO) session.get("memVO");
		
		System.out.println("2.會員資格攔截器");
		
		if(memVO == null) {
			System.out.println("無會員資料 -- 重導回登入頁面中 ...");
			HttpServletRequest request = ServletActionContext.getRequest();
			
			// 記下目前location，方便登入後可以回到原頁面
			String location = request.getRequestURI();
			
			session.put("location", location);
			ctx.put("promt", "登入已失效，請重新登入");
			
			return "login";
		}else {
			return invocation.invoke();
		}
		
	}

}
