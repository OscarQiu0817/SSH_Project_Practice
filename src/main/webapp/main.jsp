<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ page import="java.util.*"%>
<%@ page import="workitem.model.*"%>
<%@ page import="member.model.*"%>

<%
	// 暫時寫來擋一下~ 應該放在filter 或 Struts2 的 攔截器  	19.01.10 註解
// 	MemberVO memVO = (MemberVO)request.getSession().getAttribute("memVO");
// 	if(memVO == null){
// 		response.sendRedirect(request.getContextPath() + "/login.jsp");
// 	}
%>


<!DOCTYPE html>
<html style="height:100%">
<head>
<title>工作項目主頁面</title>

<!-- jquery 1.12.4-->
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<!--  jquery-ui -->
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css">
<!-- Bootstrap 3.3.7 -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<!-- Chart 描繪圖表用的 -->
<script src="${pageContext.request.contextPath}/js/Chart.js"></script>

<!-- 自己的CSS  暫不使用，待完成後再轉移過去  19.0115啟用-->
<link href="${pageContext.request.contextPath}/css/myCss.css" rel="stylesheet">

<style>

</style>

<script>


	
// 	-----------------------------------------------------------------------------

	// 計畫 新增確認
	var plan_Path = "/WorkItemProject_comment/plan/";
	function add_plan_check(span){
		
		cancel_opration(1);
		
		$("input[name='planVO.plan_no']").val();	// 避免 更新後，此欄位有值，而導致新增方法也變成更新
		
		var bigSpan = $(".planOperation_span");
		var addBtn = bigSpan.find("span:eq(1)");
		var title = addBtn.attr("title");
		
		if(title == "新增一個計畫"){
			
			addBtn.addClass("glyphicon-ok");	// 改圖案
			$(".planNow_span").css("display","none");	//把當前計畫span隱藏
			$(".addPlan_span").css("display","inline");	//把 input 欄位打開
			$("#plan_form").attr("action",plan_Path + "PlanAction!add");
			addBtn.attr("title","確認新增");
			
		}else if(title == "確認新增"){
			
			var input_empty = false;
			
			// 欄位空值檢查
			input_empty = check_plan_empty(); // return true if there is any input field is empty

			// 欄位格式檢查
			
			if(!input_empty){	// 如果回傳 false -> ! false 進入 if
				check_plan_format();
			}else{
				alert("新增計畫失敗，請檢查欄位");
			}
		}
	}
	
	// 計畫更新確認
	function update_plan_check(span,plan_no){
		
		cancel_opration(2);
		
		var bigSpan = $(".planOperation_span");
		var udtBtn = bigSpan.find("span:eq(2)");
		var title = udtBtn.attr("title");
		
		
		if(title == "修改當前計畫"){
			
			udtBtn.addClass("glyphicon-ok");	// 改圖案   ..  ok 蓋不過 edit
			udtBtn.removeClass("glyphicon-edit"); // 所以要移掉，既然有移除就要加回來
			$(".planNow_span").css("display","none");	//把當前計畫span隱藏
			$(".addPlan_span").css("display","inline");	//把 input 欄位打開
			
			//將目前的計畫拆開，設值到各個input中  -start
			var planNow_val = $(".planNow_span").text();
			
//要 trim 一下 有很多空格在
			var aps = $(".addPlan_span");
			
			aps.find("input:eq(0)").val(planNow_val.trim().substring(0,2));
			aps.find("input:eq(1)").val(planNow_val.trim().substring(3,6));
			aps.find("input:eq(2)").val(planNow_val.trim().substring(7,11));
			
			var endD_fromEL = "${plan_now.plan_end_date}";
			aps.find("input:eq(3)").val(endD_fromEL.trim().substring(5,10).replace("-",""));
			// 將目前的計畫拆開，設值到各個input中  -end
			
			$("#plan_form").attr("action",plan_Path + "PlanAction!update");
			udtBtn.attr("title","確認修改");
			
		}else if(title == "確認修改"){
			
			var input_empty = false;
			
			// 欄位空值檢查
			input_empty = check_plan_empty(); // return true if there is any input field is empty

			// 欄位格式檢查
			
			if(!input_empty){	// 如果回傳 false -> ! false 進入 if
				$("input[name='planVO.plan_no']").val(plan_no);
				check_plan_format();
			}else{
				alert("修改計畫失敗，請檢查欄位");
			}
		}
	}
	
	// 計畫 確認欄位格式
	function check_plan_format(){
		
		var input_invalid = false;
		
		var aps = $(".addPlan_span");
		
		var v1 = aps.find("input:eq(0)").val();
		var v2 = aps.find("input:eq(1)").val();
		var v3 = aps.find("input:eq(2)").val();
		var v4 = aps.find("input:eq(3)").val();
		
		if(v1.length != 2 || v2.length != 3 || v3.length != 4 || ( v4.length != 0 && v4.length != 4)){
			
			console.log(v1.length);
			console.log(v2.length);
			console.log(v3.length);
			
			input_invalid = true;
			
		}else{
			
			if(isNaN(v1) || v2.substring(0,1) != "P" || isNaN(v2.substring(1,3)) || isNaN(v3) || isNaN(v4)){
				console.log(isNaN(v1));
				console.log(v2.substring(0,1));
				console.log(isNaN(v2.substring(1,3)));
				console.log(isNaN(v3));

				input_invalid = true;
				
			}else{
				
				var date = "20" + v1 + "-" + v3.substring(0,2) + "-" + v3.substring(2,4);

// 				alert(date);
				
				if(new Date(date).getDate()!=date.substring(date.length-2)){	// 判斷是否為有效日期
					console.log(date);
					console.log(date.substring(date.length-2));
					input_invalid = true;
				}else{
					
					var sd = new Date(date); // startDate
					var start_date_str = sd.getFullYear()+ "-" + (sd.getMonth()+1) + "-" + sd.getDate();
					$("input[name='planVO.plan_start_date']").val(start_date_str);
					
					if(v4.length == 0){ // 如果不填的話
						
						sd.setDate(sd.getDate() + 12); // endDate
						var end_date_str = sd.getFullYear()+ "-" + (sd.getMonth()+1) + "-" + sd.getDate();
						$("input[name='planVO.plan_end_date']").val(end_date_str);
						
					}else{	//若有填 判斷是否為有效日期
						
						var endD = "20" + v1 + "-" + v4.substring(0,2) + "-" + v4.substring(2,4);
						if(new Date(endD).getDate()!=endD.substring(endD.length-2)){
							input_invalid = true;
						}else{
							var ed = new Date(endD); // endDate
							var end_date_str = ed.getFullYear()+ "-" + (ed.getMonth()+1) + "-" + ed.getDate();
							$("input[name='planVO.plan_end_date']").val(end_date_str);
						}
					}
				}
			}
		}
		
		if(input_invalid){
			alert("欄位有誤 ! 請依照格式填寫 : \n xx(西元年後兩位) - Pxx(計畫) - 起始日(MMDD) - 結束日(MMDD/可空)")
		}else{
			$("input[name='planVO.plan_year']").val("20"+v1)
			var name = v1+"-" + v2 + "-" + v3;
			$("input[name='planVO.plan_name']").val(name);
			$("#plan_form").submit();
		}
	}
	
	// 計畫 查看欄位是否有填
	function check_plan_empty(){
		
		var ifBreak = false;
		
		$(".work_plan").each(function(){
			val = $(this).val();
			if($(this).attr("title") != "MMDD結束日"){ // 結束日不用檢查
				if(val.length ==0 || val === undefined){
					ifBreak = true;
					return false;	// 跳出each();
				}
			}
		});
		
		return ifBreak;
	}
	
	// 計畫 取消目前操作 (input 消失)
	function cancel_opration(span){
		
		var bigSpan = $(".planOperation_span"); //不管之前的操作是更新或新增，在取消操作時一律復原
		
		if(span == 2){
			var addBtn = bigSpan.find("span:eq(1)");	// 新增鈕
			addBtn.removeClass("glyphicon-ok");
			addBtn.attr("title","新增一個計畫");
			
		}else if (span == 1){
			var udtBtn = bigSpan.find("span:eq(2)");	// 更新鈕
			udtBtn.removeClass("glyphicon-ok");
			udtBtn.addClass("glyphicon-edit"); // 因為 ok 蓋不過 edit，所以先行移除，若取消操作，則把 edit加回來
			udtBtn.attr("title","修改當前計畫");
			
		}else if( span == 3){
			var addBtn = bigSpan.find("span:eq(1)");	// 新增鈕
			addBtn.removeClass("glyphicon-ok");
			addBtn.attr("title","新增一個計畫");
			
			var udtBtn = bigSpan.find("span:eq(2)");	// 更新鈕
			udtBtn.removeClass("glyphicon-ok");
			udtBtn.addClass("glyphicon-edit"); // 因為 ok 蓋不過 edit，所以先行移除，若取消操作，則把 edit加回來
			udtBtn.attr("title","修改當前計畫");
			
			$(".addPlan_span").css("display","none");
			$(".planNow_span").css("display","inline");
		}
				
	}
	
	
	// 打開下拉式選單
	function changePlan(){
// 		alert($(".dropdown_div").attr("class"));
// 		$(".dropdown_div").addClass("open");   按鈕的預設行為 會阻止我們加上 open 這個 class 因此可以利用Timeout 來加上
	  setTimeout(function() {
		  $(".dropdown_div").addClass("open");
	  });
	}
	
	// 切換計畫
	function selectNowPlan(plan_no,plan_name){
// 		alert(plan_no);
		$("input[name='planVO.plan_no']").val(plan_no);	// 設值 並送出
		
		var result = confirm("確定要切換至  " + plan_name + " 嗎 ?") ;
		
		if(result){
			$("#plan_form").attr("action",plan_Path + "PlanAction!changeP");
			$("#plan_form").submit();
		}
	}
	
	
	
	
	
	// ▲ 計畫
	// --------------------------------------------------------------------
	// ▼ 工作項目
	
	// 工作項目 更新確認
	function update_check(id){
		
		var tr =  $("#tr_" + id);
		
		var isUpdated = tr.attr("update");
		
		if(isUpdated == "false"){
			
			update_cancel_all();

			for(var i = 7 ; i >= 0 ; i--){	// find .class 是從尾巴過來的 ~ ?
				var input = $('<input form="update_form_'+id+'">').val(tr.find(".value_td:eq("+ i +")").text());
				
				var td = $('<td>').append(input);
				if(i<5){
					td.addClass("update_td");
				}else{
					td.css("display","none");
				}
// 				tr.append(td);	
				tr.prepend(td); // 插到最前面，因為後面有放 button 了 ，用 append 會放到最後面，格式會跑掉
			}
			
			tr.find("input:eq(0)").attr("name","workitemVO.WorkItem_subsys");
			tr.find("input:eq(1)").attr("name","workitemVO.WorkItem_name");
			tr.find("input:eq(2)").attr("name","workitemVO.WorkItem_content");
			tr.find("input:eq(3)").attr("name","workitemVO.WorkItem_hour");
			
			tr.find("input:eq(4)").attr("name","workitemVO.WorkItem_Date").attr("id","date_2");
			 $("#date_2").datepicker({ minDate: new Date(plan_start_date), maxDate: new Date(plan_end_date), dateFormat: 'yy-mm-dd' });
			
			
			tr.find("input:eq(5)").attr("name","workitemVO.WorkItem_member");
			tr.find("input:eq(6)").attr("name","workitemVO.WorkItem_no");
			tr.find("input:eq(7)").attr("name","workitemVO.planVO.plan_no");
			
			tr.find(".normal_td").css("display", "none");  // 移除td顯示欄位
			tr.find(".update_td").css("display", "table-cell");
			tr.attr("update","true");
			tr.addClass("update_tr_color_change");
			
			tr.find("span:eq(1)").css("display", "inline-block");
			tr.find("span:eq(0)").addClass("glyphicon-check");	// 不用移除原本的，後面的會蓋掉前面的
			tr.find("span:eq(0)").attr("title","確認修改");
			
			tr.find("#describe").text("Editing..");
			
		}else if(isUpdated == "true"){	//確認修改工作項目
			$("#update_form_"+id).submit();
		}
	}
	
	// 取消 一個 工作項目 更新
	function update_cancel(id){
		
		var tr =  $("#tr_" + id);
		tr.find(".update_td").remove();	// 移除整個update的 class
// 		tr.find(".update_td").attr("show","off"); 所以不需要再對這個 class 做屬性切換
		tr.attr("update","false");

		tr.find(".normal_td").css("display", "table-cell");	 // 讓原本的 td 重新顯示

		tr.removeClass("update_tr_color_change");	// 是有套用上去的，只是蓋不過show_data_table tr:nth那個
		// 所以加上 !important 屬性強制蓋過去
		// 不然用 .css() 的話，無法取消
		tr.find("span:eq(1)").css("display", "none");
		tr.find("span:eq(0)").removeClass("glyphicon-check");
		tr.find("span:eq(0)").attr("title","編輯");
		tr.find("#describe").text("");
		
	}
	
	// 取消所有 工作項目 更新
	function update_cancel_all(){
		
		$("#show_data_table").find("tbody").find("tr").each(function(){
			var tr = $(this);
			tr.find(".update_td").remove();	// 移除整個update的 class
			tr.attr("update","false");

			tr.find(".normal_td").css("display", "table-cell");	 // 讓原本的 td 重新顯示

			tr.removeClass("update_tr_color_change");	// 是有套用上去的，只是蓋不過show_data_table tr:nth那個
			tr.find("span:eq(1)").css("display", "none");
			tr.find("span:eq(0)").removeClass("glyphicon-check");
			tr.find("span:eq(0)").attr("title","編輯");
			tr.find("#describe").text("");
		});
	}
	
	//工作項目  新增確認
	function add_check(){
		
		var date = $("#date_1").val();
		var hour = $("#hour_1").val();
		
		if(date === undefined || date.length == 0){
			$("#date_1").css("border","1px solid red");
			$('[data-toggle="tooltip"]').tooltip("show");
			
		    setTimeout(function(){
		    	$('[data-toggle="tooltip"]').tooltip('hide');
		    	$("#date_1").css("border","");
		    }, 3000);
		    
			return false;
		}else if(isNaN(hour)){
			alert("時數請填寫數字!")
			return false;
		}else{
			$('[data-toggle="tooltip"]').tooltip();
			return true;
		}

	}
	
	// 工作項目 刪除確認
	function delete_check(id){
		var answer = confirm("確定要刪除嗎!? 工作項目將被且無法復原 !");
		
		if(answer){
			
			$("#delete_input").val(id);
			$("#delete_form").submit();
			var tr =  $("#tr_" + id);
			tr.find("#describe").text("Deleting..");
		}
	}
	
	// 重設各項參數
	function resetParam(){
		$(".tr_input").find("td").find("input").val("");
	}
	
	// modal 相關方法
	function generate(){
		
		if(generate_way == 0){	// 產生工作清單
			$("#exampleModalLabel").text("本頁工作項目清單");
			var workItems = $("#show_data_table").find("tr").not(":first"); // 找所有工作項目的tr
			
			var text = '';
			workItems.each(function(){
				var subsys = $(this).find("td:eq(0)").text();
				var name = $(this).find("td:eq(1)").text();
				var content = $(this).find("td:eq(2)").text();
				var hour = $(this).find("td:eq(3)").text();
				
				text += "oscar" + "@" + subsys + " - " + name + " - " + content + "@" + hour +"<BR>";
				
			});
			
			$(".modal-body").html(text);
			
		}else if(generate_way == 1){ // 統計數據 -> 這裡使用 Chart.js 這個 插件
			
			// 前置作業
			$(".modal-body").empty(); // 清空元素
			generate_way = 0;		  // 設為0，下次點擊產生工作清單才會正常。
			
//				在輸入 統計 給機器人的時候就會發送 ajax 並拉回資料，在 ajax 的成功方法中才會呼叫此方法。
//				也就是說，jsonData一定有值，若無值就是有點問題，所以去判斷 jsonData 的長度
			if(jsonData.length != 0){ 
				$("#exampleModalLabel").text("統計數據");
				
				var canvas = $('<div style="overflow:hidden; margin-bottom:10px"><div style="width:160px;height:250px;float:left"></div><div style="width:600px;height:250px;float:left"><canvas id="myChart"></canvas>');
				var canvas2 = $('<div style="overflow:hidden ; margin-bottom:10px"><div id="div2" style="width:160px;height:250px;float:left"></div><div style="width:600px;height:250px;float:left"><canvas id="myChart2"></canvas>');
				$(".modal-body").append(canvas);
				$(".modal-body").append(canvas2);
				
				var ctx = $('#myChart');
				var ctx2 = $('#myChart2');
				
				var data_name = [];
				var data_time = [];
				var data_color= [];
				
				var sub_name = [];
				var sub_count= [];
				var sub_color= [];
				
				var div1 = $(".modal-body").find("div:eq(0)").find("div");
				var div2 = $("#div2");
				
				$.each(jsonData, function (index, objs) {
					
					if(index == 0){				// dataj
						
						var data_count = objs.length;
						
						$.each(objs, function (index, obj){
							data_name.push(obj.name);
							data_time.push(obj.hour)
							data_color.push(getColor());
						});
						
						div1.append("本次工作項目 共 <font color='red'> " + data_count +"</font> 項<BR>");
						
						
					}else if(index == 1){		// piej  前端才做計算
						
						var data_count = objs.length;
						var total_count = 0;
						var subsys_name = '';
						var compare = 0;
						
						$.each(objs, function (index, obj){
							sub_name.push(obj.name);
							sub_count.push(obj.count);
							sub_color.push(getColor());
							
							total_count += obj.count;
							if(obj.count > compare){
								subsys_name = obj.name;
								compare = obj.count;
							}
						});
						div2.append("本次子系統 共 <font color='red'> " + data_count +"</font> 類<BR>");
						div2.append("其中 <font color='red'>"+ subsys_name + "</font> <BR>在" + total_count + " 項中占了 "+ compare 
								+" 項,<BR> 足足有 <font color='red'> " + (compare/total_count * 100).toFixed(1) +"%</font> !");
						
						
					}else if(index ==2){		// timej   // 後端計算好放到 json
						
						div1.append("其中耗時最長的是 <BR><font color='red'>" + objs.BigWork +"</font><BR>");
						div1.append("一共耗費了 <font color='red'>" + objs.BigTime + "</font> 小時!");
						
					}else{
						
					}	// response

				});
				
				var myChart = new Chart(ctx, {
					  type: 'bar',
					  data: {
					    labels: data_name,
					    datasets: [{
					      label: '工作時數(小時)',
					      data: data_time,
					      backgroundColor: data_color,
					      borderColor: data_color,
					      borderWidth: 1,
					    }]
					  },
	                options: {
	                	responsive:true,
	                	maintainAspectRatio: false,
	                    scales: {
	                        yAxes: [{
	                            ticks: {
	                                beginAtZero: true,
//	                              max: 40 //max value for the chart is 60
	                            }
	                        }]
	                    }
	                }
				});
				
				var myChart2 = new Chart(ctx2,{
				    type: 'pie',
				    data: {    
				    	datasets: [{
				        	data: sub_count,
					        backgroundColor: sub_color,
					        borderColor: sub_color,
					        borderWidth: 1,
				    	}],
				    	labels: sub_name
				    	},
				    options: {
				    	responsive:true,
	                	maintainAspectRatio: false
	                }
				});
			}
		}
	}
	
	function randomTo255(){
		
		var randomNum = Math.floor(Math.random() * 255 +1);
		
		return randomNum;
	}
	
	function getColor(){
		
		var r = randomTo255();
		var g = randomTo255();
		var b = randomTo255();
		var a = '0.5';
		
		
		var color = 'rgba(' + r +',' + g +',' + b +','+a+')';
		
		
		return color;
	}
	
	// 使用者傳送訊息 - ajax
	function sendMessage() {
	    
	    var inputMessage = document.getElementById("message");
	    var message = inputMessage.value.trim();
	    
	    if (message === ""){
	        alert ("訊息請勿空白!");
	        inputMessage.focus();	
	    }else{

	        inputMessage.value = "";
	        inputMessage.focus();
	        
			var messagesArea = $("#messagesArea");
	        
			var userMessageDiv = $('<div class="userMessage">');
	        var userTypeDiv = $('<div class="userType">');
	        
	        userMessageDiv.text(message);
	        userTypeDiv.append(userMessageDiv);

	        messagesArea.append(userTypeDiv);
	        messagesArea.scrollTop(function() { return this.scrollHeight; });	// jquery
	        
// 	        var messagesArea = document.getElementById("messagesArea");
// 	        messagesArea.scrollTop = messagesArea.scrollHeight; // javascript 寫法

			if(message.indexOf('寄信') != -1 || message.indexOf('統計') != -1){ // 關鍵字 
				
				var send_data = "action=" + message;
				
				$.ajax({
		    		type:"POST",
		    		url:"<%=request.getContextPath()%>/robot/RobotAction!myDispatcher.action",
		    		data: send_data,
		    		processData: false,  
		    		dataType:"json",	 // json格式回傳
		    		success: function(response,XMLHttpRequest){
		    			
		    			 var result = response.response;
		    			 if(result === undefined){
		    				 if(response[0].length == 0)
		    				 	result = response[3].response2;	// 出現異常
		    				 else
		    					 result = response[3].response;	// 資料正常取得
		    			 }
		    			 robotTyping(result);
						
		    			if(message.indexOf('統計1') != -1 || message.indexOf('統計2') != -1){	// 如果是統計1 or 2 的話，產出一個統計的 modal視窗
		    				jsonData = response;
		    				generate_way = 1;
		    				$("#modal-caller").click();
		    				
		    			}
		    		},
		    		error : function(response){
		    			alert('error!');
		    		}
		    	});
				
				
			}else{
				// 非關鍵字
				robotTyping("請輸入有效的訊息~");
			}
	    }
	}
	
	// 機器人打字效果
	function robotTyping(message){
		
		var messagesArea = $("#messagesArea");
        var robotTypeDiv = $('<div class="robotType" id="wave"><span class="dot"></span><span class="dot"></span><span class="dot"></span>');
                
        messagesArea.append(robotTypeDiv);
        messagesArea.scrollTop(function() { return this.scrollHeight; });	// jquery
        
        setTimeout(function(){
        	robotTypeDiv.empty(); // 移除子元素 <span class="dot">
        	robotTypeDiv.text(message);
        	messagesArea.scrollTop(function() { return this.scrollHeight; });	// jquery
        	}, message.length * 150);	// 依據輸入字串計算打字時間
	}
	

		
