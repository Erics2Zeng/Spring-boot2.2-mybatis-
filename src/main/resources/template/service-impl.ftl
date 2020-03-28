package ${currentPackage}.service.impl;
import ${currentPackage}.mapper.${modelNameUpperCamel}Mapper;
import ${currentPackage}.entity.${modelNameUpperCamel};
import ${currentPackage}.service.PageService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;



/**
 * Created by ${author} on ${date}.
 */
@Slf4j
@Service
public class ${modelNameUpperCamel}ServiceImpl  extends PageService {


    @Autowired
    private ${modelNameUpperCamel}Mapper mapper;


    public ${modelNameUpperCamel} selectByPK(String key) {
        return mapper.selectByPrimaryKey(key);
    }


    public int save(${modelNameUpperCamel} entity) {
        return mapper.insert(entity);
    }

    public int saveNotNull(${modelNameUpperCamel} entity) {
        return mapper.insertSelective(entity);
    }

    public int delete(String key) {
        return mapper.deleteByPrimaryKey(key);
    }

    public int updateAll(${modelNameUpperCamel} entity) {
      return mapper.updateByPrimaryKey(entity);
    }

    public int updateNotNull(${modelNameUpperCamel} entity) {
    return mapper.updateByPrimaryKeySelective(entity);
    }

}
