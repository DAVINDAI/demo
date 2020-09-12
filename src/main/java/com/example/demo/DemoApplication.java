package com.example.demo;

import com.example.demo.domain.dao.test.Blog;
import com.example.demo.domain.mapper.BlogMappingQuery;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * @author work
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        System.out.println("main启动");
        Object object = new Object();
        System.out.println(object.getClass().getResource("/"));
        // todo 修改日志打印方法
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(DemoApplication.class, args);
        System.out.println("main完成");

        HikariDataSource hikariDataSource = (HikariDataSource)configurableApplicationContext.getBean("testDatasource");
        try {
           Connection connection = hikariDataSource.getConnection();
            System.out.println("获取mybatis线程成功");
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            Connection connection2 = hikariDataSource.getConnection();
            connection2.setAutoCommit(false);
            Statement statement = connection2.createStatement();

            statement.execute("select * from Blog where id=1 for update");

            System.out.println("再次获取mybatis线程成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        MybatisSpringJdbc mybatisSpringJdbc = (MybatisSpringJdbc)configurableApplicationContext.getBean("mybatisSpringJdbc");
        mybatisSpringJdbc.getJdbcTemplate().query("select * from Spring", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.out.println("select * from Spring " + rs.getString(1));
                System.out.println("Spring.title " + rs.getString(2));
            }
        });

        TestSpringJdbc testSpringJdbc = (TestSpringJdbc) configurableApplicationContext.getBean("testSpringJdbc");
        JdbcTemplate jdbcTemplate = testSpringJdbc.getJdbcTemplate();

        jdbcTemplate.execute(new ConnectionCallback<Object>() {
            @Override
            public Object doInConnection(Connection con) throws SQLException, DataAccessException {
                Statement statement = con.createStatement();
                statement.execute("select * from Blog");
                ResultSet resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    System.out.println(resultSet.getString(2));
                }
                System.out.println(resultSet.getClass().getName());
                return resultSet;
            }
        });


        jdbcTemplate.execute("select * from Blog where id=?", new CallableStatementCallback<Object>() {
            @Override
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                cs.setInt(1, 1);
                cs.execute();
                ResultSet resultSet = cs.getResultSet();


                while (resultSet.next()) {
                    System.out.println(resultSet.getString(2));
                }
                System.out.println(resultSet.getClass().getName());

                return null;
            }
        });

        jdbcTemplate.query("select * from Blog", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.out.println(rs.getString(1));

                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            }
        });
        DataSource dataSource = jdbcTemplate.getDataSource();

        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement("select title from Blog where id =1");
                return preparedStatement;
            }
        }, new ResultSetExtractor<Object>() {
            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
                return null;
            }
        });

        BlogMappingQuery blogMappingQuery = (BlogMappingQuery) configurableApplicationContext.getBean("blogQuery");
        Object[] params = new Object[1];
        params[0] = 2;
        List blogs = blogMappingQuery.execute(params);

        for (Object blog :blogs) {
            Blog tmp = (Blog)blog;
            System.out.println(tmp.getTitle());
        }

    }
}

@Component
class TestSpringJdbc {
    @Resource(name = "testJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}

@Component
class MybatisSpringJdbc {
    @Resource(name = "mybatisJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}