// -------------------------------------------------------------------------
// ▼ 初始化 
	
	// 取得起始日與結束日，若以下兩項無值，代表尚無計畫新增!
	var plan_start_date = '${plan_now.plan_start_date}';	// example '2019-01-02'
	var plan_end_date = '${plan_now.plan_end_date}';		// example '2019-01-08'
	
	// 存 每次查詢的 range & page 若為首次進入，則設為1
	var range_record = '${range}';
	if(range_record.length == 0 )
		range_record = 1;
	
	var page_record = '${page}';
	if(page_record.length == 0 )
		page_record = 1;
	
	var sort_record = '${sort}';
	if(page_record.length ==0)
		sort_record = 'd0';	// 預設為 日期 升序 (小到大)
		
	var generate_way = 0;	// 0 = 清單 / 1 = 圖表
	var jsonData = '';		// 統計用
		
	$(function(){
		
		$('[data-toggle="tabajax"]:eq(' + range_record + ')').tab('show'); // 使用之前存起來的範圍參數呼叫 tab 方法
		
	    var day_list = ['日', '一', '二', '三', '四', '五', '六'];
	    var Now = new Date();
	    var Today = Now.getFullYear()+ "-" + (Now.getMonth()+1) + "-" + Now.getDate() + "  (" + day_list[Now.getDay()] +")";
	    
	    // 把 weekHour 和 planHour 於後端計算完畢後，前端 jsp 顯示
	    $(".data_div").html("今天日期為 : " + Today 
	    		+ "<BR> 本周尚有 " + "<font color='red'> ${weekHour} </font> " + "個小時的工作項目需填寫" 
	    		+ "&nbsp&nbsp&nbsp&nbsp / &nbsp&nbsp&nbsp&nbsp計畫尚有 " + " <font color='red'> ${planHour} </font>    " + "個小時的工作項目需填寫");
	    
	    // 有用，但日期的範圍應該以計畫來決定
// 	    var day_of_week = Now.getDay(); // 0 1 2 3 4 5 6
// 	    var min = day_of_week * -1;
// 	    var max = 6 - day_of_week;


// 	    如果從後端拿不到東西，代表目前沒有任何一個計畫
		if(plan_start_date.length == 0 || plan_start_date === undefined){
			//把 工作項目新增欄位關掉
			$(".panel-body").css("background-color","orangered");
			$(".tr_input").find("td").find("input").attr("readonly","readonly");
			$(".add_div").find("input:eq(0)").attr("disabled","disabled");
			
		}else{	// 如果目前有計畫的話，才做以下事情

		    $("#date_1").datepicker({ 
		    	minDate: new Date(plan_start_date), 
		    	maxDate: new Date(plan_end_date), 
		    	dateFormat: 'yy-mm-dd' });
		    $("#date_1").attr("readonly","readonly"); // 避免手動輸入
		}
	    

// 		▼ 2019.01.02改成下面寫法   (19.01.09 新增一個  沒有作用 的 a 標籤，拿來觸發 ajax 用)

		$('[data-toggle="tabajax"], .page_control, .sort_img').click(function(e) {
			 e.preventDefault();
			 var r = ($(this).attr("href") == undefined ? range_record : $(this).attr("href")) ; // 1 - 今日 .  2 - 本周 . 3 - 計畫
   			 var range = "range=" + r;
   			 
			 var p;
			 var s;
			 if (r == $(this).attr("href")){ // href 有定義 => 切換範圍
				 p = 1; 					 // 頁數重置
				 s = 'd0';					 // 排序重置
				 $("#ds").attr('sort','d1'); // 注意這裡預設為 d1 (降序)，這樣送到後端，才會是 降序 ，而達到預期結果
			 	 $("#ts").attr('sort','h0'); // 將時數排序重設為預設值，避免相衝
				 
			 }else{     					 //herf 未定義 => 換頁 or 初始化 or 排序
				 
				 // page 未定義 => 初始化 (去讀page_record) /  有定義 => 換頁 
				 p = ($(this).attr("page") == undefined ? page_record : $(this).attr("page")); 
				 page_record = p;	// 在 頁數改變的情況下，修正前端的 page_record。供排序使用

				 // sort 未定義 => 初始化 (去讀sort_record) /  有定義 => 排序 
				 s = ($(this).attr('sort') == undefined ? sort_record : $(this).attr('sort'));
				 
				 if(s == $(this).attr('sort')){	// 若為排序，則判斷是何種
					 sort_record = s;   // 在 排序改變的情況下，修正前端的 page_record。供換頁使用
					 
					 switch(s){			// 改變對應的排序方式
					 	case 'h0':
					 		$(this).attr('sort','h1');
					 		$("#ds").attr('sort','d0');	// 若為時數的排序，則先將日期排序重設為預設值，避免相衝
					 		break;
					 	case 'h1':
					 		$(this).attr('sort','h0');
					 		$("#ds").attr('sort','d0');
					 		break;
					 	case 'd0':
					 		$(this).attr('sort','d1');
					 		$("#ts").attr('sort','h0');		// 若為日期的排序，則先將時數排序重設為預設值，避免相衝
					 		break;
					 	case 'd1':
					 		$(this).attr('sort','d0');
					 		$("#ts").attr('sort','h0');
					 		break;
					 }
				 } 
			 }
			  
   			 var page  = "&page=" + p; 
   			 var sort  = "&sort=" + s;
   			 var send_data = range + page + sort;
   			
   			 if( $(this).attr("hide")!= "true")
   			 	$(this).tab('show'); // tab 切換
   		     
 	    	$.ajax({
	    		type:"POST",
	    		url:"<%=request.getContextPath()%>/workitem/queryWorkItemAction!query.action",
	    		data: send_data,
	    		processData: false,  // 這行不要掉 否則會有錯誤 ( 告知傳送的檔案是蝦密 ) 默認為true 代表轉成字串
	    							 // 如果要发送 DOM 树信息或其它不希望转换的信息，请设置为 false。
	    		dataType:"json",	 // json格式回傳
	    		success: function(response,XMLHttpRequest){
	    			
    		    	var data_table = $("#show_data_table") ; // 在這後面組合
					
    		    	// 先將所有查出的資料 tr 移除， 第一行是資料名稱不用動
					$("#show_data_table").find("tr").not(":first").remove(); // 找所有tr，不是第一個就移除  (work !)
					
// 					alert(response.length); // 一頁5筆 多一筆 dataj , length == 6
    		    	
	    		    $.each(response, function (index, obj) {
	    		    	
						if(index != response.length -1){
							
							var td1= $('<td class="normal_td value_td">').text(obj.subsys);
		    		    	var td2= $('<td class="normal_td value_td">').text(obj.name);
		    		    	var td3= $('<td class="normal_td value_td">').text(obj.content);
		    		    	var td4= $('<td class="normal_td value_td">').text(obj.hour);
		    		    	var td5= $('<td class="normal_td value_td">').text(obj.date);
		    		    	
		    		    	var td6= $('<td class="hidden_td value_td" style="display:none">').text(obj.member);
		    		    	var td7= $('<td class="hidden_td value_td" style="display:none">').text(obj.workItem_no);
		    		    	var td8= $('<td class="hidden_td value_td" style="display:none">').text(obj.plan_no);
		    		    	
		    		    	
		    		    	// 更新的 form
		    		    	var form_update = $('<form class="Action_form" id="update_form_'+obj.workItem_no+'" action="/WorkItemProject_comment/workitem/udtWorkItemAction!update.action" >');
		    		    	var span_udt = $('<span title="編輯" class="btn btn_forAction glyphicon glyphicon-edit" onclick="update_check('+ obj.workItem_no +')">');
		    		    	form_update.append(span_udt);
		    		    	
		    		    	// 取消操作的 span
		    		    	var span_cancel = $('<span title="取消編輯" class="btn btn_forAction glyphicon glyphicon-remove-sign" style="display:none" onclick="update_cancel('+obj.workItem_no+')">');
		    		    	
		    		    	// 刪除的form
		    		    	var form_delete = $('<form class="Action_form" id="delete_form" action="/WorkItemProject_comment/workitem/delWorkItemAction!delete.action"  >');
		    		    	var span_del = $('<span title="刪除" id="delete_btn" class="btn btn_forAction glyphicon glyphicon-trash" onclick="delete_check('+obj.workItem_no+')">');
		    		    	var input_del = $('<input id="delete_input" type="hidden" name="workitemVO.workItem_no">');
		    		    	span_del.append(input_del);
		    		    	form_delete.append(span_del);
		    		    	
		    		    	var span_describe = $('<span id="describe"></span>');
		    		    	
		    		    	var td9= $('<td>');
		    		    	td9.append(form_update);
		    		    	td9.append(span_cancel);
		    		    	td9.append(form_delete);
		    		    	td9.append(span_describe);
		    		    	
		    		    	
		    		    	var tr = $("<tr>").attr("id","tr_" + obj.workItem_no).attr("update","false");
		    		    	tr.append(td1);
		    		    	tr.append(td2);
		    		    	tr.append(td3);
		    		    	tr.append(td4);
		    		    	tr.append(td5);
		    		    	tr.append(td6);
		    		    	tr.append(td7);
		    		    	tr.append(td8);
		    		    	tr.append(td9);
		    		    	
		    		    	data_table.append(tr);
		    		    	
						}else{
							range_record = obj.range;
							
		    		    	$("#prev").attr("page", obj.page -1);
		    		    	$("#next").attr("page", obj.page +1);
		    		    	$("#last").attr("page", obj.totalPage);
		    		    	$("#page_span").text(obj.page + "/" + obj.totalPage);
		    		    	
		    		    	$(".page_control").prop("disabled", false); // 都先打開 視情況關
		    		    	
		    		    	if(obj.page == 1){
		    		    		$("#first").prop("disabled", true);
		    		    		$("#prev").prop("disabled", true);
		    		    	}
		    		    	if (obj.page == obj.totalPage){
		    		    		$("#next").prop("disabled", true);
		    		    		$("#last").prop("disabled", true);
		    		    	}
						}   
	    			})
	    			
	    			// 因為 ajax 抓回來的 form 是新長的，所以要在把遮罩功能加回來。但在加之前需要先解綁之前的 submit event;
	    	 		$("form").off( "submit" ).submit(function( event ) {
	    				var loadingDiv = $('<div class="loadingDiv"></div>');
	    				var loadingIcon = $('<div class="lds-default"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>');
	    				var body =  $("body");
	    				loadingDiv.append(loadingIcon);
	    				body.prepend(loadingDiv);
	    	 		});
	    			
	    		},
	    		error : function(response){
	    				
	    				console.log(response);
// 						alert("error");
						alert("連接已失效 !");
						
						window.location.href = "<%=request.getContextPath()%>/login.jsp";
					}
	    	});  
   		     return false;
		});
		
		
		// 側邊欄 ajax
		$(".ul_li li span").click(function(e){
			
			var txt = $(this).text();
			if(txt == "輸出本次計畫" || txt == "輸出整份專案"){
				alert(txt);
				var send_data = "action=" +txt;
					
				$.ajax({
		    		type:"POST",
		    		url:"<%=request.getContextPath()%>/data/DataAction!myDispatcher.action",
		    		data: send_data,
		    		processData: false, 			 
		    		dataType:"text",	 // text格式回傳
		    		
		    		success: function(response,XMLHttpRequest){	
		    			alert(response);
		    		},
		    		error : function(response){
							console.log(response);
							alert("error");
					}
		    	}); 
	   		     return false;
	   		     
			}else if(txt == "繼承舊計劃"){
				$("#upload_select").click();
			}			
		});
		
 		 $('#upload_select').change(function(){
    			$('#upload_submit').trigger('click');  //成功選擇檔案之後 立刻觸發上傳的事件
    	 });
 		 
 		 
 		 // 換大頭貼
 		 $("#faceImg").click(function(e){
 			 $("#photo_select").click();

 		 });
 		 
 		 $('#photo_select').change(function(){
 			$('#photo_submit').trigger('click');  //成功選擇檔案之後 立刻觸發上傳的事件
 		 });
 		 
 		 // 增加 form submit 時的遮罩  >  這一段需要 copy 到 ajax success 中，因為新長出來的 form 沒辦法有遮罩
 		$("form").submit(function( event ) {
			var loadingDiv = $('<div class="loadingDiv"></div>');
			var loadingIcon = $('<div class="lds-default"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>');
			var body =  $("body");
			loadingDiv.append(loadingIcon);
			body.prepend(loadingDiv);
 		});
 		 
	});
		

	function init(){
		
		var inputs = document.getElementsByTagName("input");
		
		for(i = 0 ; inputs[i];i++){  // 可以這樣寫的原因是， for 迴圈第二個放的是 "條件"。 當 inputs[i] 不存在的時候，條件不滿足，就會跳出迴圈
			inputs[i].setAttribute("autocomplete","off");	// 把自動紀錄輸入過的內容關掉
		}
		
		var plan = '${plan_now.plan_name}';
		// 載入後 去按一下 假的a標籤，觸發ajax (此假a標，不帶任何資訊，因此會使用存在於session中的範圍、頁數 -> 若 session 中不存在，則設為1)
		if(plan != undefined)
			$(".date_range_tabs li:eq(0) a").click();
		
		
		setTimeout(function(){
	        robotTyping("Hello!我是小機器人!");	// 2000
	    }, 2000);
		
        setTimeout(function(){
        	robotTyping("您可以輸入特定字元，取得各種服務!");
        }, 4500);
        		
	}

	window.onload = init;

