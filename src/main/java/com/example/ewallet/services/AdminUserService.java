package com.example.ewallet.services;

import com.example.ewallet.enums.Status;
import com.example.ewallet.resourcesimpl.dto.ResGetListWalletDTO;
import com.example.ewallet.resourcesimpl.dto.ResTransactionLogReportToAdminDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface AdminUserService {

    Mono<ResponseEntity<Boolean>> updateWalletStatusByAdmin(Long phoneNumber, Status status);

    Mono<ResponseEntity<Page<ResGetListWalletDTO>>> getWalletsList(Pageable pagination);

    Mono<ResponseEntity<Page<ResTransactionLogReportToAdminDTO>>> getReportByPagination(Date fromDate, Date toDate, Pageable pagination);
}
