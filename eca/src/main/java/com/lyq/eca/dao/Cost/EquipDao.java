package com.lyq.eca.dao.Cost;

import com.lyq.eca.pojo.Cost.Equip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EquipDao extends JpaRepository<Equip,Integer> {
    List<Equip> findAll();
    Equip findById(int eid);
    List<Equip> findByPid(String pid);
    Equip findByName(String name);
    @Query(value = "SELECT MAX(ID) FROM EQUIP",nativeQuery = true)
    int getlast();
}