</script>

</head>
<body style="height:100%">


			<div class="col-md-2 sidebar" style="background-color:grey">
				<div class="sidebar">
					<s:form style="padding-bottom : 20px;" action="DataAction!readData" namespace="/data" method="POST" enctype="multipart/form-data" theme="simple">
						<s:file id="photo_select" name="upload" style="display:none"/>
						<s:submit id="photo_submit" value="上傳" style="display:none"/>
					</s:form>
					
					<c:choose>
						<c:when test="${not empty memVO.mem_photo}">
							<div title="點擊更換" id="faceImg" class="circle-avatar" style="background-image:url('data:image/*;base64, ${Base64.getEncoder().encodeToString(memVO.mem_photo)}')">
					
            				</div>
<%-- 							<img id="faceImg" src='data:image/*;base64, ${Base64.getEncoder().encodeToString(memVO.mem_photo)}' class="img-circle"> --%>
						</c:when>
						<c:otherwise>
							<div title="點擊更換" id="faceImg" class="circle-avatar" style="background-image:url(${pageContext.request.contextPath}/img/face.jpeg)">
					
            				</div>
<%-- 							<img id="faceImg" src="${pageContext.request.contextPath}/img/face.jpeg" class="img-circle"> --%>
						</c:otherwise>
					</c:choose>					
					<div class="ul_li">
						<ul>
							<li>Hi, <span>${memVO.mem_name}</span></li>
