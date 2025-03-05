package org.example.ybb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName word
 */
@TableName(value ="word")
@Data
public class Word implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String word;

    /**
     * 
     */
    private Integer isNewWord;

    /**
     * 
     */
    private Integer isFamiliarWord;

    /**
     * 
     */
    private String pronunciation;

    /**
     * 
     */
    private String meaning;

    /**
     * 
     */
    private String wordType;

    /**
     * 
     */
    private String audioPath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}