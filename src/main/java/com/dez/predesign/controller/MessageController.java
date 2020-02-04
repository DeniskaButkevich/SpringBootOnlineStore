package com.dez.predesign.controller;

import com.dez.predesign.data.Message;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MessageController  {

    @Autowired
    MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/admins/message")
    public String messagesShow(Model model, @RequestParam(required = false, defaultValue = "") String filter){
        Iterable<Message> messages = messageRepo.findAll();

        if (filter != null && !filter.isEmpty())
            messages = messageRepo.findByTag(filter);
        else
            messages = messageRepo.findAll();

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "admins/message";
    }

    @PostMapping("/admins/message")
    public String messageAdd(Message message,
                             Model model,
                             @RequestParam("file") MultipartFile file,
                             @AuthenticationPrincipal User user
    ) throws IOException {

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + file.getOriginalFilename();


            file.transferTo(new File("C:\\Desktop\\java\\sweater\\uploads\\" + resultFilename));

            message.setFilename(resultFilename);
        }
        model.addAttribute("message", null);

        messageRepo.save(message);

        model.addAttribute("messages", messageRepo.findAll());

        return "admins/message";
    }

    @GetMapping("/message/delete")
    public String messageRemove(@RequestParam String id) {

        messageRepo.delete(
                messageRepo.findById(
                        Integer.parseInt(id)).get()
        );

        return "redirect:/admins/message";
    }

    @PostMapping("/message/edit/{id}")
    public String messageEdit(@PathVariable String id, Message message){

        Message mes = messageRepo.findById(Integer.parseInt(id)).get();

        mes.setName(message.getName());
        mes.setText(message.getText());

        messageRepo.save(mes);

        return "redirect:/admins/message";
    }

}
