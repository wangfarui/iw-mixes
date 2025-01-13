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
public abstract class WebController<S extends WebService<A, U, V>, A extends AddDto, U extends UpdateDto, V extends DetailVo> {

    private final S webService;

    public WebController(S webService) {
        this.webService = webService;
    }

    @PostMapping("/add")
    public Serializable add(@RequestBody @Valid A dto) {
        return getWebService().add(dto);
    }

    @PutMapping("/update")
    public void update(@RequestBody @Valid U dto) {
        getWebService().update(dto);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") Serializable id) {
        getWebService().delete(id);
    }

    @GetMapping("/detail")
    @SuppressWarnings("unchecked")
    public V detail(@RequestParam("id") Serializable id) {
        return (V) getWebService().detail(id);
    }

    protected S getWebService() {
        return this.webService;
    }
}
