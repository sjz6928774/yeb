package com.xxxx.server.service.impl;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.mapper.AdminMapper;
import com.xxxx.server.service.IAdminService;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
