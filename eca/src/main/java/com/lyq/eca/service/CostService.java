package com.lyq.eca.service;

import com.lyq.eca.dao.Cost.*;
import com.lyq.eca.dao.CostMaintainDao;
import com.lyq.eca.pojo.Cost.*;
import com.lyq.eca.pojo.CostMaintain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CostService {
    @Autowired
    EmpDao empDao;
    @Autowired
    EmpPayDao empPayDao;
    @Autowired
    EquipDao equipDao;
    @Autowired
    EquipPayDao equipPayDao;
    @Autowired
    MaterialDao materialDao;
    @Autowired
    MaterialPayDao materialPayDao;
    @Autowired
    OtherDao otherDao;
    @Autowired
    IndirectDao indirectDao;
    @Autowired
    CostMaintainDao costMaintainDao;
    @Autowired
    ProjectService projectService;

    public void addEmp(Emp emp) {
        empDao.save(emp);
    }

    public List<Emp> findAllEmp() {
        return empDao.findAll();
    }

    public Emp findByIdEmp(String eid) {
        return empDao.findById(Integer.parseInt(eid));
    }

    public List<Emp> findByPidEmp(String pid) {
        return empDao.findByPid(pid);
    }

    public void addEmpP(EmpPayBean empPayBean) {
        System.out.println(empPayBean);
        CostMaintain costMaintain = new CostMaintain();
        costMaintain.setType("人工");
        costMaintain.setMoney(empPayBean.getMoney());
        //int pid=projectService.getPidByPname(empPayBean.getProject());
        costMaintain.setPid(Integer.parseInt(empPayBean.getProject()));
        costMaintain.setUpdatedate(empPayBean.getDate());
        costMaintainDao.save(costMaintain);
        EmpPay empPay = new EmpPay();
        //String eid= String.valueOf(empDao.findByName(empPayBean.getEid()).getId());
        empPay.setEid(empPayBean.getEid());
        empPay.setBasic(empPayBean.getBasic().multiply(BigDecimal.valueOf(10000)));
        empPay.setAllowance(empPayBean.getAllowance().multiply(BigDecimal.valueOf(10000)));
        empPay.setPush(empPayBean.getPush().multiply(BigDecimal.valueOf(10000)));
        int mid = costMaintainDao.getlast();
        System.out.println(mid);
        empPay.setMid(mid);
        System.out.println(empPay);
        empPayDao.save(empPay);
    }

    public List<EmpPayBean> findAllEmpPay() {
        return transformEmpPay(empPayDao.findAll());
    }

    public List<EmpPayBean> findByPidEmpP(String pid) {
        List<EmpPay> empPays = new ArrayList<>();
        List<CostMaintain> costMaintains = costMaintainDao.findByPidAndType(Integer.parseInt(pid), "人工");
        for (int i = 0; i < costMaintains.size(); i++) {
            empPays.addAll(empPayDao.findByMid(costMaintains.get(i).getMid()));
        }
        return transformEmpPay(empPays);
    }

    public List<EmpPayBean> transformEmpPay(List<EmpPay> empPays) {
        List<EmpPayBean> empPayBeans = new ArrayList<>();
        for (int i = 0; i < empPays.size(); i++) {
            EmpPayBean empPayBean = new EmpPayBean();
            empPayBean.setId(empPays.get(i).getId());
            String ename = findByIdEmp(empPays.get(i).getEid()).getName();
            empPayBean.setEid(ename);
            empPayBean.setBasic(empPays.get(i).getBasic());
            empPayBean.setAllowance(empPays.get(i).getAllowance());
            empPayBean.setPush(empPays.get(i).getPush());
            CostMaintain costMaintain = costMaintainDao.findByMid(empPays.get(i).getMid());
            empPayBean.setMoney(costMaintain.getMoney());
            String pname = projectService.findByPid(costMaintain.getPid()).getPname();
            empPayBean.setProject(pname);
            empPayBean.setDate(costMaintain.getUpdatedate());
            empPayBeans.add(empPayBean);
        }
        return empPayBeans;
    }

    public void addEquip(Equip equip) {
        equipDao.save(equip);
    }

    public List<Equip> findAllEquip() {
        return equipDao.findAll();
    }

    public Equip findByIdEquip(String eid) {
        return equipDao.findById(Integer.parseInt(eid));
    }

    public List<Equip> findByPidEquip(String pid) {
        return equipDao.findByPid(pid);
    }

    public void addEquipP(EquipPayBean equipPayBean) {
        System.out.println(equipPayBean);
        CostMaintain costMaintain = new CostMaintain();
        costMaintain.setType("机械设备");
        costMaintain.setMoney(equipPayBean.getMoney());
        //int pid= projectService.getPidByPname(equipPayBean.getProject());
        costMaintain.setPid(Integer.parseInt(equipPayBean.getProject()));
        costMaintain.setUpdatedate(equipPayBean.getDate());
        costMaintainDao.save(costMaintain);
        EquipPay equipPay = new EquipPay();
        //String eid= String.valueOf(equipDao.findByName(equipPayBean.getEname()).getId());
        equipPay.setEid(equipPayBean.getEname());
       // equipPay.setEid(String.valueOf(equipDao.getlast()));
        equipPay.setNote(equipPayBean.getNote());
        equipPay.setMid(costMaintainDao.getlast());
        equipPayDao.save(equipPay);
    }

    public List<EquipPayBean> findAllEquipP() {
        return transformEquipPay(equipPayDao.findAll());
    }

    public List<EquipPayBean> findByPidEquipP(String pid) {
        List<EquipPay> equipPays = new ArrayList<>();
        List<CostMaintain> costMaintains = costMaintainDao.findByPidAndType(Integer.parseInt(pid), "机械设备");
        for (int i = 0; i < costMaintains.size(); i++) {
            equipPays.addAll(equipPayDao.findByMid(costMaintains.get(i).getMid()));
        }
        return transformEquipPay(equipPays);
    }

    public List<EquipPayBean> transformEquipPay(List<EquipPay> equipPays) {
        List<EquipPayBean> equipPayBeans = new ArrayList<>();
        for (int i = 0; i < equipPays.size(); i++) {
            EquipPayBean equipPayBean = new EquipPayBean();
            equipPayBean.setId(equipPays.get(i).getId());
            String ename = findByIdEquip(equipPays.get(i).getEid()).getName();
            equipPayBean.setEname(ename);
            equipPayBean.setNote(equipPays.get(i).getNote());
            CostMaintain costMaintain = costMaintainDao.findByMid(equipPays.get(i).getMid());
            equipPayBean.setMoney(costMaintain.getMoney());
            String pname = projectService.findByPid(costMaintain.getPid()).getPname();
            equipPayBean.setProject(pname);
            equipPayBean.setDate(costMaintain.getUpdatedate());
            equipPayBeans.add(equipPayBean);
        }
        return equipPayBeans;
    }

    public void addM(Material material) {
        materialDao.save(material);
    }

    public List<Material> findAllM() {
        return materialDao.findAll();
    }

    public Material findByIdM(String mid) {
        return materialDao.findById(Integer.parseInt(mid));
    }

    public void addMP(MaterialPayBean materialPayBean) {
        CostMaintain costMaintain = new CostMaintain();
        costMaintain.setType("材料");
        costMaintain.setMoney(materialPayBean.getMoney());
        //int pid= projectService.getPidByPname(materialPayBean.getProject());
        costMaintain.setPid(Integer.parseInt(materialPayBean.getProject()));
        costMaintain.setUpdatedate(materialPayBean.getDate());
        costMaintainDao.save(costMaintain);
        MaterialPay materialPay = new MaterialPay();
        materialPay.setMaterialid(materialPayBean.getMaterial());
        materialPay.setSupplier(materialPayBean.getSupplier());
        materialPay.setMid(costMaintainDao.getlast());
        materialPayDao.save(materialPay);
    }

    public List<MaterialPayBean> findAllMP() {
        return transformMP(materialPayDao.findAll());
    }

    public List<MaterialPayBean> findByPidMP(String pid) {
        List<MaterialPay> materialPays = new ArrayList<>();
        List<CostMaintain> costMaintains = costMaintainDao.findByPidAndType(Integer.parseInt(pid), "材料");
        for (int i = 0; i < costMaintains.size(); i++) {
            materialPays.addAll(materialPayDao.findByMid(costMaintains.get(i).getMid()));
        }
        return transformMP(materialPays);
    }

    public List<MaterialPayBean> transformMP(List<MaterialPay> materialPays) {
        List<MaterialPayBean> materialPayBeans = new ArrayList<>();
        for (int i = 0; i < materialPays.size(); i++) {
            MaterialPayBean materialPayBean = new MaterialPayBean();
            materialPayBean.setId(materialPays.get(i).getId());
            String mname = findByIdM(materialPays.get(i).getMaterialid()).getName() + findByIdM(materialPays.get(i).getMaterialid()).getModel();
            materialPayBean.setMaterial(mname);
            materialPayBean.setSupplier(materialPays.get(i).getSupplier());
            CostMaintain costMaintain = costMaintainDao.findByMid(materialPays.get(i).getMid());
            materialPayBean.setMoney(costMaintain.getMoney());
            String pname = projectService.findByPid(costMaintain.getPid()).getPname();
            materialPayBean.setProject(pname);
            materialPayBean.setDate(costMaintain.getUpdatedate());
            materialPayBeans.add(materialPayBean);
        }
        return materialPayBeans;
    }

    public void addOther(OtherBean otherBean) {
        CostMaintain costMaintain=new CostMaintain();
        costMaintain.setType("其他");
        costMaintain.setMoney(otherBean.getMoney());
        costMaintain.setPid(Integer.parseInt(otherBean.getProject()));
        costMaintain.setUpdatedate(otherBean.getDate());
        costMaintainDao.save(costMaintain);
        Other other=new Other();
        other.setName(otherBean.getName());
        other.setNote(otherBean.getNote());
        other.setMid(costMaintainDao.getlast());
        otherDao.save(other);
    }

    public List<OtherBean> findAllO() {
        return transformO(otherDao.findAll());
    }

    public List<OtherBean> findByPidO(String pid) {
        List<Other> others = new ArrayList<>();
        List<CostMaintain> costMaintains = costMaintainDao.findByPidAndType(Integer.parseInt(pid), "其他");
        for (int i = 0; i < costMaintains.size(); i++) {
            others.addAll(otherDao.findByMid(costMaintains.get(i).getMid()));
        }
        return transformO(others);
    }

    public List<OtherBean> transformO(List<Other> others) {
        List<OtherBean> otherBeans = new ArrayList<>();
        for (int i = 0; i < others.size(); i++) {
            OtherBean otherBean = new OtherBean();
            otherBean.setId(others.get(i).getId());
            otherBean.setName(others.get(i).getName());
            otherBean.setNote(others.get(i).getNote());
            CostMaintain costMaintain = costMaintainDao.findByMid(others.get(i).getMid());
            otherBean.setMoney(costMaintain.getMoney());
            String pname = projectService.findByPid(costMaintain.getPid()).getPname();
            otherBean.setProject(pname);
            otherBean.setDate(costMaintain.getUpdatedate());
            otherBeans.add(otherBean);
        }
        return otherBeans;
    }

    public void addIndirect(IndirectBean indirectBean){
        CostMaintain costMaintain=new CostMaintain();
        costMaintain.setType("间接支出");
        costMaintain.setMoney(indirectBean.getMoney());
        costMaintain.setPid(Integer.parseInt(indirectBean.getProject()));
        costMaintain.setUpdatedate(indirectBean.getDate());
        costMaintainDao.save(costMaintain);
        Indirect indirect=new Indirect();
        indirect.setType1(indirectBean.getType1());
        indirect.setType2(indirectBean.getType2());
        indirect.setNote(indirectBean.getNote());
        indirect.setMid(costMaintainDao.getlast());
        indirectDao.save(indirect);
    }

    public List<IndirectBean> findAllI() {
        return transformI(indirectDao.findAll());
    }

    public List<IndirectBean> findByPidI(String pid) {
        List<Indirect> indirects = new ArrayList<>();
        List<CostMaintain> costMaintains = costMaintainDao.findByPidAndType(Integer.parseInt(pid), "间接支出");
        for (int i = 0; i < costMaintains.size(); i++) {
            indirects.addAll(indirectDao.findByMid(costMaintains.get(i).getMid()));
        }
        return transformI(indirects);
    }

    public List<IndirectBean> transformI(List<Indirect> indirects) {
        List<IndirectBean> indirectBeans = new ArrayList<>();
        for (int i = 0; i < indirects.size(); i++) {
            IndirectBean indirectBean = new IndirectBean();
            indirectBean.setId(indirects.get(i).getId());
            indirectBean.setType1(indirects.get(i).getType1());
            indirectBean.setType2(indirects.get(i).getType2());
            indirectBean.setNote(indirects.get(i).getNote());
            CostMaintain costMaintain = costMaintainDao.findByMid(indirects.get(i).getMid());
            indirectBean.setMoney(costMaintain.getMoney());
            String pname = projectService.findByPid(costMaintain.getPid()).getPname();
            indirectBean.setProject(pname);
            indirectBean.setDate(costMaintain.getUpdatedate());
            indirectBeans.add(indirectBean);
        }
        return indirectBeans;
    }
}
