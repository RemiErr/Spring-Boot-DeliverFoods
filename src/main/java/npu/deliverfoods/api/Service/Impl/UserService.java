package npu.deliverfoods.api.Service.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
            return user;
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public User findById(long id) {
        String sql = "SELECT * FROM users WHERE user_id=?";
        User user = null;

        try {
            jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        } catch (Exception e) {
            System.err.println("[Wrong] 未找到該 User 資料： User Id:" + id);
        }
        return user;

        // try {
        //     return Optional.ofNullable(user);
        // } catch (Exception e) {
        //     return Optional.empty();
        // }
    }

    @Override
    public void save(User model) {
        jdbcTemplate.update(
            "INSERT INTO users(user_id, user_name, email, phone, address, password) VALUES(?, ?, ?, ?, ?, ?)", 
            model.getId(), model.getName(), model.getEmail(), model.getPhone(), model.getAddress(), model.getPassword());
    }

    @Override
    public void update(User model) {
        jdbcTemplate.update(
            "UPDATE users SET user_name=?, email=?, phone=?, address=?, password=? WHERE user_id=?",
            model.getName(), model.getEmail(), model.getPhone(), model.getAddress(), model.getPassword(), model.getId());
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("DELETE * FROM users WHERE user_id=?", id);
    }

    // 可能找到同名用戶，所以回傳使用 List
    // public List<User> findByUsername(String user_name) {
    //     String sql = "SELECT * FROM users WHERE user_name=?";
    //     return jdbcTemplate.query(sql, new UserRowMapper(), user_name);
    // }
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

    // public boolean authorize(String user_name, String password) {
    //     List<User> users = findByUsername(user_name);
    //     for (User user : users) {
    //         if (user.getPassword().equals(password))
    //             return true;
    //     }
    //     return false;
    // }

}
