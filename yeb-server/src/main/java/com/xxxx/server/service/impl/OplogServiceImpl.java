package com.xxxx.server.service.impl;

import com.xxxx.server.pojo.Oplog;
import com.xxxx.server.mapper.OplogMapper;
import com.xxxx.server.service.IOplogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jinlin
 * @since 2022-01-09
 */
@Service
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
