package npu.deliverfoods.api.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Deliver {

    // Database Columns
    @JsonProperty("deliverId")
    private Long deliver_id;

    @JsonProperty("type")
    private String trans_type;

    @JsonProperty("license")
    private String license;

    // Foreign Key
    @JsonProperty("userId")
    private Long userId;

    public Deliver() {
    }

    public Deliver(Long did, String type, String license) {
        this.deliver_id = did;
        this.trans_type = type;
        this.license = license;
    }

    // Getter & Setter
    public Long getId() {
        return this.deliver_id;
    }

    public void setId(long id) {
        this.deliver_id = id;
    }

    public String getTransType() {
        return this.trans_type;
    }

    public void setTransType(String type) {
        this.trans_type = type;
    }

    public String getLicense() {
        return this.license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Long getFkUserId() {
        return this.userId;
    }

    public void setFkUserId(Long uid) {
        this.userId = uid;
    }

}
