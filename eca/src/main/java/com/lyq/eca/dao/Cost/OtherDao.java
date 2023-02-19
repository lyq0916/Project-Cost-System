package com.lyq.eca.dao.Cost;

import com.lyq.eca.pojo.Cost.Other;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtherDao extends JpaRepository<Other,Integer> {
    List<Other> findAll();
    List<Other> findByMid(int mid);
}
