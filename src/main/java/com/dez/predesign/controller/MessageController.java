package com.dez.predesign.controller;

import com.dez.predesign.data.Message;
import com.dez.predesign.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MessageController  {

    @Autowired
    MessageRepo messageRepo;

    @GetMapping("/admin/message")
    public String messagesShow(Model model){
        model.addAttribute("messages", messageRepo.findAll());

        return "admin/message";
    }

    @PostMapping("/admin/message")
    public String messageAdd(Message message, Model model){
        messageRepo.save(message);

        model.addAttribute("messages", messageRepo.findAll());

        return "admin/message";
    }

    @GetMapping("/message/delete")
    public String messageRemove(@RequestParam String id) {
        messageRepo.delete(
                messageRepo.findById(
                        Integer.parseInt(id)).get()
        );

        return "redirect:/admin/message";
    }

    @PostMapping("/message/edit/{id}")
    public String messageEdit(@PathVariable String id, Message message){
        Message mes = messageRepo.findById(Integer.parseInt(id)).get();

        mes.setName(message.getName());
        mes.setText(message.getText());

        messageRepo.save(mes);

        return "redirect:/admin/message";
    }

}