<%-- 							<li><span>工作項目管理</span></li>  不知道要管理什麼 = = --%>
<%-- 							<li><span>個人資料管理</span></li>  不知道要管理什麼 = = --%>
							<li><span title="將當前計畫輸出為TXT檔">輸出本次計畫</span></li>
							<li><span title="將整份專案的計畫輸出為TXT檔">輸出整份專案</span></li>
							<li>
								<s:form action="DataAction!readData" namespace="/data" method="POST" enctype="multipart/form-data" theme="simple">
								<s:file id="upload_select" name="upload" style="display:none"/>
								<s:submit id="upload_submit" value="上傳" style="display:none"/>
								<span title="選擇符合格式的TXT檔，並自動建立計畫與工作項目">繼承舊計劃</span>
								</s:form>
							</li>
						</ul>
					</div>
					
<!-- 					新增小機器人 -->
					<div class="robot_div">					
						<div id="messagesArea" >
<!-- 							<div class="robotType"></div> -->
						</div>
									
					  <div class="input-group" style="width: 100%;margin-top:-5px;">
					     <input type="text" id="message" class="form-control" placeholder="寄信、統計1、統計2" onkeydown="if (event.keyCode == 13) sendMessage();">
					     <span class="input-group-btn">
					       <button style="margin-right:-1px;" class="btn btn-default" type="button" onclick="sendMessage();">
					       <span class="glyphicon glyphicon-send">
					       </span>
					       </button>
					     </span>
					  </div>
					   
					</div>
				</div>
			</div>
			
			
			<div class="col-md-10 col-offset-2">
