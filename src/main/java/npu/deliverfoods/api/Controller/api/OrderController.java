package npu.deliverfoods.api.Controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import npu.deliverfoods.api.Model.*;
import npu.deliverfoods.api.Service.Impl.DeliverService;
import npu.deliverfoods.api.Service.Impl.FoodService;
import npu.deliverfoods.api.Service.Impl.ItemService;
import npu.deliverfoods.api.Service.Impl.OrderService;

/*
  前台透過 JavaScript 將資料 fetch 成 json 格式
  接收前台請求
*/

@RestController
@RequestMapping("/api/order")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @Autowired
  private ItemService itemService;

  @Autowired
  private FoodService foodService;

  private HttpSession session;

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

  // 建立訂單、關係 (Json)
  @PostMapping("/addition")
  public void addItemsToOrder(HttpServletRequest request,
      @RequestBody Map<String, List<StructFood>> foodsMap) {

    // 取得新訂單 ID
    Long newOrderId = orderService.getOrderLatestId();

    // 訂單中所有餐點
    List<StructFood> foods = foodsMap.get("foods");

    // 當前使用者
    session = request.getSession();
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    Long loggedInUserId = loggedInUser.getId();

    // 建立訂單資料
    Order newOrder = new Order();
    newOrder.setId(newOrderId);
    newOrder.setState(eS.等待中.get());
    newOrder.setForeignKeys(loggedInUserId, null);

    // 可能有多個品項
    boolean orderSafety = false;
    for (StructFood food : foods) {
      int foodQuantity = food.getQuantity();
      OrderItem orderItem = new OrderItem();
      orderItem.setQuantity(foodQuantity);
      orderItem.setForeignKeys(newOrderId, food.getFoodId());

      // 更新庫存
      Food foundFood = foodService.findById(food.getFoodId());
      Long stock = foundFood.getStock();

      // 庫存 - 訂單餐點數
      if (stock >= foodQuantity) {
        foundFood.setStock(stock - foodQuantity);
        foodService.update(foundFood);
        itemService.save(orderItem);
        orderSafety = true;
      } else {
        System.out.println("[Wrong] Lacking stock, " + stock + " are left.");
      }
    }

    if (orderSafety) {
      orderService.save(newOrder);
    } else {
      System.out.println("[Wrong] 沒有任何餐點，訂單不成立。");
    }
    
  }

  // 移除品項
  @PostMapping("/remove")
  public void removeItemFromOrder(HttpServletRequest request,
      @RequestParam(name = "orderId") Long orderId,
      @RequestParam(name = "foodId") Long foodId) {

    OrderItem orderItem = itemService.findByForignId(foodId, orderId);
    itemService.deleteByObject(orderItem);
  }

  // 修改品項數量
  @PostMapping("/edit")
  public void editItemFromOrder(HttpServletRequest request,
      @RequestBody Map<String, List<OrderItem>> foodsMap) {

    List<OrderItem> foods = foodsMap.get("foods");
    itemService.updateAll(foods);
  }

  // 接單
  @PostMapping("/pickup")
  public void pickOrder(@RequestBody Order order,
      HttpServletRequest request,
      @RequestParam(name = "deliverId") Long deliverId) {

    // session = request.getSession();
    // session.setAttribute("pickedDeliver", deliverService.findById(deliverId));

    // 更新資料
    order.setFkDeliverId(deliverId);
    order.setState(eS.外送中.get());
    orderService.update(order);
  }

  // 完成訂單
  @PostMapping("/complate")
  public void complateOrder(@RequestParam(name = "orderId") Long orderId,
      HttpServletRequest request) throws Exception {

    Order foundOrder = orderService.findById(orderId);

    foundOrder.setState(eS.已送達.get());
    orderService.update(foundOrder);
  }

}

// 儲存資料的結構體
class StructFood {
  private Long foodId;
  private int quantity;

  public Long getFoodId() {
    return foodId;
  }

  public void setFoodId(Long foodId) {
    this.foodId = foodId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
