package com.lyq.eca.controller;

import com.lyq.eca.dao.menu.RoleDao;
import com.lyq.eca.dao.menu.UserRoleDao;
import com.lyq.eca.pojo.UserBean;
import com.lyq.eca.pojo.Menu.UserRole;
import com.lyq.eca.result.Result;
import com.lyq.eca.pojo.Test;
import com.lyq.eca.pojo.User;
import com.lyq.eca.result.ResultFactory;
import com.lyq.eca.service.UserService;
import com.lyq.eca.service.menu.UserRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 　　┏┓　　　┏┓+ +
 * 　┏┛┻━━━┛┻┓ + +
 * 　┃　　　　　　　┃
 * 　┃　　　━　　　┃ ++ + + +
 * ████━████ ┃+
 * 　┃　　　　　　　┃ +
 * 　┃　　　┻　　　┃
 * 　┃　　　　　　　┃ + +
 * 　┗━┓　　　┏━┛
 * 　　　┃　　　┃
 * 　　　┃　　　┃ + + + +
 * 　　　┃　　　┃
 * 　　　┃　　　┃ +  神兽保佑
 * 　　　┃　　　┃    代码无bug
 * 　　　┃　　　┃　　+
 * 　　　┃　 　　┗━━━┓ + +
 * 　　　┃ 　　　　　　　┣┓
 * 　　　┃ 　　　　　　　┏┛
 * 　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　┃┫┫　┃┫┫
 * 　　　　┗┻┛　┗┻┛+ + + +
 */

@RestController
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    UserRoleDao userRoleDao;
    @Autowired
    RoleDao roleDao;

    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser, HttpSession session) {
        //对html标签进行转义，防止xss攻击
        String username = requestUser.getUsername();
       /* username= HtmlUtils.htmlEscape(username);
        User user=userService.get(username,requestUser.getPassword());
        if(null==user){
            return new Result(400);
        }else{
            return new Result(200);
        }*/
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        try {
            subject.login(usernamePasswordToken);
            return ResultFactory.buildSuccessResult(username);
        } catch (Exception e) {
            String message = "账号密码错误";
            return ResultFactory.buildFailResult(message);
        }

    }

    @CrossOrigin
    @PostMapping(value = "api/register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        user.setPhoto("src/assets/img/default.jpg");
        boolean exist = userService.isExist(username);
        if (exist) {
            String message = "用户名已被占用";
            return ResultFactory.buildFailResult(message);
            //return new Result(400,"用户名已被占用");
        }
        //生成盐,默认长度16位
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //设置hash 算法迭代次数
        int times = 2;
        //得到hash后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        //存储用户信息，包括salt与hash后的密码
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);
        //默认注册为普通职员
        userRoleService.RegisterSave(userService.getlast());
        return ResultFactory.buildSuccessResult(user);
    }

    @CrossOrigin
    @PostMapping(value = "api/getusr")
    @ResponseBody
    public User getusr(@RequestBody User requestUser) {
        User user = userService.findByUsername(requestUser.getUsername());
        return user;
    }

    @CrossOrigin
    @GetMapping(value = "api/getUsers")
    @ResponseBody
    public List<UserBean> getUsers() {
        return userService.getUsers();
    }

    @CrossOrigin
    @GetMapping(value = "api/search")
    @ResponseBody
    public List<User> search(@RequestParam("name") String name) {
        return userService.search(name);
    }

    @CrossOrigin
    @GetMapping(value = "api/getRolesById")
    @ResponseBody
    public List<Integer> getRolesById(@RequestParam("id") String id) {
        List<UserRole> userRoles = userRoleDao.findAllByUid(Integer.parseInt(id));
        List<Integer> roles = new ArrayList<>();
        for (int j = 0; j < userRoles.size(); j++) {
            roles.add(userRoles.get(j).getRid());
        }
        return roles;
    }

    @CrossOrigin
    @PostMapping(value = "api/updateUser")
    @ResponseBody
    public Result updateUser(@RequestBody UserBean userBean) {
        //System.out.println(userBean.getRoles());
        try {
            userService.updateUser(userBean);
            return new Result(200);
        } catch (Exception e) {
            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/deleteUser")
    @ResponseBody
    public Result deleteUser(@RequestParam("id") String id) {
        try {
            userService.deleteUser(Integer.parseInt(id));
            return new Result(200);
        }catch (Exception e){
            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/getUsernames")
    @ResponseBody
    public List<Object> getUsernames(){
        return userService.getUsernames();
    }


    @CrossOrigin
    @GetMapping(value = "api/isHeader")
    @ResponseBody
    public Boolean isHeader(@RequestParam("uid")String uid){
        return userRoleService.isHeader(Integer.parseInt(uid));
    }

    @CrossOrigin
    @PostMapping(value = "api/updatephoto")
    @ResponseBody
    public Result updatePhoto(@RequestBody User requestUser) {
        if (userService.updatephotoByUsername(requestUser.getUsername(), requestUser.getPhoto())) {
            return new Result(200);
        } else return new Result(400);
    }

    @CrossOrigin
    @GetMapping(value = "api/test")
    @ResponseBody
    public List<Test> getchartdata() {
        List<Test> t = new ArrayList<>();
        Test t1 = new Test("9998", "长安区");
        Test t2 = new Test("10220", "碑林区");
        t.add(t1);
        t.add(t2);
        return t;
    }

}
