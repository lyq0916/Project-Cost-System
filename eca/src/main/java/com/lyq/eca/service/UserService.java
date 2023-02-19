package com.lyq.eca.service;

import com.lyq.eca.dao.UserDao;
import com.lyq.eca.dao.menu.RoleDao;
import com.lyq.eca.dao.menu.UserRoleDao;
import com.lyq.eca.pojo.User;
import com.lyq.eca.pojo.UserBean;
import com.lyq.eca.pojo.Menu.UserRole;
import com.lyq.eca.service.menu.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    UserRoleDao userRoleDao;
    @Autowired
    RoleDao roleDao;
    @Autowired
    UserRoleService userRoleService;

    public List<UserBean> getUsers(){
        List<UserBean> userBeans=new ArrayList<>();
        List<User> users=userDao.findAll();
        for(int i=0;i<users.size();i++){
            UserBean userBean=new UserBean();
            userBean.setId(users.get(i).getId());
            userBean.setUsername(users.get(i).getUsername());
            List<UserRole> userRoles=userRoleDao.findAllByUid(users.get(i).getId());
            List<Integer> roles=new ArrayList<>();
            for(int j=0;j<userRoles.size();j++){
                //String role=roleDao.findById(userRoles.get(j).getRid()).getNamezh();
                roles.add(userRoles.get(j).getRid());
            }
            userBean.setRoles(roles);
            userBeans.add(userBean);
        }
        //System.out.println(userBeans);
        return userBeans;
    }

    public void updateUser(UserBean userBean){
        userRoleService.saveRoleChanges(userBean.getId(),userBean.getRoles());
        userDao.updateUser(userBean.getUsername(),userBean.getId());
    }

    @Transactional
    public void deleteUser(int id){
        userRoleDao.deleteAllByUid(id);
        userDao.deleteById(id);
    }

    public List<User> search(String name){
        return userDao.search(name);
    }

    public boolean isExist(String username) {
        User user = getByName(username);
        return null != user;
    }

    public User getByName(String username) {
        return userDao.findByUsername(username);
    }

    public User get(String username, String password) {
        return userDao.getByUsernameAndPassword(username, password);
    }

    public Integer getlast(){return userDao.getlast();};

    public void add(User user) {
        userDao.save(user);
    }

    public User findByUsername(String username){
        return userDao.findByUsername(username);
    }

    public List<Object> getUsernames(){
        return userDao.getUsernames();
    }

    public boolean updatephotoByUsername(String username, String photo) {
        User user=userDao.findByUsername(username);
        user.setPhoto(photo);
        try {
            userDao.save(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}