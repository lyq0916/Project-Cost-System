package com.lyq.eca.dao.Cost;

import com.lyq.eca.pojo.Cost.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialDao extends JpaRepository<Material,Integer> {
    List<Material> findAll();
    Material findById(int id);
    Material findByName(String name);
}
