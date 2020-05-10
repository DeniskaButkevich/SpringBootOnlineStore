package com.dez.predesign.controller.admin;

import com.dez.predesign.data.Message;
import com.dez.predesign.data.User;
import com.dez.predesign.repository.MessageRepo;
import com.dez.predesign.service.PageService;
import com.dez.predesign.util.ControllerUtils;
import com.dez.predesign.util.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admins/messages")
public class MessageController  {

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    PageService pageService;

    @GetMapping()
    public String messagesShow(@RequestParam(required = false, defaultValue = "") String filter,
                               @RequestParam(required = false, defaultValue = "") String search_by,
                               @PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                               Model model) {
        Page<Message> page;
        if((filter != null && !filter.isEmpty()) && (search_by != null && !filter.isEmpty())){
            page = pageService.findByFilterMessages(search_by, filter, pageable);
        } else {
            page = messageRepo.findAll(pageable);
        }

        List<Integer> listpages = pageService.listPages(page);

        model.addAttribute("listpages", listpages);
        model.addAttribute("page", page);
        model.addAttribute("url", "/admins/messages");
        model.addAttribute("filter", filter);

        return "admins/message";
    }

    @GetMapping("/delete")
    public String messageRemove(@RequestParam Long id){
        Message message =  messageRepo.findById(id).get();
        messageRepo.delete(message);
        return "redirect:/admins/messages";
    }
}
