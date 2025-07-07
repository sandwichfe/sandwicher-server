package com.lww.sandwich.view;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.lww.common.web.vo.PageDataVo;
import com.lww.common.web.vo.PageVo;
import com.lww.sandwich.service.ViewService;
import com.lww.sandwich.vo.ViewVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ViewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ViewService viewService;

    @Test
    public void testGetViewList() throws Exception {
        // 创建模拟的 PageVo 参数
        PageVo pageVo = new PageVo(1, 10);

        // 创建模拟的返回结果
        List<ViewVO> mockViewList = Arrays.asList(new ViewVO("1", "View1","","", LocalDateTime.now(),""), new ViewVO("2", "View1","","", LocalDateTime.now(),""));
        PageDataVo<List<ViewVO>> mockPageDataVo = new PageDataVo<>(1L, 20L,mockViewList); // 假设总共有20条数据

        // 模拟 viewService 返回的结果
        Mockito.when(viewService.getViewList(Mockito.any(PageVo.class))).thenReturn(mockPageDataVo);

        // 执行 HTTP GET 请求，并验证返回的结果
        mockMvc.perform(get("/getViewList")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())  // 验证返回状态为 200
                .andExpect(jsonPath("$.code").value(200))  // 验证 JSON 中的 code 字段为 200
                .andExpect(jsonPath("$.data.total").value(20))  // 验证 total 字段为 20
                .andExpect(jsonPath("$.data.list[0].name").value("View1"))  // 验证第一个元素的 name 字段
                .andExpect(jsonPath("$.data.list[1].name").value("View2"));  // 验证第二个元素的 name 字段
    }

    // 测试传入无效参数时的返回结果
    @Test
    public void testGetViewListWithInvalidParams() throws Exception {
        mockMvc.perform(get("/getViewList")
                        .param("page", "-1")  // 传入非法页码
                        .param("size", "10"))
                .andExpect(status().isBadRequest());  // 期望返回 400 状态码（表示请求参数无效）
    }
}
