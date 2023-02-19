package com.lyq.eca.dao.menu;

import com.lyq.eca.pojo.Menu.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleMenuDao extends JpaRepository<RoleMenu,Integer> {
    List<RoleMenu> findAllByRid(int rid);
    List<RoleMenu> findAllByRidIn(List<Integer> rids);
    void deleteAllByRid(int rid);
}