<!-- 			按下按鈕的時候，透過修改 form 的 action 達到呼叫不同方法的效果 -->
			<s:form id="plan_form" theme="simple" action="" namespace="/plan">
					<span id="mainName"> 工作項目管理
						<span class="planNow_span">
							<c:if test="${empty plan_now}">
								${errorMsg_plan}
							</c:if>
							${plan_now.plan_name}
						</span>
						<span class="addPlan_span" style="display:none">
							 <s:textfield name="planVO.plan_year" cssClass="work_plan"  value="" required="true" maxlength="2" title="西元年後兩位" label="年分"/>
							 -
							 <s:textfield name="" cssClass="work_plan"  value="" required="true" maxlength="3" title="P + 計畫" label="計畫"/>
							 -
							 <s:textfield name="planVO.plan_start_date" cssClass="work_plan"  value="" required="true" maxlength="4" title="MMDD起始日" label="日期"/>
							 -
							 <s:textfield name="planVO.plan_end_date" placeholder="預設12天後" cssClass="work_plan"  value=""  maxlength="4" title="MMDD結束日" label="日期"/>
							 <s:hidden name="planVO.plan_name"></s:hidden>
							 <s:hidden name="planVO.plan_no"></s:hidden>
						</span>
						<span class="planOperation_span">
						
							<span class="btn plan_btn glyphicon glyphicon-sort"   title="切換現有計畫" onClick="changePlan()" style="display:block;width:1px;" 
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ></span>
							
							<span class="btn plan_btn glyphicon glyphicon-plus"   title="新增一個計畫" onClick="add_plan_check(this)"></span>
							<span class="btn plan_btn glyphicon glyphicon-edit"   title="修改當前計畫" onClick="update_plan_check(this,${plan_now.plan_no})"></span>
							<span class="btn plan_btn glyphicon glyphicon-remove" title="取消目前操作" onClick="cancel_opration(3)"></span>
							
						</span>
					</span>
