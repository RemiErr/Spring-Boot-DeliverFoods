package npu.deliverfoods.api.Service.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import npu.deliverfoods.api.Model.OrderItem;
import npu.deliverfoods.api.Service.IService;

@Service
public class ItemService implements IService<OrderItem> {

  @Autowired
  JdbcTemplate jdbcTemplate;

  private static final class OrderItemRowMapper implements RowMapper<OrderItem> {
    @SuppressWarnings("null")
    public OrderItem mapRow(ResultSet rs, int row_num) throws SQLException {
      OrderItem orderItem = new OrderItem();
      orderItem.setQuantity(rs.getInt("quantity"));
      orderItem.setForeignKeys(rs.getLong("order_id"), rs.getLong("food_id"));
      return orderItem;
    }
  }

  @Override
  public List<OrderItem> findAll() {
    String sql = "SELECT * FROM order_items";
    return jdbcTemplate.query(sql, new OrderItemRowMapper());
  }

  @Override
  public OrderItem findById(long id) {
    String error = "※ 請使用 findByFkFoodId()、findByFkOrderId()、findByForignId()";
    System.err.println(error);

    try {
      throw new Exception(error);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void save(OrderItem item) {
    String sql = "INSERT INTO order_items(quantity, order_id, food_id) VALUES(?, ?, ?)";
    jdbcTemplate.update(sql, item.getQuantity(), item.getOrderId(), item.getFoodId());
  }

  @Override
  public void update(OrderItem item) {
    String sql = "UPDATE order_items SET quantity=? WHERE order_id=? AND food_id=?";
    jdbcTemplate.update(sql, item.getQuantity(), item.getOrderId(), item.getFoodId());
  }

  @Override
  public void deleteById(long id) {
    jdbcTemplate.update("DELETE * FROM order_items WHERE order_id=?", id);
    String error = "※ 請使用 deleteByForignId()";
    System.err.println(error);

    try {
      throw new Exception(error);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public OrderItem findByFkFoodId(Long foodId) {
    String sql = "SELETE * FROM order_items WHERE food_id=?";
    OrderItem founOrderItem = null;

    try {
      founOrderItem = jdbcTemplate.queryForObject(sql, new OrderItemRowMapper(), foodId);
    } catch (Exception e) {
      e.getStackTrace();
    }

    return founOrderItem;
  }

  public OrderItem findByFkOrderId(Long orderId) {
    String sql = "SELETE * FROM order_items WHERE order_id=?";
    OrderItem founOrderItem = null;

    try {
      founOrderItem = jdbcTemplate.queryForObject(sql, new OrderItemRowMapper(), orderId);
    } catch (Exception e) {
      e.getStackTrace();
    }

    return founOrderItem;
  }

  public OrderItem findByForignId(Long foodId, Long orderId) {
    String sql = "SELETE * FROM order_items WHERE food_id=? AND order_id=?";
    OrderItem founOrderItem = null;

    try {
      founOrderItem = jdbcTemplate.queryForObject(sql, new OrderItemRowMapper(), foodId, orderId);
    } catch (Exception e) {
      e.getStackTrace();
    }
    return founOrderItem;
  }

  public void deleteByForignId(Long foodId, Long orderId) {
    String sql = "DELETE * FROM order_items WHERE food_id=? AND order_id=?";

    try {
      jdbcTemplate.update(sql, foodId, orderId);
      System.out.println("訂單刪除成功");
    } catch (Exception e) {
      e.getStackTrace();
    }
  }

  public void saveAll(List<OrderItem> items) {
    for (OrderItem item : items) {
      this.save(item);
    }
  }

}
