package org.example.ybb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName word_group_date_group
 */
@TableName(value ="word_date")
@Data
public class WordGroupDateGroup implements Serializable {
    /**
     * 
     */

    private Integer wordGroupId;

    /**
     * 
     */

    private Integer dateGroupId;

    private Integer subjectId;
    private Integer userId;

    /**
     * 
     */
    private Integer completionStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}