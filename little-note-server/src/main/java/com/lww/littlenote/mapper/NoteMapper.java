package com.lww.littlenote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lww.littlenote.entity.Note;
import com.lww.littlenote.vo.NoteCountVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lww
 * @since 2024-06-13
 */
public interface NoteMapper extends BaseMapper<Note> {

    /**
     * 统计当前用户各分组下的笔记数量，没有笔记的分组也会返回，数量为 0。
     *
     * @param userId 用户ID
     * @return 各分组笔记数量
     */
    @Select("""
            SELECT g.id AS groupId, g.group_name AS groupName, COUNT(n.id) AS noteCount
            FROM note_group g
            LEFT JOIN note n ON n.group_id = g.id AND n.user_id = #{userId}
            GROUP BY g.id, g.group_name
            ORDER BY g.id
            """)
    List<NoteCountVo.GroupItem> countGroupNote(@Param("userId") Long userId);
}
