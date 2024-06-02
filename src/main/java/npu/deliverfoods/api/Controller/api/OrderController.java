package npu.deliverfoods.api.Controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

  @Autowired
  private FoodService foodService;

  private HttpSession session;

  // 等待中、外送中、已送達
  private final String[] orderState = { "waiting", "delivering", "arrived" };

  // 以 Json 格式，建立多筆訂單資料
  @PostMapping("/addition")
  public void addItemToOrder(HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "itemId") Long itemId,
      @RequestParam(name = "quantity") int quantity) {

    // 取得新訂單 ID
    Long newOrderId = orderService.getOrderLatestId();
    
    // 取得食物品項
    // Food foundFood = foodService.findById(itemId);

    // 點餐的使用者
    session = request.getSession();
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    
    // 建立資料
    Order newOrder = new Order();
    newOrder.setId(newOrderId);
    newOrder.setState(orderState[0]);
    // newOrder.setForeignKeys(loggedInUser.getId(), null);
    newOrder.setForeignKeys(1L, null);
    
    OrderItem orderItem = new OrderItem();
    orderItem.setQuantity(quantity);
    orderItem.setForeignKeys(newOrderId, itemId);
    
    orderService.save(newOrder);
    itemService.save(orderItem);
  }

}
