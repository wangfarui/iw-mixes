package com.itwray.iw.web.dao;

import cn.hutool.core.collection.CollUtil;
import com.itwray.iw.web.config.IwAliyunProperties;
import com.itwray.iw.web.mapper.BaseBusinessFileMapper;
import com.itwray.iw.web.model.dto.FileDto;
import com.itwray.iw.web.model.entity.BaseBusinessFileEntity;
import com.itwray.iw.web.model.enums.BusinessFileTypeEnum;
import com.itwray.iw.web.model.vo.FileVo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 业务文件关联表 DAO
 *
 * @author wray
 * @since 2025-04-23
 */
@Component
public class BaseBusinessFileDao extends BaseDao<BaseBusinessFileMapper, BaseBusinessFileEntity> {

    private IwAliyunProperties iwAliyunProperties;

    /**
     * 更新保存业务文件关联信息
     *
     * @param businessId           业务id
     * @param businessFileTypeEnum 业务文件类型
     * @param fileList             新保存的文件列表
     */
    public void saveBusinessFile(Integer businessId, BusinessFileTypeEnum businessFileTypeEnum, List<FileDto> fileList) {
        // 首先删除历史关联信息
        this.removeBusinessFile(businessId, businessFileTypeEnum);
        // 新增关联信息
        if (CollUtil.isNotEmpty(fileList)) {
            List<BaseBusinessFileEntity> entityList = fileList.stream()
                    .map(t -> {
                        BaseBusinessFileEntity entity = new BaseBusinessFileEntity();
                        entity.setBusinessId(businessId);
                        entity.setBusinessType(businessFileTypeEnum);
                        entity.setFileName(t.getFileName());
                        entity.setFileUrl(t.getFileUrl());
                        return entity;
                    }).toList();
            this.saveBatch(entityList);
        }
    }

    /**
     * 删除业务文件关联信息
     *
     * @param businessId           业务id
     * @param businessFileTypeEnum 业务文件类型
     */
    public void removeBusinessFile(Integer businessId, BusinessFileTypeEnum businessFileTypeEnum) {
        this.lambdaUpdate()
                .eq(BaseBusinessFileEntity::getBusinessType, businessFileTypeEnum)
                .eq(BaseBusinessFileEntity::getBusinessId, businessId)
                .remove();
    }

    /**
     * 获取业务文件关联信息
     *
     * @param businessId           业务id
     * @param businessFileTypeEnum 业务文件类型
     * @return 业务文件关联信息
     */
    public List<FileVo> getBusinessFile(Integer businessId, BusinessFileTypeEnum businessFileTypeEnum) {
        List<BaseBusinessFileEntity> entityList = this.lambdaQuery()
                .eq(BaseBusinessFileEntity::getBusinessType, businessFileTypeEnum)
                .eq(BaseBusinessFileEntity::getBusinessId, businessId)
                .select(BaseBusinessFileEntity::getFileName, BaseBusinessFileEntity::getFileUrl)
                .list();
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream()
                .map(t -> new FileVo(t.getFileName(), t.getFileUrl()))
                .toList();
    }
}
