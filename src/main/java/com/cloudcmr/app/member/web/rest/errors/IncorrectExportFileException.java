package com.cloudcmr.app.member.web.rest.errors;

import com.cloudcmr.app.web.rest.errors.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;

public class IncorrectExportFileException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public IncorrectExportFileException() {
        super(ErrorConstants.INCORRECT_EXPORT_FILE, "Le fichier d'export est incorrect. Veuillez le vU+00E9rifier");
    }

}
