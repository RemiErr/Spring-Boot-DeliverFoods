package npu.deliverfoods.api.Controller.api;

import java.io.IOException;
import java.util.List;

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

  HttpSession session;

  @PostMapping("/login")
  public void login(HttpServletRequest request, HttpServletResponse response, @RequestBody User user)
      throws IOException {

    session = request.getSession();
    User foundUser = userService.findByUsername(user.getName());
    Deliver foundDeliver = null;
    boolean authorized = false;

    // 先確認有該使用者，再驗證資料
    if (foundUser != null) {
      foundDeliver = deliverService.findByUserId(foundUser.getId());
      authorized = foundUser.getPassword().equals(user.getPassword());
    } else {
      session.setAttribute("loginMessage", user.getName() + "不存在的帳號");
    }

    // 將參數一起重新導向至對應頁面
    if (authorized) {

      session.setAttribute("loggedInUser", foundUser);
      session.setAttribute("loginMessage", user.getName() + " 登入成功");
      session.setAttribute("deliverId", null);
      
      boolean isDeliver = foundDeliver != null;
      session.setAttribute("isDeliver", isDeliver);
      if (isDeliver) {
        
        @SuppressWarnings("null")
        Long deliverId = foundDeliver.getId();
        
        session.setAttribute("deliverId", deliverId);
      }

      response.sendRedirect("/");
    } else {
      session.setAttribute("loginMessage", user.getName() + " 登入失敗");
      response.sendRedirect("/login");
    }
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    session = request.getSession();
    session.invalidate();
    response.sendRedirect("/login");
    return;
  }

  @PostMapping("/signup")
  public void signUp(HttpServletRequest request, HttpServletResponse response,
      @RequestBody User signUpUser) {

    session = request.getSession();

    if (signUpUser.getName() == null ||
        signUpUser.getName() == "" ||
        signUpUser.getPassword() == null ||
        signUpUser.getPassword() == "") {

      session.setAttribute("signUpErrorMessage", "帳號或密碼不能為空");
      return;
    }

    // 檢查是否重複帳號
    List<User> allUser = userService.findAll();
    String signUpUserName = signUpUser.getName();
    for (User user : allUser) {
      if (user.getName().equals(signUpUserName)) {
        session.setAttribute("signUpErrorMessage", "帳號已存在");
        System.out.println("[Error] 已有存在的帳號 User-Name=" + user.getName());
        return;
      }
    }

    // 生成帳號
    Long latesUserId = userService.getLatesUsertId();
    signUpUser.setId(latesUserId);
    userService.save(signUpUser);
  }

  @DeleteMapping("/remove")
  public void removeUser(HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "user", required = true) Long userId)
      throws IOException {

    session = request.getSession();
    User foundUser = userService.findById(userId);
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
      response.sendRedirect("/login");
    } else if (foundUser == null) {
      System.out.println("[Wrong] 查無該使用者 UserId=" + userId);
    } else if (foundUser.getId() != loggedInUser.getId()) {
      System.out.println("[Error] 無法刪除其他使用者！");
    } else {
      userService.deleteById(userId);
      session = request.getSession();
      session.invalidate();
      System.out.println("已成功刪除 UserId=" + userId);
    }
  }

  @PutMapping("/edit")
  public void editUser(HttpServletRequest request, HttpServletResponse response,
      @RequestParam(name = "user", required = true) Long userId,
      @RequestBody User editedUser)
      throws IOException {

    session = request.getSession();
    User foundUser = userService.findById(userId);
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
      response.sendRedirect("/login");
    } else if (foundUser == null) {
      System.out.println("[Wrong] 查無該使用者 UserId=" + userId);
    } else if (foundUser.getId() != loggedInUser.getId()) {
      System.out.println("[Error] 無法修改其他使用者資料！");
    } else {
      foundUser.setName(editedUser.getName());
      foundUser.setEmail(editedUser.getEmail());
      foundUser.setPhone(editedUser.getPhone());
      foundUser.setAddress(editedUser.getAddress());
      userService.update(foundUser);
      session = request.getSession();
      session.invalidate();
      System.out.println("已成功修改 UserId=" + userId);
    }
  }

}
