package npu.deliverfoods.api.Service.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import npu.deliverfoods.api.Model.Food;
import npu.deliverfoods.api.Service.IService;

@Service
public class FoodService implements IService<Food> {
  @Autowired
  JdbcTemplate jdbcTemplate;

  private static final class FoodRowMapper implements RowMapper<Food> {
    @SuppressWarnings("null")
    public Food mapRow(ResultSet rs, int row_num) throws SQLException {
      Food food = new Food();
      food.setId(rs.getLong("food_id"));
      food.setName(rs.getString("food_name"));
      food.setFoodType(rs.getString("food_type"));
      food.setPrice(rs.getDouble("price"));
      food.setStock(rs.getLong("stock"));
      return food;
    }
  }

  @Override
  public List<Food> findAll() {
    // return jdbcTemplate.query(sql, new FoodRowMapper());
    String sql = "SELECT * FROM foods";
    List<Food> foods = null;

    try {
      foods = jdbcTemplate.query(sql, new FoodRowMapper());
    } catch (Exception e) {
      System.err.println("[ERROR] 該資料表為空");
      ;
    }
    return foods;
  }

  @Override
  public Food findById(long id) {
    String sql = "SELECT * FROM foods WHERE food_id=?";
    Food founFood = null;

    try {
      founFood = jdbcTemplate.queryForObject(sql, new FoodRowMapper(), id);
    } catch (Exception e) {
      e.getStackTrace();
    }

    return founFood;
  }

  @Override
  public void save(Food food) {
    String sql = "INSERT INTO foods(food_id, food_name, food_type, price, stock) VALUES(?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, food.getId(), food.getName(), food.getFoodType(), food.getPrice(), food.getStock());
  }

  @Override
  public void update(Food food) {
    String sql = "UPDATE foods SET food_name=?, food_type=?, price=?, stock=? WHERE food_id=?";
    jdbcTemplate.update(sql, food.getName(), food.getFoodType(), food.getPrice(), food.getStock(), food.getId());
  }

  @Override
  public void deleteById(long id) {
    String sql = "DELETE * FROM foods WHERE food_id=?";
    jdbcTemplate.update(sql, id);
  }

  public void saveAll(List<Food> foods) {
    try {
      for (Food food : foods) {
        this.save(food);
      }
    } catch (Exception e) {
      e.getStackTrace();
    }
  }

}
