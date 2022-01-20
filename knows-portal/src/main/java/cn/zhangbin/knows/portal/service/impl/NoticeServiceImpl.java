package cn.zhangbin.knows.portal.service.impl;

import cn.zhangbin.knows.portal.model.Notice;
import cn.zhangbin.knows.portal.mapper.NoticeMapper;
import cn.zhangbin.knows.portal.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangbin.cn
 * @since 2021-11-23
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

}
