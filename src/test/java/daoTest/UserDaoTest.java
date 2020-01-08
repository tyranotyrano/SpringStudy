package daoTest;

import dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import vo.User;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * TDD 주요 3단계
 *  1. arrange : 조건 명시
 *  2. act : 실행
 *  3. assert : 기대결과 확인
 * */

/**
 * 어노테이션 정리
 * 1. @RunWith(SpringJUnit4ClassRunner.class)
 *    - JUnit이 테스트 진행 중에 @Test 메소드가 사용할 ApplicationContext 만들고 관리
 * 2. @ContextConfiguration(locations = "/applicationContext.xml")
 *    - 자동으로 만들어줄 ApplicationContext 의 설정파일 위치 지정
 * 3. @DirtiesContext
 *    - 스프링의 테스트 컨텍스트 프레임워크에게 해당 클래스의 테스트에서 ApplicationContext의 상태를 변경한다는 것을 알려준다.
 *    - 테스트 컨텍스트는 이 어노테이션이 붙은 테스트 클래스에는 ApplicationContext를 공유하지 않는다.
 *      (매번 새로운 ApplicationContext 만들어서 사용)
 *    - 메소드에도 붙일 수 있다. -> 해당 메소드에서만 ApplicationContext를 새로 만들고 메소드 종료 후 폐기.
 * */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@DirtiesContext
public class UserDaoTest {

    /**
     * @Autowired
     *  - 스프링 DI에 사용되는 어노테이션
     *  - 변수 타입과 일치하는 컨텍스트 내의 빈을 찾는다.
     *    타입이 일치하는 빈이 있으면 인스턴스 변수에 주입(타입에 의한 자동와이어링)
     *  ※ ApplicationContext는 초기화할 때 자기 자신도 빈으로 등록한다.
     *     따라서, ApplicationContext 타입의 빈이 존재하는 셈이고 DI도 가능하다.
     *  ※ 같은 타입의 빈이 두 개 이상 있는 경우에는 타입만으로 어떤 빈을 가져올지 결정할 수 없다.
     *     - 변수의 이름과 같은 이름의 빈이 있는지 확인 후 타입/이름 모두 같은 빈을 가져온다.
     *     - 이름으로도 빈을 찾을 수 없는 경우에는 예외가 발생한다.
     * */
    @Autowired
    private UserDao dao;

    private User user1 = null;
    private User user2 = null;
    private User user3 = null;

    /**
     * 모든 @Test 메소드를 실행하기 전에 먼저 실행돼야 하는 메소드를 정의
     * - 테스트를 실행할 때마다 반복되는 준비 작업 처리
     * */
    @Before
    public void setUp() {
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost/spring",
                "root",
                "chldudwls12!",
                true
        );
        dao.setDataSource(dataSource);

        // 픽스처 : 테스트를 수행하는 데 필요한 정보나 오브젝트
        this.user1 = new User("omygirl1", "현승희", "패스워드1");
        this.user2 = new User("omygirl2", "효정", "패스워드2");
        this.user3 = new User("omygirl3", "아린", "패스워드3");
    }

    @Test
    public void addAndGet() throws SQLException {
        // 모든 데이터 삭제
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        // 데이터 추가
        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        // 데이터 조회
        User userGet1 = dao.get(user1.getId());
        assertThat(userGet1.getName(), is(user1.getName()));
        assertThat(userGet1.getPassword(), is(user1.getPassword()));

        User userGet2 = dao.get(user2.getId());
        assertThat(userGet2.getName(), is(user2.getName()));
        assertThat(userGet2.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void count() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    /**
     * @Test(expected = ~~~Exception.class)
     *  - 테스트 중에 발생할 것으로 기대하는 예외 클래스를 지정해준다.
     */
    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_ID");
    }
}
