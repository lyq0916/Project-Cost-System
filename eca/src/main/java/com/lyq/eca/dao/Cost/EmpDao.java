package com.lyq.eca.dao.Cost;

import com.lyq.eca.pojo.Cost.Emp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpDao extends JpaRepository<Emp,Integer> {
    List<Emp> findAll();
    Emp findById(int id);
    List<Emp> findByPid(String pid);
    Emp findByName(String name);
}
