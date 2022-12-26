package lesson6Test;

import db.model.CategoriesExample;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoriesTest {

    private static SqlSession session = null;
    private static String resource = "mybatis-config.xml";
    private static InputStream inputStream;
    private static db.model.CategoriesExample example;
    private static db.dao.CategoriesMapper categoriesMapper;
    private static SqlSessionFactory sqlSessionFactory;
    @BeforeAll
    @SneakyThrows
    static void open(){
        inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        categoriesMapper = session.getMapper(db.dao.CategoriesMapper.class);
        example = new db.model.CategoriesExample();
    }
    @Test
    @DisplayName("Вывод количества категорий по ID")
    void countCategories() {
        CategoriesExample example1 = new CategoriesExample();
        example1.createCriteria().andIdEqualTo(1);
        List<db.model.Categories> list = categoriesMapper.selectByExample(example1);
        System.out.println(categoriesMapper.countByExample(example1));

        assertThat(categoriesMapper.countByExample(example1), equalTo(1L));
    }
    @Test
    @DisplayName("Cоздание и удаление категории")
    void createCreateAndDeleteTest(){
        db.model.Categories categories = new db.model.Categories();
        categories.setTitle("test");
        categoriesMapper.insert(categories);
        session.commit();

        assertThat(categories.getTitle(), equalTo("test"));

            categoriesMapper.deleteByPrimaryKey(categories.getId());
            session.commit();
        assertNotEquals(categories.getTitle(), equalTo("test"));

    }
    @Test
    @DisplayName("Cоздание категории")
    @Order(0)
    void createTest(){
        db.model.Categories categories = new db.model.Categories();
        categories.setTitle("Animals");
        categoriesMapper.insert(categories);
        session.commit();
        assertThat(categories.getTitle(), equalTo("Animals"));
    }
    @Test
    @DisplayName("Изменение названия категории")
    @Order(1)
    void updateCategories(){
        example = new db.model.CategoriesExample();
        example.createCriteria().andTitleLike("%nima%");
        List<db.model.Categories> list = categoriesMapper.selectByExample(example);
        db.model.Categories categories2 = list.get(0);
        categories2.setTitle("Cats");
        categoriesMapper.updateByPrimaryKey(categories2);
        session.commit();
        assertThat(categories2.getTitle(), equalTo("Cats"));
    }
    @Test
    @DisplayName("Удаление категории")
    @Order(2)
    void delCategories(){
        example = new db.model.CategoriesExample();
        example.createCriteria().andTitleLike("%ats%");
        List<db.model.Categories> list = categoriesMapper.selectByExample(example);
        db.model.Categories categories2 = list.get(0);
        categoriesMapper.deleteByPrimaryKey(categories2.getId());
        session.commit();

        assertNotEquals(categories2.getTitle(), equalTo("Cats"));
    }
    @AfterAll
    static void close(){
        session.close();
    }
}
