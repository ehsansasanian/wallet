package com.example.ewallet.ServiceImpl.sms;

import com.example.ewallet.base.GenerateRandomChars;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author EhSan
 */
@Service
public class SMSService {

    final
    WebClient.Builder webClientBuilder;

    @Value("${sms.server.scheme: default}")
    private String scheme;
    @Value("${sms.server.host: default}")
    private String host;
    @Value("${sms.server.path: default}")
    private String path;
    @Value("${sms.server.from: default}")
    private String from;
    @Value("${sms.code.length: default}")
    private String smsCodeLength;


    public SMSService(@Qualifier("WebClientCustom") WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<String> smsSend(Long num) {

        String number = "0" + num;

        String securityCode = GenerateRandomChars.generatedCode(Integer.parseInt(smsCodeLength));

        SMSBodyDTO dataToSend = new SMSBodyDTO(securityCode, number, this.from);

//        try {
//            return webClientBuilder //
//                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build() //
//                    .post() //
//                    .uri(builder -> builder.scheme(scheme).host(host).path(path).build()) //
//                    .syncBody(dataToSend) //
//                    .exchange() //
//                    .flatMap(collegeResponse -> {
//
//                        if (collegeResponse != null && collegeResponse.statusCode() == HttpStatus.OK)
//                            return collegeResponse.bodyToMono(String.class).map(data -> data).defaultIfEmpty(securityCode);
//
//                        else
//                            return Mono.just(securityCode);
//                    }).defaultIfEmpty(securityCode);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Mono.just(securityCode);
//        }
        return Mono.just(securityCode);
    }

}
