package com.example.ewallet.resourcesimpl;

import com.example.ewallet.base.ValidationTools;
import com.example.ewallet.enums.Status;
import com.example.ewallet.resource.AdminUserResource;
import com.example.ewallet.resourcesimpl.dto.ResGetListWalletDTO;
import com.example.ewallet.resourcesimpl.dto.ResTransactionLogReportToAdminDTO;
import com.example.ewallet.services.AdminUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
public class AdminUserResourceResourceImpl implements AdminUserResource {

    private final AdminUserService adminUserService;

    public AdminUserResourceResourceImpl(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Override
    public Mono<ResponseEntity<Boolean>> updateWalletStatusByAdmin(Long phoneNumber, Status status) {
        ValidationTools.phoneNumber(phoneNumber, "phoneNumber");
        ValidationTools.nullObject(status, "status");
        return adminUserService.updateWalletStatusByAdmin(phoneNumber, status);
    }

    @Override
    public Mono<ResponseEntity<Page<ResGetListWalletDTO>>> getWalletsList(Integer page, Integer size) {
        ValidationTools.nullObject(page, "page");
        ValidationTools.nullObject(size, "size");
        return adminUserService.getWalletsList(PageRequest.of(page, size));
    }

    @Override
    public Mono<ResponseEntity<Page<ResTransactionLogReportToAdminDTO>>> getReportByPagination(Date fromDate, Date toDate, Integer page, Integer size) {
        return adminUserService.getReportByPagination(fromDate, toDate, PageRequest.of(page, size));
    }
}
