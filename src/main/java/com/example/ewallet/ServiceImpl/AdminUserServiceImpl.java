package com.example.ewallet.ServiceImpl;

import com.example.ewallet.enums.Status;
import com.example.ewallet.repository.TransactionLogsRepository;
import com.example.ewallet.repository.WalletRepository;
import com.example.ewallet.resourcesimpl.dto.ResGetListWalletDTO;
import com.example.ewallet.resourcesimpl.dto.ResTransactionLogReportToAdminDTO;
import com.example.ewallet.services.AdminUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final WalletRepository walletRepository;
    private final TransactionLogsRepository transactionLogsRepository;

    public AdminUserServiceImpl(WalletRepository walletRepository, TransactionLogsRepository transactionLogsRepository) {
        this.walletRepository = walletRepository;
        this.transactionLogsRepository = transactionLogsRepository;
    }

    @Override
    public Mono<ResponseEntity<Boolean>> updateWalletStatusByAdmin(Long phoneNumber, Status status) {
        walletRepository.update(phoneNumber, status);
        return Mono.just(ResponseEntity.ok(true));
    }

    @Override
    public Mono<ResponseEntity<Page<ResGetListWalletDTO>>> getWalletsList(Pageable pagination) {
        return Mono.just(walletRepository.getAllByPagination(pagination)).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Page<ResTransactionLogReportToAdminDTO>>> getReportByPagination(Date fromDate, Date toDate, Pageable pagination) {
        if (toDate == null)
            toDate = new Date();
        if (fromDate == null)
            return Mono.just(transactionLogsRepository.reportToAdminByPagination(toDate, pagination)).map(ResponseEntity::ok);
        else
            return Mono.just(transactionLogsRepository.reportToAdminByPagination(fromDate, toDate, pagination)).map(ResponseEntity::ok);

    }
}
