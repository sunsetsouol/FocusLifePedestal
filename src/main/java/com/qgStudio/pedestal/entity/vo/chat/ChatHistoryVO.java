package com.qgStudio.pedestal.entity.vo.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatHistoryVO {

    private String type;

    private Integer targetId;

    private List<ChatHistoryContextVO> chatHistoryContextVOs;

    @AllArgsConstructor
    @Getter
    public enum ChatType{
        PRIVATE("private"),
        GROUP("group"),
        ;
        private final String type;

    }

}
