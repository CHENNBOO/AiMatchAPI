package com.aimatch.business.mapper;

import com.aimatch.business.entity.VirtualInteractionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VirtualInteractionRecordMapper extends BaseMapper<VirtualInteractionRecord> {
    
    // 按用户ID分页查询聊天记录
    @Select("SELECT * FROM virtual_interaction_record WHERE user_id = #{userId} ORDER BY create_time DESC")
    IPage<VirtualInteractionRecord> selectByUserId(Page<VirtualInteractionRecord> page, @Param("userId") String userId);
    
    // 按会话ID查询聊天记录
    @Select("SELECT * FROM virtual_interaction_record WHERE session_id = #{sessionId} ORDER BY create_time ASC")
    List<VirtualInteractionRecord> selectBySessionId(@Param("sessionId") String sessionId);
    
    // 按时间范围分页查询聊天记录
    @Select("SELECT * FROM virtual_interaction_record WHERE create_time BETWEEN #{startTime} AND #{endTime} ORDER BY create_time DESC")
    IPage<VirtualInteractionRecord> selectByTimeRange(Page<VirtualInteractionRecord> page, 
                                                     @Param("startTime") LocalDateTime startTime, 
                                                     @Param("endTime") LocalDateTime endTime);
    
    // 按性格特征分页查询聊天记录
    @Select("SELECT * FROM virtual_interaction_record WHERE personality LIKE CONCAT('%', #{personality}, '%') ORDER BY create_time DESC")
    IPage<VirtualInteractionRecord> selectByPersonality(Page<VirtualInteractionRecord> page, @Param("personality") String personality);
    
    // 统计某个用户的聊天次数
    @Select("SELECT COUNT(*) FROM virtual_interaction_record WHERE user_id = #{userId}")
    long countByUserId(@Param("userId") String userId);
    
    // 统计某个会话的聊天次数
    @Select("SELECT COUNT(*) FROM virtual_interaction_record WHERE session_id = #{sessionId}")
    long countBySessionId(@Param("sessionId") String sessionId);
    
    // 按时间范围统计聊天次数
    @Select("SELECT COUNT(*) FROM virtual_interaction_record WHERE create_time BETWEEN #{startTime} AND #{endTime}")
    long countByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    // 查询最近N条聊天记录
    @Select("SELECT * FROM virtual_interaction_record ORDER BY create_time DESC LIMIT #{n}")
    List<VirtualInteractionRecord> selectTopN(@Param("n") int n);
} 