package npu.deliverfoods.api.Controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import npu.deliverfoods.api.Model.*;
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

  private HttpSession session;

  // 等待中、外送中、已送達
  private final String[] orderState = { "waiting", "delivering", "arrived" };

  // 以 Json 格式建立資料
  @PostMapping("/addition")
  public void addFoodsToOrder(HttpServletRequest request, HttpServletResponse response,
      @RequestBody Map<String, StructFood[]> foodsMap) {

    // 取得新訂單 ID
    Long newOrderId = orderService.getOrderLatestId();

    // 所有品項
    StructFood[] foods = foodsMap.get("foods");

    // 當前使用者
    session = request.getSession();
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    // 建立訂單資料
    Order newOrder = new Order();
    newOrder.setId(newOrderId);
    newOrder.setState(orderState[0]);
    // newOrder.setForeignKeys(loggedInUser.getId(), null);
    newOrder.setForeignKeys(1L, null);

    orderService.save(newOrder);

    // 可能有多個品項
    for (StructFood food : foods) {
      OrderItem orderItem = new OrderItem();
      orderItem.setQuantity(food.getQuantity());
      orderItem.setForeignKeys(newOrderId, food.getFoodId());

      itemService.save(orderItem);
    }
  }

  @PostMapping("/remove")
  public void removeItemFromOrder(HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "orderId") Long orderId,
      @RequestParam(name = "foodId") Long foodId){
      
      OrderItem orderItem = itemService.findByForignId(foodId, orderId);
      itemService.deleteByObject(orderItem);
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

/* Json 格式如下：
{
  "foods":
  [
    {
      "foodId": 1,
      "quantity": 11
    },
    {
      "foodId": 2,
      "quantity": 45
    },
    {
      "foodId": 3,
      "quantity": 14
    }
  ]
}
 */