<!-- 					雖然 下拉式選單的button 完全沒有用到，但不能拿掉，否則會關不起來 -->
					<div class="btn-group dropdown_div" >
					  <button id="dropdownBtn" style="display:none" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" 
					  aria-expanded="false">
					  </button>
					  <ul class="dropdown-menu " style="will-change: transform; position: absolute; transform: translate3d(11px, 28px, 0px); top: 0px; left: 0px;">
   					    <c:forEach var="planVO" items="${plan_list}">
   					    	<c:if test="${planVO.plan_no ne plan_now.plan_no}">
					    		<li><a href="javascript:void(0);" onclick="selectNowPlan(${planVO.plan_no},'${planVO.plan_name}');">${planVO.plan_name}</a></li>
					    	</c:if>
					    </c:forEach>
					    <li role="separator" class="divider"></li>
					    <li><a href="#">當前 - ${plan_now.plan_name}</a></li>
					  </ul>
					</div>
			
<!-- 			18.12.27 重構寫法，以下為舊版 -->
<%-- 				<span id="mainName" >工作項目管理 --%>
<%-- 					 <s:textfield name="planVO.plan_year" cssClass="work_plan"  value="" required="true" maxlength="2" title="西元年後兩位" readonly="readonly"/> --%>
<!-- 					 - -->
<%-- 					 <s:textfield name="planVO." cssClass="work_plan"  value="" required="true" maxlength="3" title="P + 計畫" readonly="readonly"/> --%>
<!-- 					 - -->
<%-- 					 <s:textfield name="planVO.plan_start_date" cssClass="work_plan"  value="" required="true" maxlength="4" title="MMDD起始日" readonly="readonly"/> --%>
<%-- 					 <s:hidden name="planVO.plan_no" value="" required="true"/>
<!-- <!-- 					 <input type="button" id="addplan_btn" value="新增計畫" class="btn btn-info" style="color:brown" onClick="add_plan_check(this)"> --> -->
<!-- <!-- 					 <input type="button" style="display:none" value="取消" class="btn btn-danger" style="color:brown" onClick="add_plan_check(this)" > --> -->
<%-- 				</span>  --%>
<!-- 				應該改成 EL 寫法 -->
			</s:form>
									
			<div class="panel">
			 	<div class="panel-body">
					<div class="data_in">
						<s:form id="add_form" action="addWorkItemAction!add" namespace="/workitem" theme="simple">
						<table class="data_in_table">
							<tr>
								<td style="width:15%">子系統名稱</td>
								<td style="width:20%">需求名稱</td>
								<td style="width:45%">需求內容</td>
								<td style="width:10%">時間(小時)</td>
								<td style="width:10%">日期</td>
							</tr>
							
							<tr class="tr_input">
								<td>
									<s:textfield list="subSys" name="workitemVO.WorkItem_subsys"  required="true"></s:textfield>
									<datalist id="subSys">
										<c:forEach var="subSysVO" items="${subsys_list}">
											<option>${subSysVO.subSys_twName}</option>
										</c:forEach>
									</datalist>
								</td>
								<td>
									<s:textfield name="workitemVO.WorkItem_name" label="需求名稱" required="true"/>
								</td>
								<td>
									<s:textfield name="workitemVO.WorkItem_content" label="需求內容"  required="true"/>
								</td>
								<td>
									<s:textfield name="workitemVO.WorkItem_hour" label="時間(小時)" required="true" id="hour_1"/>
								</td>
								<td>
									<s:textfield data-toggle="tooltip"  data-placement="bottom"  title="請選擇日期!" name="workitemVO.WorkItem_Date" label="日期" id="date_1" />
								</td>
							</tr>
						</table>
						<input type="hidden" name="workitemVO.WorkItem_member" value="${memVO.mem_name}">
						<input type="hidden" name="workitemVO.planVO.plan_no" value="${plan_now.plan_no}">
						
						<div class="info_div">
							<div class="data_div" >
							</div>
						
							<div class="add_div" >
								<s:submit value="新增" onClick="return add_check()" cssClass="btn btn-success"/>
								<input id="modal-caller" onClick="generate()" type="button" value="清單" title="產生當前頁面Jazz格式清單"  Class="btn btn-info" data-toggle="modal" data-target="#exampleModal"/>
								<input onClick="resetParam()" type="button" value="重設"   Class="btn btn-warning"/> 
							</div>	
						</div>
						
	
						</s:form>
					</div>
				</div>
			</div>
			
			  <ul class="nav nav-tabs date_range_tabs">
			    <li style="display:none"><a hide="true" data-toggle="tabajax" ></a></li>
			    <li class="active"><a href="1" data-toggle="tabajax">今日</a></li>
			    <li><a href="2" data-toggle="tabajax" >本周</a></li>
			    <li><a href="3" data-toggle="tabajax" >計畫</a></li>
			  </ul>
				
				<div class="show_data">
					<table style="width:100%" id="show_data_table">
						<tr  style="background:white">
							<th style="width:12%">分類</th>
							<th style="width:13%">名稱</th>
							<th style="width:40%">內容</th>
							<th style="width:10%">時數 <img class="sort_img" id="ts" sort="h0" src="${pageContext.request.contextPath}/img/sort.png"></th>
							<th style="width:10%">日期 <img class="sort_img" id="ds" sort="d0" src="${pageContext.request.contextPath}/img/sort.png"></th>
							<th style="width:15%">行動</th>
						</tr>
						
