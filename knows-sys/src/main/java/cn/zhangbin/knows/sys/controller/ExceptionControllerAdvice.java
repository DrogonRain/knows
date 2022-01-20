package cn.zhangbin.knows.sys.controller;

import cn.zhangbin.knows.portal.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**ExceptionControllerAdvice
 * 这个类是专门处理控制器发生的异常的
 * @RestControllerAdvice表示当前类是处理控制器通知功能的
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    /**
     * 编写异常处理的方法
     * 每个方法可以处理一种异常,可以编写多个方法
     * @ExceptionHandler表示下面方式专门处理异常
     * 如果控制器中发生的异常类型和这个方法的参数匹配就运行这个方法
     */
    @ExceptionHandler //Handler(处理者)
    public String handlerServiceException(ServiceException e){
        log.error("业务异常: ",e);
        return e.getMessage();
    }

    /**
     * 一个类可以有多个处理异常的方法,每个方法处理不同的异常类型
     * 这里处理异常的弗雷Exception表示出现任何异常,当前类都处理
     */
    @ExceptionHandler
    public String handlerException(Exception e){
        log.error("其他异常: ",e);
        return e.getMessage();
    }
}
