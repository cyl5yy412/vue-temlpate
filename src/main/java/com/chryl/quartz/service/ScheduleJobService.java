/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.chryl.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chryl.quartz.model.ScheduleJob;

/**
 *
 * @author lgh
 *
 */
public interface ScheduleJobService extends IService<ScheduleJob>{

	/**
	 * 保存并开始定时任务
	 * @param scheduleJob
	 */
	void saveAndStart(ScheduleJob scheduleJob);

	/**
	 * 更新定时任务
	 * @param scheduleJob
	 */
	void updateScheduleJob(ScheduleJob scheduleJob);

	/**
	 * 批量删除定时任务
	 * @param jobIds 需要删除的job id列表
	 */
	void deleteBatch(Long[] jobIds);

	/**
	 * 批量更新定时任务状态
	 * @param jobIds 需要更新的job id列表
	 * @param status 更新后的状态
	 * @return 更新数量
	 */
	int updateBatch(Long[] jobIds, int status);

	/**
	 * 立即执行
	 * @param jobIds job id列表
	 */
	void run(Long[] jobIds);

	/**
	 * 暂停运行
	 * @param jobIds job id列表
	 */
	void pause(Long[] jobIds);

	/**
	 * 恢复运行
	 * @param jobIds job id列表
	 */
	void resume(Long[] jobIds);

}
