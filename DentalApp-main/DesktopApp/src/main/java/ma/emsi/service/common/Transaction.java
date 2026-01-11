package ma.emsi.service.common;

import java.sql.Connection;
import ma.emsi.common.consoleLog.ConsoleLogger;
import ma.emsi.config.SessionFactory;

public final class Transaction {


    private Transaction(){}


    @FunctionalInterface
    public interface TransactionBlocExecuter<T> {

        T run(Connection c) throws Exception;
    }

    public static <T> T initTransaction(TransactionBlocExecuter<T> blocTransactionnelAExecuter) {

        try (Connection connection = SessionFactory.getInstance().getConnection()) {

            ConsoleLogger.info(" ouverture de la connexion JDBC " + connection.getMetaData().getURL());
            boolean oldAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try {
                T result = blocTransactionnelAExecuter.run(connection);
                connection.commit();
                return result;
            }
            catch (Exception ex) {
                connection.rollback();
                throw new RuntimeException(ex);
            }
            finally {
                connection.setAutoCommit(oldAutoCommit);

                if(connection.isClosed()){

                    ConsoleLogger.info("Closing JDBC connection " + connection.getMetaData().getURL());
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
