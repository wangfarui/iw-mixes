package com.itwray.iw.web.controller;

import com.itwray.iw.web.model.dto.AddDto;
import com.itwray.iw.web.model.dto.UpdateDto;
import com.itwray.iw.web.model.vo.DetailVo;
import com.itwray.iw.web.service.WebService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * web抽象接口控制层
 *
 * @author wray
 * @since 2024/9/11
 */
public abstract class WebController<S extends WebService<A, U, V, ID>, A extends AddDto, U extends UpdateDto, V extends DetailVo, ID extends Serializable> {

    private final S webService;

    public WebController(S webService) {
        this.webService = webService;
    }

    @PostMapping("/add")
    public ID add(@RequestBody @Valid A dto) {
        return getWebService().add(dto);
    }

    @PutMapping("/update")
    public void update(@RequestBody @Valid U dto) {
        getWebService().update(dto);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") ID id) {
        getWebService().delete(id);
    }

    @GetMapping("/detail")
    public V detail(@RequestParam("id") ID id) {
        return getWebService().detail(id);
    }

    protected S getWebService() {
        return this.webService;
    }
}
