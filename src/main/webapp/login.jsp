<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>
<title>登入頁面</title>

<!-- jquery 1.12.4-->
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

<style>
	.align-center{
		vertical-align: middle;
		margin-top: 230px;
	}
	
	.center{
		text-align:center;
		color : #AAAAAA;
	}
	
	#input_center{
	
		text-align:center;
		
	}
	
	.margin-left{
		margin-left : 10px;
	}

</style>



<c:if test="${promt != null}">
	<script>
		function aa(){
			alert("${promt}");
		};
		window.onload = aa;
	</script>
</c:if>

</head>
<body>
	<div class="container">
		<div class="row align-center">
			<div class="col-md-4 col-md-offset-4">
				<div class="panel panel-info">
					<div class="panel-heading">
						<span class="panel-title">登入</span>
						<c:if test="${not empty errorMsg}">
							<span class="panel-title" style="display:inline-block;width:80%;text-align:right;color:red">錯誤 - ${errorMsg }</span>
						</c:if>
					</div>
					
					<div class="panel-body">
						<s:form action="login" namespace="/member" theme="simple">
							<div Class="form-group">
								<s:textfield name="mem_name" cssClass="form-control" autocomplete="off"/>
								<BR>
								<s:textfield type="password" name="mem_password" cssClass="form-control"/>
							</div>
							
							<div id="input_center">
								<s:submit value="登入" method="login" cssClass="btn btn-success" /> 
							</div>
						</s:form>
					</div>
				</div>
			</div>
		</div>
	</div>


</body>
</html>