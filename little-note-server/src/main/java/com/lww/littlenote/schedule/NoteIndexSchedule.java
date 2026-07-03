package com.lww.littlenote.schedule;

import com.lww.littlenote.service.NoteIndexTaskService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时扫描笔记索引同步任务。
 */
@Component
public class NoteIndexSchedule {

    @Resource
    private NoteIndexTaskService noteIndexTaskService;

    @Scheduled(fixedDelayString = "${little-note.es.index-task-fixed-delay:10000}")
    public void processPendingTasks() {
        noteIndexTaskService.processPendingTasks();
    }
}
