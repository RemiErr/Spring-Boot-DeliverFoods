package npu.deliverfoods.api.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Food {

    @JsonProperty("foodId")
    private Long food_id;
    
    @JsonProperty("name")
    private String food_name;
    
    @JsonProperty("type")
    private String food_type;
    
    @JsonProperty("price")
    private double price;
    
    @JsonProperty("stock")
    private Long stock;

    public Food() {
    }

    public Food(Long fid, String name, String type, double price, Long stock) {
        this.food_id = fid;
        this.food_name = name;
        this.food_type = type;
        this.price = price;
        this.stock = stock;
    }

    // Getter & Setter
    public Long getId() {
        return this.food_id;
    }

    public void setId(Long id) {
        this.food_id = id;
    }

    public String getName() {
        return this.food_name;
    }

    public void setName(String name) {
        this.food_name = name;
    }

    public String getFoodType() {
        return this.food_type;
    }

    public void setFoodType(String type) {
        this.food_type = type;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getStock() {
        return this.stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
}
