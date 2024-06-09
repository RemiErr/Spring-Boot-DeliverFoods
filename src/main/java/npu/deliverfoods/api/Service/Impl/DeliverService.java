package npu.deliverfoods.api.Service.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import npu.deliverfoods.api.Model.Deliver;
import npu.deliverfoods.api.Service.IService;

@Service
public class DeliverService implements IService<Deliver> {

  @Autowired
  JdbcTemplate jdbcTemplate;

  private static final class DeliverRowMapper implements RowMapper<Deliver> {
    @SuppressWarnings("null")
    public Deliver mapRow(ResultSet rs, int row_num) throws SQLException {
      Deliver deliver = new Deliver();
      deliver.setId(rs.getLong("deliver_id"));
      deliver.setTransType(rs.getString("trans_type"));
      deliver.setLicense(rs.getString("license"));
      deliver.setFkUserId(rs.getLong("user_id"));
      return deliver;
    }
  }

  @Override
  public List<Deliver> findAll() {
    // return jdbcTemplate.query("SELECT * FROM delivers", new
    // BeanPropertyRowMapper<Deliver>(Deliver.class));
    String sql = "SELECT * FROM delivers";
    List<Deliver> delivers = null;

    try {
      delivers = jdbcTemplate.query(sql, new DeliverRowMapper());
    } catch (Exception e) {
      System.err.println("[ERROR] 該資料表為空");
    }

    return delivers;
  }

  @Override
  public Deliver findById(long id) {
    // return jdbcTemplate.queryForObject("SELECT * FROM delivers WHERE
    // delivers_id=?",
    // new DeliverRowMapper(), id);
    String sql = "SELECT * FROM delivers WHERE deliver_id=?";
    Deliver deliver = null;

    try {
      deliver = jdbcTemplate.queryForObject(sql, new DeliverRowMapper(), id);
    } catch (Exception e) {
      System.err.println("[Wrong] 未找到該 Deliver 資料： Deliver Id:" + id);
    }

    return deliver;
  }

  @Override
  public void save(Deliver model) {
    String sql = "INSERT INTO delivers(deliver_id, trans_type, license, user_id) VALUES(?, ?, ?, ?)";
    jdbcTemplate.update(sql, model.getId(), model.getTransType(), model.getLicense(), model.getFkUserId());
  }

  @Override
  public void update(Deliver model) {
    String sql = "UPDATE delivers SET trans_type=?, license=?, user_id=? WHERE deliver_id=?";
    jdbcTemplate.update(sql, model.getTransType(), model.getLicense(), model.getFkUserId(), model.getId());
  }

  @Override
  public void deleteById(long id) {
    String sql = "DELETE * FROM delivers WHERE deliver_id=?";
    jdbcTemplate.update(sql, id);
  }

  public Deliver findByUserId(Long userId) {
    String sql = "SELECT * FROM delivers WHERE user_id=?";
    Deliver deliver = null;
    try {
      deliver = jdbcTemplate.queryForObject(sql, new DeliverRowMapper(), userId);
    } catch (Exception e) {
      // System.err.println("[Wrong] User Id=" + userId + "，該使用者未取得外送員身分！");
      e.getStackTrace();
    }
    return deliver;
  }
}
