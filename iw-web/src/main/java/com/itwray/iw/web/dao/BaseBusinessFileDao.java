package com.itwray.iw.web.dao;

import com.itwray.iw.web.config.IwAliyunProperties;
import com.itwray.iw.web.mapper.BaseBusinessFileMapper;
import com.itwray.iw.web.model.dto.FileDto;
import com.itwray.iw.web.model.entity.BaseBusinessFileEntity;
import com.itwray.iw.web.model.enums.BusinessFileTypeEnum;
import org.springframework.stereotype.Component;

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

    public void saveBusinessFile(BusinessFileTypeEnum businessFileTypeEnum, Integer businessId, List<FileDto> fileList) {

    }

    private String getBaseUrl() {
        return this.iwAliyunProperties.getOss().getBaseUrl();
    }
}
