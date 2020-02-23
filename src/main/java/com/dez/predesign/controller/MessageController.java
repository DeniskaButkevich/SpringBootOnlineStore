package com.dez.predesign.controller;

import com.dez.predesign.data.Message;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.MessageRepo;
import com.dez.predesign.service.MessageService;
import com.dez.predesign.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class MessageController  {

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    PageService pageService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/admins/message")
    public String messagesShow( @RequestParam(required = false, defaultValue = "") String filter,
                                Model model,
                                @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 3) Pageable pageable) {
        Page<Message> page;

        if (filter != null && !filter.isEmpty())
            page = messageRepo.findByTag(filter, pageable);
        else
            page = messageRepo.findAll(pageable);

        List<Integer> listpages = pageService.listPages(pageable, page);

        model.addAttribute("listpages", listpages);
        model.addAttribute("page", page);
        model.addAttribute("url", "/admins/message");
        model.addAttribute("filter", filter);

        return "admins/message";
    }

    @PostMapping("/admins/message")
    public String messageAdd(@AuthenticationPrincipal User user,
                             @Valid Message message,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam("file") MultipartFile file,
                             @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 3) Pageable pageable
    ) throws IOException {

        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);

        } else {
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
        }
        Page<Message> page = messageRepo.findAll(pageable);
        List<Integer> listpages = pageService.listPages(pageable, page);

        model.addAttribute("listpages", listpages);

        model.addAttribute("page", page);

        return "admins/message";
    }

    @GetMapping("/message/delete")
    public String messageRemove(@RequestParam String id) {

        messageRepo.delete(
                messageRepo.findById(
                        Long.parseLong(id)).get()
        );

        return "redirect:/admins/message";
    }

    @PostMapping("/message/edit/{id}")
    public String messageEdit(@PathVariable String id, Message message){

        Message mes = messageRepo.findById(Long.parseLong(id)).get();

        mes.setName(message.getName());
        mes.setText(message.getText());

        messageRepo.save(mes);

        return "redirect:/admins/message";
    }

}
