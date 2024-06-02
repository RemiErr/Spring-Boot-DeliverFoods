package npu.deliverfoods.api.Model;

import java.util.Date;

public class Invoice {

    private Long invoice_id;
    private Date create_at;

    // Foreign Key
    private Long order_id;

    public Invoice() {
    }

    public Invoice(Long iid, Date create_at, Long oid) {
        this.invoice_id = iid;
        this.create_at = create_at;
        this.order_id = oid;
    }

    // Getter & Setter
    public Long getInvoiceId() {
        return this.invoice_id;
    }

    public void setInvoiceId(Long id) {
        this.invoice_id = id;
    }

    public Date getCreateAt() {
        return this.create_at;
    }

    public void setCreateAt(Date create_at) {
        this.create_at = create_at;
    }

    public Long getFkOrderId() {
        return this.order_id;
    }

    public void setFkOrderId(Long id) {
        this.order_id = id;
    }

}
