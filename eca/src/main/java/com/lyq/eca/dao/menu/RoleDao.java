package com.lyq.eca.dao.menu;

import com.lyq.eca.pojo.Menu.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role,Integer> {
    Role findById(int id);
}
