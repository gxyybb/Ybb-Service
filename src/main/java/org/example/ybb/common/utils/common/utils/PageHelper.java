package org.example.ybb.common.utils.common.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class PageHelper<T> {
    private int pageSize;
    private int pageNum;
    private int totalItems;
    private int totalPages;
    private List<T> list;

    public PageHelper(List<T> list, int pageSize, int pageNum) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be greater than 0");
        }
        if (pageNum <= 0) {
            throw new IllegalArgumentException("pageNum must be greater than 0");
        }

        Collections.reverse(list);
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalItems = list.size();
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Ensure current page is within the valid range
        if (pageNum > totalPages) {
            this.pageNum = totalPages;
        }

        if (totalPages == 0) {
            this.pageNum = 1;
            this.totalPages = 1;
        }
        int fromIndex = (pageNum - 1) * pageSize;
        if (fromIndex >= totalItems || fromIndex < 0) {
           this.list = Collections.emptyList();
        }
        int toIndex = Math.min(fromIndex + pageSize, totalItems);
         this.list = list.subList(fromIndex, toIndex);
    }

    public PageHelper(List<T> list, Integer pageNum) {
        this(list, 10, pageNum);
    }


}
