package com.dez.predesign.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PageService {
    public List<Integer> listPages(Page page) {

        if(page.getTotalPages() > 7){

            Integer totalPages = page.getTotalPages();
            Integer pageNumber = page.getNumber() + 1;

            List<Integer> head = (pageNumber > 4) ? Arrays.asList(1,-1) :Arrays.asList(1, 2, 3);
            List<Integer> tail = (pageNumber < totalPages - 3) ? Arrays.asList(-1, totalPages) : Arrays.asList(totalPages - 2, totalPages -1, totalPages);
            List<Integer> bodyBefore = (pageNumber > 4 && pageNumber < totalPages - 1) ? Arrays.asList(pageNumber - 2, pageNumber - 1) : Arrays.asList();
            List<Integer> bodyAfter = (pageNumber > 2 && pageNumber < totalPages - 3) ?  Arrays.asList(pageNumber + 1, pageNumber + 2) : Arrays.asList();

            List<Integer> result = new ArrayList<>();;

            result.addAll(head);
            result.addAll(bodyBefore);
            result.addAll((pageNumber > 3 && pageNumber < totalPages - 2) ?  Arrays.asList(pageNumber)  : Arrays.asList()) ;
            result.addAll(bodyAfter);
            result.addAll(tail);

            return result;
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= page.getTotalPages(); i++)
            result.add(i);

        return result;
    }
}
