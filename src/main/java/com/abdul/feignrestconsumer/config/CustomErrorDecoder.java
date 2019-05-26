package com.abdul.feignrestconsumer.config;

import com.abdul.feignrestconsumer.exception.BadRequestException;
import com.abdul.feignrestconsumer.exception.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

import static feign.FeignException.errorStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()){
            case 400:
                System.out.println(" Error Code : " + response.status());
                return new BadRequestException("Bad Request !!");
            case 404:
                System.out.println(" Error Code : " + response.status());
                return new NotFoundException("NOT FOUND !!!!");
            default:
                return new Exception("Generic error");
        }
    }


//    @Override
//    public Exception decode(String methodKey, Response response) {
//        if (response.status() >= 400 && response.status() <= 499) {
//            return new StashClientException(
//                    response.status(),
//                    response.reason()
//            );
//        }
//        if (response.status() >= 500 && response.status() <= 599) {
//            return new StashServerException(
//                    response.status(),
//                    response.reason()
//            );
//        }
//        return errorStatus(methodKey, response);
//    }
}