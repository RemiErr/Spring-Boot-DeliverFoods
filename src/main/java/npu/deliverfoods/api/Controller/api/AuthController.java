package npu.deliverfoods.api.Controller.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// 自 Java EE 8 以後，「javax.servlet.http.*」已被遷移至「jakarta.servlet.http.*」
import jakarta.servlet.http.*;
import npu.deliverfoods.api.Model.Deliver;
import npu.deliverfoods.api.Model.User;
import npu.deliverfoods.api.Service.Impl.DeliverService;
import npu.deliverfoods.api.Service.Impl.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private UserService userService;

  @Autowired
  private DeliverService deliverService;

  @PostMapping("/login")
  public void login(User user, HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    HttpSession session = request.getSession();
    User foundUser = userService.findByUsername(user.getName());
    Deliver foundDeliver = null;
    boolean authorized = false;

    // 先確認有該使用者，再驗證資料
    if (foundUser != null) {
      deliverService.findByUserId(foundUser.getId());
      authorized = foundUser.getPassword().equals(user.getPassword());
    }

    // 將參數一起重新導向至對應頁面
    if (authorized) {
      
      session.setAttribute("loggedInUser", foundUser);
      session.setAttribute("loginMessage", user.getName() + " 登入成功");
      session.setAttribute("deliverId", null);
      
      boolean isDeliver = foundDeliver != null;
      if (isDeliver) {

        @SuppressWarnings("null")
        Long deliverId = foundDeliver.getId();

        session.setAttribute("deliverId", deliverId);
        response.sendRedirect("/?deliver=" + deliverId);

      }
      
      response.sendRedirect("/");
    } else {
      session.setAttribute("loginMessage", user.getName() + " 登入失敗");
      response.sendRedirect("/login?error=true");
    }

    // boolean authorized = userService.authorize(user.getName(), user.getPassword());
    
    // if (authorized) {
    //   HttpSession session = request.getSession();
    //   session.setAttribute("user", user);
    //   session.setAttribute("loginMessage", user.getName() + " 成功登入");
    //   response.sendRedirect("/");
    // } else {
    //   response.sendRedirect("/login?error=true");
    // }
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession();
    session.invalidate();
    response.sendRedirect("/login");
  }

}
