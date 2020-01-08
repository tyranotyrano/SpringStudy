import dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import vo.User;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("tyrano");
        user.setName("최영진");
        user.setPassword("암호1");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공!");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getId() + " : " + user2.getName() + " : " + user2.getPassword());
    }
}
