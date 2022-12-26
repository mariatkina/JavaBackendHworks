package lesson6Test;

import db.model.Products;
import db.model.ProductsExample;
import lesson6.db.Utils;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTests {
    long id;
    Products products;
    private static SqlSession session = null;
    private static String resource = "mybatis-config.xml";
    private static InputStream inputStream;
    private static ProductsExample example;
    private static db.dao.ProductsMapper productsMapper;
    private static SqlSessionFactory sqlSessionFactory;

    @BeforeAll
    @SneakyThrows
    static void beforeAll(){
        inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        productsMapper = session.getMapper(db.dao.ProductsMapper.class);
        example = new ProductsExample();

    }
    @Test
    @Order(1)
    @DisplayName("Проверка изменеия количества продуктов после создания и удаления 1экз")
    void countProductsCreateAndDeleteTest(){
        Integer countProductsBefore = Utils.countProducts(productsMapper);
        products = new db.model.Products();
        products.setTitle("test");
        productsMapper.insert(products);
        Integer countProductsAfterCreate = Utils.countProducts(productsMapper);

        assertThat(countProductsAfterCreate, equalTo(countProductsBefore+1));

        productsMapper.deleteByPrimaryKey(products.getId());
        Integer countProductsAfterDelete = Utils.countProducts(productsMapper);

        assertThat(countProductsAfterDelete, equalTo(countProductsBefore));
    }
    @Test
    @DisplayName("Cоздание 1экз продукта")
    @Order(2)
    void createProduct(){
        products = new db.model.Products();
        products.setTitle("Butter");
        products.setCategory_id(1L);
        products.setPrice(23);
        productsMapper.insert(products);
        session.commit();
        id = Math.toIntExact(products.getId());

        assertThat(products.getTitle(), equalTo("Butter"));
        assertThat(products.getPrice(), equalTo(23));

    }
    @Test
    @DisplayName("Проверка изменения цены продукта")
    @Order(3)
    void updateProduct(){
        example = new ProductsExample();
        example.createCriteria().andPriceLessThan(24);
        List<Products> list = productsMapper.selectByExample(example);
        Products products1 = list.get(0);
        products1.setPrice(350);
        productsMapper.updateByPrimaryKey(products1.getId());
        session.commit();
        assertThat(products1.getPrice(), equalTo(350));
    }
    @Test
    @DisplayName("Удаление 1экз продукта")
    @Order(4)
    void deleteProduct(){
        example = new ProductsExample();
        example.createCriteria().andTitleLike("Butter");
        List<Products> list = productsMapper.selectByExample(example);
        products = list.get(0);
        productsMapper.deleteByPrimaryKey(products.getId());
        session.commit();
        assertFalse(list.contains(example));
    }

    @AfterAll
    static void close(){
        session.close();
    }

}
