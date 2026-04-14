package com.manufacturing.inventory.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/*MyBatis-Plus 多租户拦截器*/
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 多租户插件 - 自动拼接 WHERE tenant_id = ?
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler()));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "tenantId", Long.class, getTenantId());
                this.strictInsertFill(metaObject, "createBy", Long.class, getUserId());
                this.strictInsertFill(metaObject, "updateBy", Long.class, getUserId());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
                this.strictUpdateFill(metaObject, "updateBy", Long.class, getUserId());
            }
            
            private Long getTenantId() {
                try {
                    return com.manufacturing.inventory.security.TenantContext.getTenantId();
                } catch (Exception e) {
                    return null;
                }
            }
            
            private Long getUserId() {
                try {
                    return com.manufacturing.inventory.security.TenantContext.getUserId();
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }
    
    public static class TenantLineHandler implements com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler {
        
        @Override
        public Expression getTenantId() {
            try {
                Long tenantId = com.manufacturing.inventory.security.TenantContext.getTenantId();
                return tenantId != null ? new LongValue(tenantId) : new LongValue(-1);
            } catch (Exception e) {
                return new LongValue(-1);
            }
        }

        @Override
        public String getTenantIdColumn() {
            return "tenant_id";
        }

        // 忽略不需要租户隔离的表
        @Override
        public boolean ignoreTable(String tableName) {
            return "sys_tenant".equals(tableName) || 
                   "sys_user".equals(tableName) ||
                   tableName.startsWith("sys_");
        }
    }
}
