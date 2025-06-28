package com.itwray.iw.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwray.iw.web.annotation.IgnorePermission;
import com.itwray.iw.web.model.entity.BaseDictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 字典表 Mapper 接口
 *
 * @author wray
 * @since 2024-05-26
 */
@Mapper
public interface BaseDictMapper extends BaseMapper<BaseDictEntity> {

    /**
     * 通过 dictType + dictName 匹配所有用户的字典项, 执行更新操作
     *
     * @param dictType 修改的字典类型条件
     * @param dictName 修改的字典名称条件
     * @param dto      修改后的数据
     */
    @IgnorePermission
    void updateAllDictByDictName(@Param("dictType") Integer dictType, @Param("dictName") String dictName, @Param("dto") BaseDictEntity dto);
}
