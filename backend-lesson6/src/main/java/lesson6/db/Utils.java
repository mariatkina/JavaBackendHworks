package lesson6.db;

import com.github.javafaker.Faker;
import db.model.Categories;
import db.model.CategoriesExample;
import db.model.Products;
import db.model.ProductsExample;
import lombok.SneakyThrows;
import lombok.With;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

@UtilityClass
public class Utils {


    private static String resource = "mybatis-config.xml";
    static Faker faker = new Faker();


    @SneakyThrows
    private static SqlSession getSqlSession(){

        SqlSessionFactory sqlSessionFactory= new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(resource));
        return sqlSessionFactory.openSession(true);

    }
    @SneakyThrows
    public static db.dao.CategoriesMapper getCategoriesMapper(){
        return getSqlSession().getMapper(db.dao.CategoriesMapper.class);
    }
    @SneakyThrows
    public static db.dao.ProductsMapper getProductsMapper(){
        return getSqlSession().getMapper(db.dao.ProductsMapper.class);
    }
    public static Integer countCategories(db.dao.CategoriesMapper categoriesMapper) {
        long catCount = categoriesMapper.countByExample(new CategoriesExample());
        return Math.toIntExact(catCount);
    }
    public static Integer countProducts(db.dao.ProductsMapper productsMapper) {
        long prodCount = productsMapper.countByExample(new ProductsExample());
        return Math.toIntExact(prodCount);
    }
    public static void createCategory(db.dao.CategoriesMapper categoriesMapper){
        db.model.Categories categories = new Categories();
        categories.setTitle(faker.animal().name());
        categoriesMapper.insert(categories);
    }
    public static void createProduct(db.dao.ProductsMapper productsMapper){
        db.model.Products products = new Products();
        products.setTitle(faker.food().ingredient());
        productsMapper.insert(products);
    }

}
