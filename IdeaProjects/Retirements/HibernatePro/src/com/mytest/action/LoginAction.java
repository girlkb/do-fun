package com.mytest.action;


import com.alibaba.fastjson.JSON;
import com.mytest.json.DBResultJson;
import com.mytest.model.Client;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAction extends ActionSupport implements
        ServletRequestAware, ServletResponseAware {
    private static final long serialVersionUID = 2L;

    HttpServletRequest request;
    HttpServletResponse response;

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }


    /**
     * 用户登录的action-method
     */
    public String login() throws Exception {
        Client client=new Client();
        String loginResult=null;
        String result=null;

            //HttpServletRequest request = ServletActionContext.getRequest();
            //HttpServletResponse response = ServletActionContext.getResponse();
            this.response.setContentType("text/html;charset=utf-8");
            this.response.setCharacterEncoding("UTF-8");
            
            String userName = this.request.getParameter("userName");
            String pwd=this.request.getParameter("loginPwd");


            System.out.println(userName+","+pwd);
           if(userName==null||pwd==null){
               result="Please input the correct phone number";
               throw new NullPointerException("input invalid!");
            }




        //将数据处理成json格式

        DBResultJson loginUser = client.loginValid(userName,pwd);

        String jsonString = JSON.toJSONString(loginUser);
        System.out.println("json String:" + jsonString);

        byte[] jsonBytes = jsonString.getBytes("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.setContentLength(jsonBytes.length);
        response.getOutputStream().write(jsonBytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
//       ServletActionContext.getRequest().setAttribute("data", jsonObject.toString());

        //this.response.getWriter().write(result);
        return result;
    }


}