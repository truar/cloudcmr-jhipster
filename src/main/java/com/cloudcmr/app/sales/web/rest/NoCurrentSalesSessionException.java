package com.cloudcmr.app.sales.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No Sales Session for the current user. Open a new one first !")
public class NoCurrentSalesSessionException extends RuntimeException {
}
