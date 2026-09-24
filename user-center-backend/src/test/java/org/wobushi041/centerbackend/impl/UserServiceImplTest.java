package org.wobushi041.centerbackend.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.wobushi041.centerbackend.exception.BusinessException;
import org.wobushi041.centerbackend.mapper.UserMapper;
import org.wobushi041.centerbackend.service.impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 验证用户服务注册逻辑
 *
 * @author wobushi041
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    /**
     * 模拟用户数据访问依赖
     */
    @Mock
    private UserMapper userMapper;

    /**
     * 注入待测试的用户服务
     */
    @InjectMocks
    private UserServiceImpl userService;

    /**
     * 初始化用户服务的数据访问依赖
     */
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "baseMapper", userMapper);
    }

    // 场景：测试注册参数校验失败
    @Test
    @DisplayName("userRegister 参数校验失败")
    void userRegister_shouldThrowWhenParamsInvalid() {
        // 1. 准备不同类型的无效注册参数

        // 2. 调用用户注册方法

        // 3. 断言每组无效参数均抛出业务异常
        assertThrows(BusinessException.class, () -> userService.userRegister("", "12345678", "12345678"));
        assertThrows(BusinessException.class, () -> userService.userRegister("041", "12345678", "12345678"));
        assertThrows(BusinessException.class, () -> userService.userRegister("00041", "123456", "123456"));
        assertThrows(BusinessException.class, () -> userService.userRegister("000401", "12345678", "12345678"));
        assertThrows(BusinessException.class, () -> userService.userRegister("41", "12345678", "12345679"));
    }

    // 场景：测试重复账号注册失败
    @Test
    @DisplayName("userRegister 账号重复")
    void userRegister_shouldThrowWhenAccountExists() {
        // 1. 准备已存在账号的查询结果
        when(userMapper.selectCount(any())).thenReturn(1L);

        // 2. 调用用户注册方法

        // 3. 断言重复账号触发业务异常
        assertThrows(BusinessException.class, () -> userService.userRegister("wobushi041", "12345678", "12345678"));
    }

}
