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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private UserService userService;

  @Autowired
  private DeliverService deliverService;

  HttpSession session;

  @PostMapping("/login")
  public void login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    session = request.getSession();
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
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    session = request.getSession();
    session.invalidate();
    response.sendRedirect("/login");
  }

  @PostMapping("/signup")
  public boolean signUp(HttpServletRequest request, HttpServletResponse response,
      @RequestBody User signUpUser) {

    if (signUpUser.getName() == null ||
        signUpUser.getName() == "" ||
        signUpUser.getPassword() == null ||
        signUpUser.getPassword() == "") {
      
      session = request.getSession();
      session.setAttribute("signUpError", "帳號或密碼不能為空");
      return false;
    }

    Long latesUserId = userService.getUserLatestId();
    signUpUser.setId(latesUserId);
    userService.save(signUpUser);
    return true;
  }

}
