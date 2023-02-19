package com.lyq.eca.dao;

import com.lyq.eca.pojo.CostMaintain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CostMaintainDao extends JpaRepository<CostMaintain,Integer>{
    CostMaintain findByMid(int mid);
    List<CostMaintain> findByPid(int pid);
    List<CostMaintain> findByUpdatedate(Date updatedate);
    List<CostMaintain> findByPidAndUpdatedate(int pid,Date updatedate);
    List<CostMaintain> findByPidAndType(int pid,String type);

    @Query(value = "select type,sum(money) from cost_maintain where date(update_date)=curdate() group by type;",nativeQuery = true)
    List<Object[]> findByCurdate();

    @Query(value = "select pid,sum(money) from cost_maintain group by pid",nativeQuery = true)
    List<Object[]> costgroupByPid();

    @Query(value = "select type,sum(money) from cost_maintain where pid=?1 group by type;",nativeQuery = true)
    List<Object[]> groupByTypeFroPid(int pid);

    @Query(value = "set @s=0",nativeQuery = true)
    void refresh();

    @Query(value = "select update_date,(@s:=@s+sum) from (select update_date,sum(money) as sum from cost_maintain where pid=1 and type='材料' group by update_date) as a,(select @s:=0) as b",nativeQuery = true)
    List<Object[]> getMsumByDate();

    @Query(value = "SELECT MAX(MID) FROM COST_MAINTAIN",nativeQuery = true)
    int getlast();


}
