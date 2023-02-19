package com.lyq.eca.dao.Cost;

import com.lyq.eca.pojo.Cost.EmpPay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpPayDao extends JpaRepository<EmpPay,Integer> {
    List<EmpPay> findAll();
    List<EmpPay> findByEid(String eid);
    List<EmpPay> findByMid(int mid);
}
