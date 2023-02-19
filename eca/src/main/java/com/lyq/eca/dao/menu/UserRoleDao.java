package com.lyq.eca.dao.menu;

import com.lyq.eca.pojo.Menu.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleDao extends JpaRepository<UserRole,Integer> {
    List<UserRole> findAllByUid(int uid);
    void deleteAllByUid(int uid);
}
