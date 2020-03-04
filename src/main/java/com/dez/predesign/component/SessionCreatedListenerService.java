package com.dez.predesign.component;

import com.dez.predesign.data.catalog.Color;
import com.dez.predesign.repository.ColorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import javax.servlet.http.HttpSession;

@Component
public class SessionCreatedListenerService implements ApplicationListener<ApplicationEvent> {

    private static final Logger logger = LoggerFactory
            .getLogger(SessionCreatedListenerService.class);

    @Autowired
    HttpSession httpSession;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
////        if(applicationEvent instanceof ServletRequestHandledEvent)
////            logger.debug(applicationEvent.toString());
//        if (applicationEvent instanceof HttpSessionCreatedEvent) { //If event is a session created event
//
//        } else if (applicationEvent instanceof HttpSessionDestroyedEvent) { //If event is a session destroy event
//            // handler.expireCart();
//            logger.debug("" + (Long) httpSession.getAttribute("userId"));
//            logger.debug(" Session is destory  :"); //log data
//
//        } else if (applicationEvent instanceof AuthenticationSuccessEvent) { //If event is a session destroy event
//            logger.debug("  athentication is success  :"); //log data
//            logger.debug(applicationEvent.toString());
//        } else if (applicationEvent instanceof LogoutSuccessEvent) { //If event is a session destroy event
//            logger.debug("  athentication is logout  :"); //log data
//
//        }else{
//           // logger.debug(applicationEvent.toString());
//        }
    }
}
