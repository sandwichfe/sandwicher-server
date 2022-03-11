package com.lww.sandwich.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 例子 前端控制器
 *
 * @author lww
 * @since 2022-03-11 16:14:43
 */
@Api(tags = "例子")
@RestController
@RequestMapping("/sandwich/simple")
public class SimpleController {

}
