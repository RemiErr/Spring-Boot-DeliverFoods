package npu.deliverfoods.api.Model;

public class Deliver {

    private Long deliver_id;
    private String trans_type;
    private String license;

    // Foreign Key
    private Long user_id;

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
        return this.user_id;
    }

    public void setFkUserId(Long uid) {
        this.user_id = uid;
    }

}
