package npu.deliverfoods.api.Model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItem {

    @JsonProperty("quantity")
    private int quantity;
    
    // Foreign Keys
    @JsonProperty("orderId")
    private Long order_id;

    @JsonProperty("foodId")
    private Long food_id;

    public OrderItem() {
    }

    public OrderItem(int quantity, Long oid, Long fid) {
        this.quantity = quantity;
        this.order_id = oid;
        this.food_id = fid;
    }

    // Getter & Setter
    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Long> getForeignKeys() {
        return Arrays.asList(this.order_id, this.food_id);
    }

    public void setForeignKeys(List<Long> keys) {
        this.order_id = keys.get(0);
        this.food_id = keys.get(1);
    }

    public void setForeignKeys(Long oid, Long fid) {
        this.order_id = oid;
        this.food_id = fid;
    }

    public Long getOrderId() {
        return this.order_id;
    }

    public Long getFoodId() {
        return this.food_id;
    }

}
