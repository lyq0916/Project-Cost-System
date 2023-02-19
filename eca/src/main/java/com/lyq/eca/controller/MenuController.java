package com.lyq.eca.controller;

import com.lyq.eca.pojo.Menu.Menu;
import com.lyq.eca.service.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MenuController {
    @Autowired
    MenuService menuService;

    @CrossOrigin
    @GetMapping(value = "/api/menu")
    @ResponseBody
    public List<Menu> menu(){
        return menuService.getMenuByCurrentUser();
    }
}
