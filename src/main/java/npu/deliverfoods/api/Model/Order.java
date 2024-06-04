package npu.deliverfoods.api.Model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {

    @JsonProperty("orderId")
    private Long order_id;

    @JsonProperty("state")
    private String state;
    
    // Foreign Keys
    @JsonProperty("userId")
    private Long user_id;
    
    @JsonProperty("deliverId")
    private Long deliver_id;

    public Order() {
    }

    public Order(Long oid, String state, Long uid, Long did) {
        this.order_id = oid;
        this.state = state;
        this.user_id = uid;
        this.deliver_id = did;
    }

    // Getter & Setter
    public Long getId() {
        return this.order_id;
    }

    public void setId(Long id) {
        this.order_id = id;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Long> getForeignKeys() {
        return Arrays.asList(user_id, order_id);
    }

    public void setForeignKeys(List<Long> keys) {
        this.user_id = keys.get(0);
        this.order_id = keys.get(1);
    }

    public void setForeignKeys(Long uid, Long did) {
        this.user_id = uid;
        this.deliver_id = did;
    }

    public Long getFkUserId() {
        return this.user_id;
    }

    public void setFkUserId(Long id) {
        this.user_id = id;
    }

    public Long getFkDeliverId() {
        return this.deliver_id;
    }

    public void setFkDeliverId(Long id) {
        this.deliver_id = id;
    }

}
