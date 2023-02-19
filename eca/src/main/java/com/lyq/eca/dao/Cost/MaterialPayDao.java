package com.lyq.eca.dao.Cost;

import com.lyq.eca.pojo.Cost.MaterialPay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialPayDao extends JpaRepository<MaterialPay,Integer> {
    List<MaterialPay> findAll();
    List<MaterialPay> findByMid(int mid);
}
