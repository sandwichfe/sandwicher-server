package com.lww.littlenote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.littlenote.entity.NoteIndexTask;

/**
 * 笔记 ES 索引同步任务服务。
 */
public interface NoteIndexTaskService extends IService<NoteIndexTask> {

    String ACTION_CREATE = "CREATE";
    String ACTION_UPDATE = "UPDATE";
    String ACTION_DELETE = "DELETE";

    /**
     * 创建笔记索引同步任务。
     */
    void createTask(Long noteId, String action);

    /**
     * 扫描并处理待同步任务。
     */
    void processPendingTasks();
}
