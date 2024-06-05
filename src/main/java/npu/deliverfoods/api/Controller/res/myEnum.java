package npu.deliverfoods.api.Controller.res;

public class myEnum {
  // 等待中、外送中、已送達
  public enum eS {
    等待中("waiting"),
    外送中("delivering"),
    已送達("arrived");

    private String orderState;

    eS(String state) {
      this.orderState = state;
    }

    public String get() {
      return this.orderState;
    }
  };
}
