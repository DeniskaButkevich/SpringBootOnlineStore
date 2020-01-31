package com.dez.predesign.repository;

import com.dez.predesign.data.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface MessageRepo extends CrudRepository<Message,Integer> {
    Iterable<Message> findByTag(String tag);
}
