package cn.zhangbin.knows.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan扫描指定的包下的所有类和接口,形成当前报下所有类和接口添加@Mapper相同的效果
@MapperScan("cn.zhangbin.knows.portal.mapper")
public class KnowsPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowsPortalApplication.class, args);
    }

}
