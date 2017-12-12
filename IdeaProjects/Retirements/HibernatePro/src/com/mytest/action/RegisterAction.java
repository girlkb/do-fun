package com.mytest.action;

import com.alibaba.fastjson.JSON;
import com.mytest.json.DBResultJson;
import com.mytest.model.Client;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RegisterAction extends ActionSupport implements
        ServletRequestAware, ServletResponseAware {
    private static final long serialVersionUID = 1L;
    static String INPUT_ERROR = "user name and password must be 6-16,including numbers and letters";

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
     * 注册用户的action-method
     */
    public String register() throws Exception {
        Client client = new Client();

        String result = null;

        request = ServletActionContext.getRequest();
        response = ServletActionContext.getResponse();
        //this.response.setContentType("text/html;charset=utf-8");
        //this.response.setCharacterEncoding("UTF-8");

        String userName = this.request.getParameter("userName");
        String pwd = this.request.getParameter("loginPwd");

        System.out.println(userName + "," + pwd);
        DBResultJson registerResult;
        if (userName == null || pwd == null) {
            registerResult = new DBResultJson("Please input the correct phone number", "false");
        } else {
            registerResult = client.registerUser(userName, pwd);
        }


        //将数据处理成json格式

        String jsonString = JSON.toJSONString(registerResult);
        System.out.println("json String:" + jsonString);

        byte[] jsonBytes = jsonString.getBytes("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.setContentLength(jsonBytes.length);
        response.getOutputStream().write(jsonBytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();

        return result;
    }

}
