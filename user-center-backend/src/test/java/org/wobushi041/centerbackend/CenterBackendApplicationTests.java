package org.wobushi041.centerbackend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wobushi041.centerbackend.mapper.UserMapper;
import org.wobushi041.centerbackend.model.enity.User;

import javax.annotation.Resource;

/**
 * 验证用户中心应用的数据访问配置
 *
 * @author wobushi041
 */
@SpringBootTest
class CenterBackendApplicationTests {

    /**
     * 注入用户数据访问依赖
     */
    @Resource
    private UserMapper userMapper;

    // 场景：测试用户角色字段能够正确映射
    @Test
    void testSelectUserRoleAndPlanetCode() {
        // 1. 准备测试用户 id
        Long testUserId = 1L;

        // 2. 查询用户数据
        User user = userMapper.selectById(testUserId);

        // 3. 断言用户及角色字段存在
        Assertions.assertNotNull(user, "用户不存在，请先手动插入测试数据");
        Assertions.assertNotNull(user.getUserRole(), "role 没查出来，请检查数据库字段、XML 映射或实体字段");
        System.out.println("id = " + user.getId());
        System.out.println("role = " + user.getUserRole());
    }

}
