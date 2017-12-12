import com.mytest.dao.TCustomerLoginInfoEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;


public class DaoTest {
    Configuration config = null;
    SessionFactory sessionFactory = null;
    Session session = null;
    Transaction tx = null;
    @Before
    public void init() {
        config = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
    }
    //增加
    @Test
    public void insert() {
        TCustomerLoginInfoEntity ue = new TCustomerLoginInfoEntity();
        ue.setId("12332424242");
        ue.setUserName("test11");
        ue.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        ue.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ue.setUpdatedBy("system");
        ue.setCreatedBy("system");
        ue.setLoginStatus("test");
        ue.setLoginPwd("123456");
        session.save(ue);
        tx.commit();
    }
    //修改
    @Test
    public void update() {
        TCustomerLoginInfoEntity user = (TCustomerLoginInfoEntity) session.get(TCustomerLoginInfoEntity.class,new String("18814801995"));
        user.setLoginStatus("test");
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        session.update(user);
        tx.commit();
        session.close();
    }
    //查找
    @Test
    public void getById() {
        TCustomerLoginInfoEntity user = (TCustomerLoginInfoEntity) session.get(TCustomerLoginInfoEntity.class, new String("18814801995"));
        tx.commit();
        session.close();
        System.out.println("ID号：" + user.getId() + "；用户名：" + user.getUserName() +
                "；创建时间：" + user.getCreatedAt());
    }



    //删除
    @Test
    public void delete() {
        TCustomerLoginInfoEntity user = (TCustomerLoginInfoEntity) session.get(TCustomerLoginInfoEntity.class, new String("12345678901"));
        session.delete(user);
        tx.commit();
        session.close();
    }

}
