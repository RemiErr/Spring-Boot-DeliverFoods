package npu.deliverfoods.api.Controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import npu.deliverfoods.api.Model.*;
import npu.deliverfoods.api.Service.Impl.FoodService;
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
  private FoodService foodService;

  private HttpSession session;

  // 撈出訂單資料
  @PostMapping("/addition")
  public void addItemToOrder(HttpServletRequest request, HttpServletResponse response,
      @RequestBody Map<String, List<Food>> itemsMap) {

    // 以 Json 格式，一次建立多筆訂單資料
    List<Food> foods = itemsMap.get("foods");
    foodService.saveAll(foods);
  }



}
