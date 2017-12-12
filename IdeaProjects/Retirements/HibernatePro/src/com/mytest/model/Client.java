package com.mytest.model;

import com.mytest.dao.TCustomerLoginInfoEntity;
import com.mytest.json.DBResultJson;
import com.mytest.util.CheckInputUtil;
import com.mytest.util.MD5Util;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class Client {
    static String INPUT_ERROR="user name and password must be 6-16,including numbers and letters";
    static String USER_REGISTERED="user registered";
    Configuration config = null;
    SessionFactory sessionFactory = null;
    Session session = null;
    Transaction tx = null;

    /**
     * 初始化session
     */
    public void init() {
        config = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
    }

    /**
     * 生成随机id
     *
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }


    /**
     * 注册用户
     *
     * @param userName
     * @param password
     */
    public DBResultJson registerUser(String userName, String password) {
        init();
        try {
            String sql = "select * from user_info.t_customer_login_info  where USER_NAME='" + userName+"'";
            System.out.println(sql);
            SQLQuery sqlQuery = session.createSQLQuery(sql);
            sqlQuery.addEntity(TCustomerLoginInfoEntity.class);

            List<TCustomerLoginInfoEntity> usersEntitys = sqlQuery.list();
            System.out.println(usersEntitys.size());

            if (usersEntitys.size() != 0) {
                System.out.println("用户存在");
                tx.commit();
                session.close();
                //用户存在
                return new DBResultJson(USER_REGISTERED,"false");

            } else {
                if(!CheckInputUtil.checkInput(password)||!CheckInputUtil.checkInput(userName))
                    return new DBResultJson(INPUT_ERROR,"false");
                TCustomerLoginInfoEntity user = new TCustomerLoginInfoEntity();
                String uid=getUUID();
                user.setId(uid);
                user.setUserName(userName);
                user.setIsDeleted(0);
                user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                user.setUpdatedBy("system");
                user.setCreatedBy("system");

                user.setOs("android");
                user.setLoginPwd(MD5Util.convertMD5(MD5Util.string2MD5(password)));

                session.save(user);
                tx.commit();
                session.close();

                //System.out.printf("uid="+uid);
                //成功注册，返回用户id
                return new DBResultJson(uid,"true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }


        return new DBResultJson("","false");
    }


    /**
     * 验证登陆
     *
     * @param userName
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public DBResultJson loginValid(String userName, String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        init();
        //当用户名为主键时可采用该方法
        //TCustomerLoginInfoEntity user = (TCustomerLoginInfoEntity) session.get(TCustomerLoginInfoEntity.class, userName);
        String sql = "select * from user_info.t_customer_login_info  t where t.USER_NAME='" + userName+"'";

        try {
            SQLQuery sqlQuery = session.createSQLQuery(sql);
            sqlQuery.addEntity(TCustomerLoginInfoEntity.class);
            List<TCustomerLoginInfoEntity> usersEntitys = sqlQuery.list();


            session.get(TCustomerLoginInfoEntity.class, userName);

            if (usersEntitys.size() != 0) {// 该用户存在
                TCustomerLoginInfoEntity user = usersEntitys.get(0);

                //存在数据库的密码是经过加密的
                String pwdInDb = user.getLoginPwd();
                String user_id = user.getId();
                tx.commit();
                //解密密码
                if (MD5Util.convertMD5(pwdInDb).equals(MD5Util.string2MD5(password))) {
                    return new DBResultJson(user_id,"true");
                } else {
                    return new DBResultJson("wrong password or user does not exist","false");
                    //System.out.println("密码错误或用户不存在");
                }
            } else {
                return new DBResultJson("user does not exist","false");

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发生异常");
        }
        return new DBResultJson("","false");
    }
}