package npu.deliverfoods.api.Service.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import npu.deliverfoods.api.Model.Order;
import npu.deliverfoods.api.Service.IService;

@Service
public class OrderService implements IService<Order> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final class OrderRowMapper implements RowMapper<Order> {
        @SuppressWarnings("null")
        public Order mapRow(ResultSet rs, int row_num) throws SQLException {
            Order order = new Order();
            order.setId(rs.getLong("order_id"));
            order.setState(rs.getString("state"));
            order.setForeignKeys(rs.getLong("user_id"), rs.getLong("deliver_id"));
            return order;
        }
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query("SELECT * FROM orders", new OrderRowMapper());
    }

    @Override
    public Order findById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM orders WHERE user_id=?", new OrderRowMapper(), id);
    }

    @Override
    public void save(Order order) {
        jdbcTemplate.update(
                "INSERT INTO orders(order_id, state, user_id, deliver_id) VALUES(?, ?, ?, ?)",
                order.getId(), order.getState(), order.getFkUserId(), order.getFkDeliverId());
    }

    @Override
    public void update(Order order) {
        jdbcTemplate.update(
                "UPDATE orders SET stat=?, user_id=?, deliver_id=? WHERE order_id=?",
                order.getState(), order.getFkUserId(), order.getFkDeliverId());
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("DELETE * FROM orders WHERE order_id=?", id);
    }

    public void updateOrderState(Long orderId) {
        String sql = "UPDATE orders SET stat=? WHERE order_id=?";
        jdbcTemplate.update(sql, orderId);
    }

    public void saveAll(List<Order> orders) {
        for (Order order : orders) {
            this.save(order);
        }
    }

    @SuppressWarnings("null")
    public Long getOrderLatestId() {
        String sql = "SELECT TOP 1 order_id FROM orders ORDER BY order_id DESC";
        Long latesId = 1L;

        try {
            latesId += jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            e.getStackTrace();
        }

        return latesId;
    }

}
