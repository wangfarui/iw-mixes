package com.itwray.iw.auth.client;

import com.itwray.iw.auth.model.vo.DictListVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典接口
 *
 * @author farui.wang
 * @since 2025/7/18
 */
@FeignClient(value = "iw-auth-service", contextId = "dictClient", path = "/dict")
public interface BaseDictClient {

    @GetMapping("/getDictListByType")
    @Operation(summary = "查询字典列表")
    List<DictListVo> getDictListByType(@RequestParam("dictType") Integer dictType);
}
