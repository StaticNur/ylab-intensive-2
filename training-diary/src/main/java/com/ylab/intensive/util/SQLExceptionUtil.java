package com.ylab.intensive.util;

import com.ylab.intensive.exception.DaoException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

@Log4j2
public class SQLExceptionUtil {

    public static void handleSQLException(SQLException exc, Logger log) {
        StringBuilder stringBuilder = new StringBuilder();
        while (exc != null) {
            log.error("SQLState: {}", exc.getSQLState());
            stringBuilder.append("Code: ").append(exc.getErrorCode()).append(" Message: ").append(exc.getMessage()).append("; ");
            exc = exc.getNextException();
        }
        throw new DaoException(stringBuilder.toString());
    }
}
