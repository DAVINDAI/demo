package com.example.demo.domain.mapper;

import com.example.demo.domain.dao.test.Blog;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Component(value = "blogQuery")
public class BlogMappingQuery extends MappingSqlQuery {

    public BlogMappingQuery(DataSource ds){
        super(ds, "select * from Blog where id != ?");

        super.declareParameter(new SqlParameter("id", Types.INTEGER));

        compile();
    }

    @Override
    protected Blog mapRow(ResultSet rs, int rowNum) throws SQLException {
        System.out.println("rowNum: " + rowNum);
        Blog blog = new Blog();
        blog.setId(rs.getInt(1));
        blog.setTitle(rs.getString(2));

        return blog;
    }
}
