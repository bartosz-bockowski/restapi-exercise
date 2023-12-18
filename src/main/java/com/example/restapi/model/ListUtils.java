package com.example.restapi.model;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static List getListPartFromPageable(List list, Pageable pageable) {
        if (list.isEmpty()) {
            return list;
        }
        int size = pageable.getPageSize();
        int start = pageable.getPageNumber() * size;
        int end = (pageable.getPageNumber() + 1) * size;
        if (start > list.size() - 1) {
            return new ArrayList();
        }
        if (end > list.size()) {
            end = list.size();
        }
        return list.subList(start, end);
    }

}
