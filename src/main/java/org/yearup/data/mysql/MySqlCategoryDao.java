package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    @Autowired
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while(resultSet.next()){
                int categoryID = resultSet.getInt(1);
                String categoryName = resultSet.getString(2);
                String description= resultSet.getString(3);
                Category category = new Category(categoryID,categoryName,description);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
       String sql = "SELECT * FROM categories WHERE category_id=?";
       try(Connection connection = getConnection();
       PreparedStatement statement= connection.prepareStatement(sql)){
           statement.setInt(1,categoryId);
           try(ResultSet resultSet = statement.executeQuery()){
               if (resultSet.next()){
                   int categoryID = resultSet.getInt(1);
                   String categoryName = resultSet.getString(2);
                   String description= resultSet.getString(3);
                   Category category = new Category(categoryID,categoryName,description);
                   return category;
               }
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
        return null;
    }

    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO categories (name,description) VALUES (?,?)";

        try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            int affectedRows = statement.executeUpdate();

            if (affectedRows==0){
                throw new SQLException("Creating category failed, no rows effected")
            }

            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    int generatedId = generatedKeys.getInt(1);
                    category.setCategoryId(generatedId);
                } else
                    throw new SQLException("no ID obtained");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
    }

    @Override
    public void delete(int categoryId) {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
