package com.lyq.eca.service.menu;

import com.lyq.eca.dao.menu.MenuDao;
import com.lyq.eca.pojo.User;
import com.lyq.eca.pojo.Menu.Menu;
import com.lyq.eca.pojo.Menu.RoleMenu;
import com.lyq.eca.pojo.Menu.UserRole;
import com.lyq.eca.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Autowired
    MenuDao menuDao;
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleMenuService roleMenuService;

    public List<Menu> getAllByParentId(int parentId){
        return menuDao.findAllByParentid(parentId);
    }

    public List<Menu> getMenuByCurrentUser(){
        //获取当前用户
        String username= SecurityUtils.getSubject().getPrincipal().toString();
        User user=userService.findByUsername(username);

        List<Integer> rids=userRoleService.listAllByUid(user.getId())
                .stream().map(UserRole::getRid).collect(Collectors.toList());

        List<Integer> menuIds=roleMenuService.findAllByRidIn(rids)
                .stream().map(RoleMenu::getMid).collect(Collectors.toList());

        //注意！！！在role_menu中要写入角色对应的所有的一级和二级菜单才能查出三级菜单
        List<Menu> menus=menuDao.findAllById(menuIds).stream().distinct().collect(Collectors.toList());

        handleMenus(menus);
        return menus;
    }

    public List<Menu> getMenusByRoleId(int rid){
        List<Integer> menuIds=roleMenuService.findAllByRid(rid)
                .stream().map(RoleMenu::getMid).collect(Collectors.toList());
        List<Menu> menus=menuDao.findAllById(menuIds);
        handleMenus(menus);
        return menus;
    }

    private void handleMenus(List<Menu> menus) {
        menus.forEach(m->{
            List<Menu> children=getAllByParentId(m.getId());
            m.setChildren(children);
        });
        menus.removeIf(m->m.getParentid()!=0);
    }

}
