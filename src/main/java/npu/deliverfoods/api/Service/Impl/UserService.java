package npu.deliverfoods.api.Service.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import npu.deliverfoods.api.Model.User;
import npu.deliverfoods.api.Service.IService;

@Service
public class UserService implements IService<User> {

  @Autowired
  JdbcTemplate jdbcTemplate;

  private static final class UserRowMapper implements RowMapper<User> {
    @SuppressWarnings("null")
    public User mapRow(ResultSet rs, int row_num) throws SQLException {
      User user = new User();
      user.setId(rs.getLong("user_id"));
      user.setName(rs.getString("user_name"));
      user.setPassword(rs.getString("password"));
      user.setEmail(rs.getString("email"));
      user.setAddress(rs.getString("address"));
      user.setPhone(rs.getString("phone"));
      return user;
    }
  }

  @Override
  public List<User> findAll() {
    // return jdbcTemplate.query(sql, new UserRowMapper());
    String sql = "SELECT * FROM users";
    List<User> users = null;

        try {
          users = jdbcTemplate.query(sql, new UserRowMapper());
        } catch (Exception e) {
            System.err.println("[ERROR] 該資料表為空");
        }

        return users;
  }

  @Override
  public User findById(long id) {
    String sql = "SELECT * FROM users WHERE user_id=?";
    User user = null;

    try {
      user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    } catch (Exception e) {
      System.out.println("[Wrong] 未找到該 User 資料： User Id:" + id);
    }
    return user;
  }

  @Override
  public void save(User model) {
    String sql = "INSERT INTO users(user_id, user_name, email, phone, address, password) VALUES(?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql, model.getId(), model.getName(), model.getEmail(), model.getPhone(), model.getAddress(), model.getPassword());
  }

  @Override
  public void update(User model) {
    String sql = "UPDATE users SET user_name=?, email=?, phone=?, address=?, password=? WHERE user_id=?";
    jdbcTemplate.update(sql, model.getName(), model.getEmail(), model.getPhone(), model.getAddress(), model.getPassword(), model.getId());
  }

  @Override
  public void deleteById(long id) {
    String sql = "DELETE FROM users WHERE user_id=?";
    jdbcTemplate.update(sql, id);
  }

  // 可能找到同名用戶，所以回傳使用 List
  public User findByUsername(String user_name) {
    String sql = "SELECT * FROM users WHERE user_name=?";
    User user = null;

    try {
      user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), user_name);
    } catch (Exception e) {
      System.err.println("[Wrong] 未找到該 User 資料： User Name:" + user_name);
    }
    return user;
  }

  @SuppressWarnings("null")
  public Long getLatesUsertId() {
    String sql = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
    Long latesId = 1L;

    try {
      latesId += jdbcTemplate.queryForObject(sql, Long.class);
    } catch (Exception e) {
      e.getStackTrace();
    }

    return latesId;
  }

  public String getNameByOrderIdAndDeliverId(Long oid, Long did) {
    String sql = "SELECT user_name FROM users, orders, delivers " + 
                 "WHERE (delivers.user_id = users.user_id) " + 
                 "AND (delivers.deliver_id = orders.deliver_id) " + 
                 "AND orders.order_id=? AND delivers.deliver_id=?";
    return jdbcTemplate.queryForObject(sql, String.class, oid, did);
  }

}
