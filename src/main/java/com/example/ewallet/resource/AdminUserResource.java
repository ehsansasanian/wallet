package com.example.ewallet.resource;

import com.example.ewallet.constants.RoleList;
import com.example.ewallet.enums.Status;
import com.example.ewallet.resourcesimpl.dto.ResGetListWalletDTO;
import com.example.ewallet.resourcesimpl.dto.ResTransactionLogReportToAdminDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Date;

@RequestMapping("/admin")
public interface AdminUserResource {

    @PutMapping("/update-wallet-status/{phoneNumber}")
    @PreAuthorize(RoleList.ROLE_ADMIN)
    Mono<ResponseEntity<Boolean>> updateWalletStatusByAdmin(@PathVariable("phoneNumber") Long phoneNumber, @RequestParam("status") Status status);

    @GetMapping("/get/wallet-list")
    @PreAuthorize(RoleList.ROLE_ADMIN)
    Mono<ResponseEntity<Page<ResGetListWalletDTO>>> getWalletsList(@RequestParam("page") Integer page, @RequestParam("size") Integer size);

    @GetMapping("/get/report-list")
    @PreAuthorize(RoleList.ROLE_ADMIN)
    Mono<ResponseEntity<Page<ResTransactionLogReportToAdminDTO>>> getReportByPagination(@RequestParam(value = "fromDate", required = false) Date fromDate,
                                                                                        @RequestParam(value = "toDate", required = false) Date toDate,
                                                                                        @RequestParam("page") Integer page,
                                                                                        @RequestParam("size") Integer size);

}
