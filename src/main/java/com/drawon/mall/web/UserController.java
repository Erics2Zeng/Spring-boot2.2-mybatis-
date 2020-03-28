package com.drawon.mall.web;

import com.drawon.mall.common.utils.ResponseBo;
import com.drawon.mall.entity.User;
import com.drawon.mall.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
/**
* Created by CodeGenerator on 2020/03/28.
*/
@RestController
@RequestMapping("/user")
public class UserController{

    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    public ResponseBo add(@RequestBody User user) {
         userService.save(user);
         return ResponseBo.ok("");
    }

    @DeleteMapping("/{id}")
    public ResponseBo delete(@PathVariable String id) {
         userService.delete(id);
         return ResponseBo.ok("");
    }

    @PutMapping
    public ResponseBo update(@RequestBody User user) {
         userService.updateAll(user);
         return ResponseBo.ok("");
    }

    @GetMapping("/{id}")
    public ResponseBo detail(@PathVariable String id) {
         User user = userService.selectByPK(id);
         return ResponseBo.ok(user);
    }

    @PostMapping("/list")
    public ResponseBo  list(User user ,int pageNum,int pageSize ) {
         return ResponseBo.ok(this.userService.selectByPageNumSize(pageNum,pageSize,()->Arrays.asList()));
    }

}