<!-- 					2019.01.03 改用 ajax 寫法  -->

<%-- 					<jsp:useBean id="work_SvcBean" class="workitem.model.WorkItemService" scope="request"/> --%>
<%-- 					<c:forEach var="workItemVO" items="${plan_now.workItems}" begin="0" end="4"> --%>
<%-- 						<tr id="tr_${workItemVO.workItem_no}"  update="false"> --%>
<%-- 							<td class="normal_td">${workItemVO.workItem_subsys}</td> --%>
<%-- 							<td class="normal_td">${workItemVO.workItem_name }</td> --%>
<%-- 							<td class="normal_td">${workItemVO.workItem_content }</td> --%>
<%-- 							<td class="normal_td">${workItemVO.workItem_hour }</td> --%>
<%-- 							<td class="normal_td">${workItemVO.workItem_Date }</td> --%>
							
<!-- 							<td class="update_td" show="off"> -->
<%-- 								<input name="workitemVO.WorkItem_subsys" form="update_form" value="${workItemVO.workItem_subsys}"> --%>
<!-- 							</td> -->
<!-- 							<td class="update_td" show="off"> -->
<%-- 								<input name="workitemVO.WorkItem_name" form="update_form" value="${workItemVO.workItem_name}"> --%>
<!-- 							</td> -->
<!-- 							<td class="update_td" show="off"> -->
<%-- 								<input name="workitemVO.WorkItem_content" form="update_form" value="${workItemVO.workItem_content}"> --%>
<!-- 							</td> -->
<!-- 							<td class="update_td" show="off"> -->
<%-- 								<input name="workitemVO.WorkItem_hour" form="update_form" value="${workItemVO.workItem_hour}"> --%>
<!-- 							</td> -->
<!-- 							<td class="update_td" show="off"> -->
<%-- 								<input name="workitemVO.WorkItem_Date" form="update_form"  value="${workItemVO.workItem_Date}"> --%>
<!-- 							</td> -->
							
