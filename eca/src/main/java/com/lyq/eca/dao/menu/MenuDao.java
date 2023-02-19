package com.lyq.eca.dao.menu;

import com.lyq.eca.pojo.Menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDao extends JpaRepository<Menu,Integer> {
    Menu findById(int id);
    List<Menu> findAllByParentid(int parentid);
}
