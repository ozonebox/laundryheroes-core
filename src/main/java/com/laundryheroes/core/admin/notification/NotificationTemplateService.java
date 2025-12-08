package com.laundryheroes.core.admin.notification;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.notification.NotificationCategory;
import com.laundryheroes.core.notification.NotificationService;
import com.laundryheroes.core.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private final NotificationTemplateRepository repo;
    private final NotificationService notificationService;
    private final ResponseFactory responseFactory;

    // ------------------------------------------------------
    // CREATE TEMPLATE (SUPERADMIN)
    // ------------------------------------------------------
    @Transactional
    public ApiResponse<NotificationTemplate> create(NotificationTemplateRequest req) {
        NotificationTemplate t = new NotificationTemplate();
        t.setActive(true);
        t.setBody(req.getBody());
        t.setCategory(req.getCategory());
        t.setTitle(req.getTitle());

        return responseFactory.success(ResponseCode.SUCCESS, repo.save(t));
    }

    // ------------------------------------------------------
    // UPDATE TEMPLATE
    // ------------------------------------------------------
    @Transactional
    public ApiResponse<NotificationTemplate> update(Long id, NotificationTemplateRequest req) {
        NotificationTemplate t = repo.findById(id).orElse(null);

        if (t == null) {
            return responseFactory.error(ResponseCode.TEMPLATE_NOT_FOUND);
        }

        t.setTitle(req.getTitle());
        t.setBody(req.getBody());
        t.setCategory(req.getCategory());
        t.setActive(true);

        return responseFactory.success(ResponseCode.SUCCESS, repo.save(t));
    }

    // ------------------------------------------------------
    // DELETE TEMPLATE
    // ------------------------------------------------------
    @Transactional
    public ApiResponse<Void> delete(Long id) {
        NotificationTemplate t = repo.findById(id).orElse(null);

        if (t == null) {
            return responseFactory.error(ResponseCode.TEMPLATE_NOT_FOUND);
        }

        repo.delete(t);
        return responseFactory.success(ResponseCode.SUCCESS, null);
    }

    // ------------------------------------------------------
    // LIST ALL ACTIVE TEMPLATES
    // ------------------------------------------------------
    public ApiResponse<List<NotificationTemplate>> listActive() {
        return responseFactory.success(ResponseCode.SUCCESS, repo.findByActiveTrue());
    }

    // ------------------------------------------------------
    // LIST BY CATEGORY
    // ------------------------------------------------------
    public ApiResponse<List<NotificationTemplate>> listByCategory(NotificationCategory category) {
        return responseFactory.success(ResponseCode.SUCCESS, repo.findByCategory(category));
    }

    // ------------------------------------------------------
    // GET ONE TEMPLATE
    // ------------------------------------------------------
    public ApiResponse<NotificationTemplate> get(Long id) {
        NotificationTemplate t = repo.findById(id).orElse(null);

        if (t == null) {
            return responseFactory.error(ResponseCode.TEMPLATE_NOT_FOUND);
        }

        return responseFactory.success(ResponseCode.SUCCESS, t);
    }

    // ------------------------------------------------------
    // SEND NOTIFICATION USING A TEMPLATE
    // ------------------------------------------------------
    @Transactional
    public ApiResponse<Void> sendUsingTemplate(Long templateId, User user, Map<String, String> vars) {

        NotificationTemplate t = repo.findById(templateId).orElse(null);

        if (t == null) {
            return responseFactory.error(ResponseCode.TEMPLATE_NOT_FOUND);
        }

        String renderedBody = renderTemplate(t.getBody(), vars);

        notificationService.sendToUser(
            user,
            t.getCategory(),
            t.getTitle(),
            renderedBody,
            null
        );

        return responseFactory.success(ResponseCode.SUCCESS, null);
    }

    // ------------------------------------------------------
    // TEMPLATE VARIABLE RENDERER
    // ------------------------------------------------------
    public String renderTemplate(String body, Map<String, String> values) {
        String msg = body;
        for (var entry : values.entrySet()) {
            msg = msg.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return msg;
    }
}
