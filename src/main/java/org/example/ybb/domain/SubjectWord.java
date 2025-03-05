package org.example.ybb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName subject_word
 */
@TableName(value ="subject_word")
@Data
public class SubjectWord implements Serializable {
    /**
     * 
     */
    private Integer subjectId;

    /**
     * 
     */
    private Integer wordId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}