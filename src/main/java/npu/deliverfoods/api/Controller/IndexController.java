package npu.deliverfoods.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.*;
import npu.deliverfoods.api.Model.*;
import npu.deliverfoods.api.Service.Impl.DeliverService;
import npu.deliverfoods.api.Service.Impl.FoodService;
import npu.deliverfoods.api.Service.Impl.ItemService;
import npu.deliverfoods.api.Service.Impl.OrderService;
import npu.deliverfoods.api.Service.Impl.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 當要回傳的資料需要打包成 Json 或 XML 格式，使用「@RestController」
// 如果要回傳的是要給模板引擎(Thymeleaf)的頁面，使用「@Controller」
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeliverService deliverService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private ItemService itemService;

    private User loggedInUser;

    private HttpSession session;

    // 首頁 (登入/登出)
    @GetMapping({ "/", "/index" })
    public String index(HttpServletRequest request, Model model) {

        session = request.getSession();
        loggedInUser = (User) session.getAttribute("loggedInUser");

        // 如果未登入，導向登入頁面
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // 打包要回傳的資料
        User foundUser = userService.findById(loggedInUser.getId());
        model.addAttribute("user", foundUser);

        Deliver foundDeliver = deliverService.findByUserId(loggedInUser.getId());
        if (foundDeliver != null) {
            model.addAttribute("deliver", foundDeliver);
        }

        List<Order> foundAllOrder = orderService.findByUserIdForList(loggedInUser.getId());
        model.addAttribute("orders", foundAllOrder);

        model.addAttribute("foods", foodService.findAll());

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 外送接單後台
    @GetMapping("/pickup")
    public String pickUP(HttpServletRequest request, Model model) {

        session = request.getSession();
        loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // 阻擋非外送員的使用者
        Deliver foundDeliver = deliverService.findByUserId(loggedInUser.getId());
        if (foundDeliver == null) {
            return "redirect:/";
        }

        // 登入中的使用者
        User foundUser = userService.findById(loggedInUser.getId());
        model.addAttribute("user", foundUser);

        model = getAllOrderDetail(model, orderService.findAllWaitingOrder());

        return "pick";
    }

    // 使用者後台
    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {

        session = request.getSession();
        loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User foundUser = userService.findById(loggedInUser.getId());
        model.addAttribute("user", foundUser);

        // 外送中清單
        model = getAllOrderDetail(model, orderService.findAllDeliveringOrder());
        // 已送達清單
        model = getAllOrderDetail(model, orderService.findAllArrivedOrder());

        return "dashboard";
    }

    // 購物車
    @GetMapping("/cart")
    public String cart(HttpServletRequest request, Model model) {

        session = request.getSession();
        loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User foundUser = userService.findById(loggedInUser.getId());
        if (foundUser == null)
            return "redirect:/login";

        // 當前使用者所有訂單
        List<Order> foundAllOrder = orderService.findByUserIdForList(loggedInUser.getId());
        model = getAllOrderDetail(model, foundAllOrder);

        
        return "cart";
    }



    // 要顯示訂單狀態、內含品項，打包成 Model
    public Model getAllOrderDetail(Model model, List<Order> allOrder) {
      // 防止空陣列
      if (allOrder == null || allOrder.isEmpty()) {
        return model;
      }

      List<ItemDeteil> itemDeteilList = new ArrayList<>();                  // 品項清單
      List<OrderDetail> orderDetailList = new ArrayList<>();                // 詳細資料
      Map<String, List<OrderDetail>> data = new HashMap<>();
      List<Food> allFood = foodService.findAll();
      
      for (Order order : allOrder) {
        Long foundDeliverId = 0L;
        Deliver foundDeliver = null;

        try {
          foundDeliverId = orderService.getDeliverIdByOrderId(order.getId());
          foundDeliver = deliverService.findById(foundDeliverId);
        } catch (Exception e) {
          e.getStackTrace();
        }


        // 打包所有餐點品項資料
        for (Food food : allFood) {
          if (itemService.checkRelation(food.getId(), order.getId())) {             
            ItemDeteil itemDeteil = new ItemDeteil();
            itemDeteil.setFoodId(food.getId());
            itemDeteil.setFoodName(food.getName());
            itemDeteil.setFoodType(food.getFoodType());
            itemDeteil.setPrice(food.getPrice());

            OrderItem foundOrderItem = itemService.findByForignId(food.getId(), order.getId());
            itemDeteil.setQuantity(foundOrderItem.getQuantity());
            itemDeteilList.add(itemDeteil);
          }
        }

        // 中間層
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(order.getId());
        if (foundDeliver != null) {
          orderDetail.setDeliverName(userService.getNameByOrderIdAndDeliverId(order.getId(), foundDeliver.getId()));
          orderDetail.setDeliverId(foundDeliver.getId());
        } else {
          orderDetail.setDeliverName(null);
          orderDetail.setDeliverId(null);
        }
        orderDetail.setItemDeteilList(itemDeteilList);
        orderDetailList.add(orderDetail);
        data.put(order.getState(), orderDetailList);
      }

      model.addAttribute("data", data);
      return model;
    }


    // 前台顯示的訂單結構
    class OrderDetail {
        Long orderId;
        Map<String, List<ItemDeteil>> itemDeteilList;
        Long deliverId;
        String deliverName;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
          this.orderId = orderId;
        }

        public List<ItemDeteil> getItemDeteilList() {
            return itemDeteilList.get("items");
        }

        public List<ItemDeteil> getItemDeteilList(String itemKey) {
            return itemDeteilList.get(itemKey);
        }
        
        public void setItemDeteilList(List<ItemDeteil> itemDeteilList) {
          Map<String, List<ItemDeteil>> itemDeteilMap = new HashMap<>();
          itemDeteilMap.put("items", itemDeteilList);
          this.itemDeteilList = itemDeteilMap;
        }

        public Long getDeliverId() {
          return deliverId;
        }

        public void setDeliverId(Long deliverId) {
          this.deliverId = deliverId;
        }
        
        public void setDeliverId() {
          this.deliverId = null;
        }

        public String getDeliverName() {
          return deliverName;
        }

        public void setDeliverName(String deliverName) {
          this.deliverName = deliverName;
        }
        
        public void setDeliverName() {
          this.deliverName = null;
        }
    }

    class ItemDeteil {
        Long foodId;
        String foodName;
        String foodType;
        double price;
        int quantity;

        public Long getFoodId() {
          return foodId;
        }

        public void setFoodId(Long foodId) {
          this.foodId = foodId;
        }

        public String getFoodName() {
          return foodName;
        }

        public void setFoodName(String foodName) {
          this.foodName = foodName;
        }

        public String getFoodType() {
          return foodType;
        }

        public void setFoodType(String foodType) {
          this.foodType = foodType;
        }

        public double getPrice() {
          return price;
        }

        public void setPrice(double price) {
          this.price = price;
        }

        public int getQuantity() {
          return quantity;
        }

        public void setQuantity(int quantity) {
          this.quantity = quantity;
        }
    }
}

/* 
{
  "wait": [
    {
      "orderId": 1,
      "items":
      [
        {
            "foodId": 1,
            "foodName": "Cookie",
            "foodType": "dessert",
            "price": 50,
            "quantity": 5
        }
      ],
      "deliverId": 2,
      "deliverName": "ABC"
    }
  ]
}
*/