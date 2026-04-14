package com.manufacturing.inventory;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manufacturing.inventory.entity.SysUser;
import com.manufacturing.inventory.mapper.SysUserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器
 * 仅在 admin 用户不存在时创建默认账户（首次部署用），不会覆盖已有用户密码
 */
@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final SysUserMapper userMapper;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public DataInitializer(SysUserMapper userMapper,
                           org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, "admin")
        );

        if (count == 0) {
            SysUser admin = new SysUser();
            admin.setTenantId(1L);
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRealName("系统管理员");
            admin.setStatus(1);
            userMapper.insert(admin);
            System.out.println("==============================================");
            System.out.println("  初始化默认管理员: admin / 123456");
            System.out.println("  ⚠ 请登录后立即修改密码！");
            System.out.println("==============================================");
        }
    }
}
