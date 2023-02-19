package com.lyq.eca.service.menu;

import com.lyq.eca.dao.menu.UserRoleDao;
import com.lyq.eca.pojo.Menu.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleService {
    @Autowired
    UserRoleDao userRoleDao;

    public List<UserRole> listAllByUid(int uid){
        return userRoleDao.findAllByUid(uid);
    }

    public Boolean isHeader(int uid){
        List<UserRole> list=userRoleDao.findAllByUid(uid);
        for(UserRole userRole:list){
            if(userRole.getRid()==2){
                return true;
            }
        }
        return false;
    }

    public void RegisterSave(int uid){
        UserRole userRole=new UserRole();
        userRole.setUid(uid);
        userRole.setRid(4);
        userRoleDao.save(userRole);
    }

    @Transactional
    public void saveRoleChanges(int uid,List<Integer> roles){
        userRoleDao.deleteAllByUid(uid);
        List<UserRole> urs=new ArrayList<>();
        roles.forEach(r->{
            UserRole ur=new UserRole();
            ur.setUid(uid);
            ur.setRid(r);
            urs.add(ur);
        });
        userRoleDao.saveAll(urs);
    }




}
