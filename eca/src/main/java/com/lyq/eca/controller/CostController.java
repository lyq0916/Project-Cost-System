package com.lyq.eca.controller;

import com.lyq.eca.dao.Cost.EquipDao;
import com.lyq.eca.pojo.Cost.*;
import com.lyq.eca.result.Result;
import com.lyq.eca.service.CostService;
import com.lyq.eca.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CostController {
    @Autowired
    CostService costService;
    @Autowired
    ProjectService projectService;
    @Autowired
    EquipDao equipDao;

    @CrossOrigin
    @PostMapping(value = "api/addEmp")
    @ResponseBody
    public Result addEmp(@RequestBody Emp emp){
        try{
            costService.addEmp(emp);
            return new Result(200);
        }catch (Exception e){
            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/getEmp")
    @ResponseBody
    public List<Emp> getemp() {
        List<Emp> emps = costService.findAllEmp();
        for (int i = 0; i < emps.size(); i++) {
            String pname = projectService.findByPid(Integer.parseInt(emps.get(i).getPid())).getPname();
            emps.get(i).setPid(pname);
        }
        return emps;
    }

    @CrossOrigin
    @GetMapping(value = "api/getEmpByPid")
    @ResponseBody
    public List<Emp> getEmpByPid(@RequestParam("pid") String pid) {
        List<Emp> emps = costService.findByPidEmp(pid);
        for (int i = 0; i < emps.size(); i++) {
            String pname = projectService.findByPid(Integer.parseInt(emps.get(i).getPid())).getPname();
            emps.get(i).setPid(pname);
        }
        return emps;
    }

    @CrossOrigin
    @PostMapping(value = "api/addEmpPay")
    @ResponseBody
    public Result addEmpPay(@RequestBody EmpPayBean empPayBean) {
        try {
            costService.addEmpP(empPayBean);
            return new Result(200);
        } catch (Exception e) {
            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/getEmpPay")
    @ResponseBody
    public List<EmpPayBean> getEmpPay() {
        return costService.findAllEmpPay();
    }

    @CrossOrigin
    @GetMapping(value = "api/getEmpPayByPid")
    @ResponseBody
    public List<EmpPayBean> getEmpPayByPid(@RequestParam("pid") String pid) {
        return costService.findByPidEmpP(pid);
    }


    @CrossOrigin
    @PostMapping(value = "api/addEquip")
    @ResponseBody
    public Result addEquip(@RequestBody Equip equip){
        try{
            //System.out.println(equip);
            costService.addEquip(equip);
            EquipPayBean equipPayBean=new EquipPayBean();
            equipPayBean.setEname(String.valueOf(equipDao.getlast()));
            //System.out.println(String.valueOf(equipDao.getlast()));
            equipPayBean.setNote("购买设备"+equip.getName());
            equipPayBean.setMoney(equip.getMoney());
            equipPayBean.setProject(equip.getPid());
            equipPayBean.setDate(equip.getBuy());
            System.out.println(equipPayBean);
            costService.addEquipP(equipPayBean);
            return new Result(200);
        }catch (Exception e){

            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/getEquip")
    @ResponseBody
    public List<Equip> getEquip() {
        List<Equip> equips = costService.findAllEquip();
        for (int i = 0; i < equips.size(); i++) {
            String pname = projectService.findByPid(Integer.parseInt(equips.get(i).getPid())).getPname();
            equips.get(i).setPid(pname);
        }
        return equips;
    }

    @CrossOrigin
    @GetMapping(value = "api/getEquipByPid")
    @ResponseBody
    public List<Equip> getEquipByPid(@RequestParam("pid") String pid) {
        List<Equip> equips = costService.findByPidEquip(pid);
        for (int i = 0; i < equips.size(); i++) {
            String pname = projectService.findByPid(Integer.parseInt(equips.get(i).getPid())).getPname();
            equips.get(i).setPid(pname);
        }
        return equips;
    }

    @CrossOrigin
    @PostMapping(value = "api/addEquipPay")
    @ResponseBody
    public Result addEquipPay(@RequestBody EquipPayBean equipPayBean){
        try {
            costService.addEquipP(equipPayBean);
            return new Result(200);
        }catch (Exception e){
            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/getEquipPay")
    @ResponseBody
    public List<EquipPayBean> getEquipPay() {
        return costService.findAllEquipP();
    }

    @CrossOrigin
    @GetMapping(value = "api/getEquipPayByPid")
    @ResponseBody
    public List<EquipPayBean> getEquipPayByPid(@RequestParam("pid") String pid) {
        return costService.findByPidEquipP(pid);
    }


    @CrossOrigin
    @PostMapping(value = "api/addMaterial")
    @ResponseBody
    public Result addMaterial(@RequestBody Material material){
        try{
            costService.addM(material);
            return new Result(200);
        }catch (Exception e){
            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/getMaterial")
    @ResponseBody
    public List<Material> getMaterial() {
        return costService.findAllM();
    }

    @CrossOrigin
    @PostMapping(value = "api/addMaterialPay")
    @ResponseBody
    public Result addMaterialPay(@RequestBody MaterialPayBean materialPayBean){
        try{
            costService.addMP(materialPayBean);
            return new Result(200);
        }catch (Exception e){
            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/getMaterialPay")
    @ResponseBody
    public List<MaterialPayBean> getMaterialPay() {
        return costService.findAllMP();
    }

    @CrossOrigin
    @GetMapping(value = "api/getMaterialPayByPid")
    @ResponseBody
    public List<MaterialPayBean> getMaterialPayByPid(@RequestParam("pid") String pid) {
        return costService.findByPidMP(pid);
    }

    @CrossOrigin
    @PostMapping(value = "api/addOther")
    @ResponseBody
    public Result addOther(@RequestBody OtherBean otherBean){
        try{
            costService.addOther(otherBean);
            return new Result(200);
        }catch (Exception e){
            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/getOther")
    @ResponseBody
    public List<OtherBean> getOther() {
        return costService.findAllO();
    }

    @CrossOrigin
    @GetMapping(value = "api/getOtherByPid")
    @ResponseBody
    public List<OtherBean> getOtherByPid(@RequestParam("pid") String pid) {
        return costService.findByPidO(pid);
    }

    @CrossOrigin
    @PostMapping(value = "api/addIndirect")
    @ResponseBody
    public Result addIndirect(@RequestBody IndirectBean indirectBean){
        try{
            costService.addIndirect(indirectBean);
            return new Result(200);
        }catch (Exception e){
            return new Result(400);
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/getIndirect")
    @ResponseBody
    public List<IndirectBean> getIndirect() {
        return costService.findAllI();
    }

    @CrossOrigin
    @GetMapping(value = "api/getIndirectByPid")
    @ResponseBody
    public List<IndirectBean> getIndirectByPid(@RequestParam("pid") String pid) {
        return costService.findByPidI(pid);
    }

}
