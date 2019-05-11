package login.controller;


import com.alibaba.fastjson.JSON;
import filecenter.service.DeptService;
import filecenter.utils.PanResult;
import filecenter.utils.Utils;
import login.model.Dept;
import login.model.User;
import login.service.LoginService;
import login.utils.CookieUtils;
import login.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "login", method = {RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {

        User user = loginService.getUserinfo(username, password);
        try {
            String accessToken = JWTUtils.createJWT(UUID.randomUUID().toString().replaceAll("-", ""),
                    JSON.toJSONString(user), 30 * 24 * 60 * 60 * 1000);

            if (user != null) {
                CookieUtils.addCookie("token", accessToken);
                CookieUtils.addCookie("uid", user.getUid());
                Utils.saveUser(user);
                //panResult.success(null);
                return "success";
            } else {

                return "wrong";
            }

        } catch (Exception e) {
            // TODO: handle exception
            //panResult.error("用户信息不存在");
            return "error";
        }

    }

    @RequestMapping(value = "userInfo", method = {RequestMethod.GET})
    @ResponseBody
    public PanResult getUserInfo(){
        PanResult panResult=new PanResult();
        User user=Utils.getUser();
        panResult.success(user);
        return panResult;
    }
}
