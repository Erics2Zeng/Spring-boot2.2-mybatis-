package ${currentPackage}.web;
import ${currentPackage}.common.utils.ResponseBo;
import ${currentPackage}.entity.${modelNameUpperCamel};
import ${currentPackage}.service.impl.${modelNameUpperCamel}ServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Map;
/**
* Created by ${author} on ${date}.
*/
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller{

    @Autowired
    private ${modelNameUpperCamel}ServiceImpl ${modelNameLowerCamel}Service;

    @PostMapping
    public ResponseBo add(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
         ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
         return ResponseBo.ok("");
    }

    @DeleteMapping("/{id}")
    public ResponseBo delete(@PathVariable String id) {
         ${modelNameLowerCamel}Service.delete(id);
         return ResponseBo.ok("");
    }

    @PutMapping
    public ResponseBo update(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
         ${modelNameLowerCamel}Service.updateAll(${modelNameLowerCamel});
         return ResponseBo.ok("");
    }

    @GetMapping("/{id}")
    public ResponseBo detail(@PathVariable String id) {
         ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.selectByPK(id);
         return ResponseBo.ok(${modelNameLowerCamel});
    }

    @PostMapping("/list")
    public ResponseBo  list(${modelNameUpperCamel} ${modelNameLowerCamel} ,int pageNum,int pageSize ) {
         return ResponseBo.ok(this.${modelNameLowerCamel}Service.selectByPageNumSize(pageNum,pageSize,()->Arrays.asList()));
    }

}
