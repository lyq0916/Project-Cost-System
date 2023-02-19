package com.lyq.eca.dao.Cost;

import com.lyq.eca.pojo.Cost.EquipPay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipPayDao extends JpaRepository<EquipPay,Integer> {
    List<EquipPay> findAll();
    List<EquipPay> findByEid(String eid);
    List<EquipPay> findByMid(int mid);
}
