package npu.deliverfoods.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.*;
import npu.deliverfoods.api.Model.*;
import npu.deliverfoods.api.Service.Impl.FoodService;
import npu.deliverfoods.api.Service.Impl.OrderService;
import npu.deliverfoods.api.Service.Impl.UserService;

import java.util.List;

// 當要回傳的資料需要打包成 Json 或 XML 格式，使用「@RestController」
// 如果要回傳的是要給模板引擎(Thymeleaf)的頁面，使用「@Controller」
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FoodService foodService;

    private User loggedInUser;

    private HttpSession session;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
            @RequestParam(name = "deliver", required = false) Long deliverId) {

        session = request.getSession();
        loggedInUser = (User) session.getAttribute("loggedInUser");

        // 如果未登入，導向登入頁面
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // 打包要回傳的資料
        model.addAttribute("deliverId", deliverId);
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        List<Food> foods = foodService.findAll();
        model.addAttribute("foods", foods);

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String index() {
        // 如果未登入，導向登入頁面
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("orders", orderService.findAll());

        return "dashboard";
    }

}