<!-- 							<td> -->
<%-- 								<s:form cssClass="Action_form" id="update_form" action="udtWorkItemAction!update" namespace="/workitem"  method="" theme="simple"> --%>
<%-- 									<s:hidden name="workitemVO.WorkItem_subsys"></s:hidden> --%>
<%-- 									<s:hidden name="workitemVO.WorkItem_name"></s:hidden> --%>
<%-- 									<s:hidden name="workitemVO.WorkItem_content"></s:hidden> --%>
<%-- 									<s:hidden name="workitemVO.WorkItem_hour"></s:hidden> --%>
<%-- 									<s:hidden name="workitemVO.WorkItem_Date"></s:hidden> --%>
<%-- 									<s:hidden name="workitemVO.WorkItem_member"></s:hidden> --%>
<%-- 									<span title="編輯" class="btn btn_forAction glyphicon glyphicon-edit" onclick="update_check(${workItemVO.workItem_no})"></span> --%>
<%-- 								</s:form> --%>
<%-- 								<span title="取消編輯" class="btn btn_forAction glyphicon  glyphicon-remove-sign" style="display:none"  --%>
<%-- 									onclick="update_cancel(${workItemVO.workItem_no})"> --%>
<%-- 								</span> --%>
<%-- 								<s:form cssClass="Action_form" id="delete_form" action="delWorkItemAction!delete" namespace="/workitem"  method="delete" theme="simple"> --%>
<%-- 									<span title="刪除" id="delete_btn" class="btn btn_forAction glyphicon glyphicon-trash" onclick="delete_check(${workItemVO.workItem_no})"></span> --%>
<!-- 									<input id="delete_input" type="hidden" name="workitemVO.WorkItem_no" value=""> -->
<%-- 								</s:form> --%>
<%-- 								<span id="describe"></span> --%>
<!-- 							</td> -->
<!-- 						</tr> -->
<%-- 					</c:forEach> --%>
					</table>
				</div>
			</div>
			
			<div class="page_controller" style="text-align:center;padding-top:5px;">
				
					<button class="btn btn-info page_control" id="first" page="1"         >first</button>
					<button class="btn btn-info page_control" id="prev"  page=""   ><i class="glyphicon glyphicon-chevron-left"></i></button>
					<span id="page_span"></span>
				 	<button class="btn btn-info page_control" id="next"  page=""   ><i class="glyphicon glyphicon-chevron-right"></i></button>
				    <button class="btn btn-info page_control" id="last" page="" >last</button>
				
			</div>
			
			
			

<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title" id="exampleModalLabel">本頁工作項目清單</h3>
      </div>
      <div class="modal-body" >
		
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
			
			
</body>
</html>
<%-- ${ (whichPage == totalPage) ? 'disabled' : ''} --%>
<%-- ${ (whichPage == "1") ? 'disabled' : ''} --%>

<!-- displayTag 不相容於 common-lang-3以上的版本，只能用2.6版，但老師的 jar 包如果改成 2.6 其他部分會出問題，因此本專案不使用 displayTag -->
<%-- 					<display:table name="list" id="testId" length="5"> --%>
<%-- 						<display:column  title="子系統">${testId.subsysVO.SubSys_twName}</display:column>	 --%>
<%-- 						<display:column  title="需求名稱"	property="WorkItem_name" /> --%>
<%-- 						<display:column  title="需求內容"	property="WorkItem_content" /> --%>
<%-- 						<display:column  title="時數"		property="WorkItem_hour" /> --%>
<%-- 						<display:column  title="日期"		property="WorkItem_Date" /> --%>
<%-- 					</display:table> --%>