package com.ylab.intensive.service;

import com.ylab.intensive.util.PropertiesUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A utility class for managing a pool of database connections.
 */
public final class ConnectionManager {
    private static final String URI_KEY = "db.uri";
    private static final String USER_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE = "db.poolSize";
    private static final int POOL_SIZE_DEFAULT = 5;
    private static BlockingQueue<Connection> pool;
    private static List<Connection> sourcesConnections;

    private ConnectionManager() {
    }

    /**
     *
     * Static initialization block for loading the database driver and initializing the connection pool
     */
    static {
        loadDriver();
        initConnectionPool();
    }

    /**
     * Initializes the connection pool based on configuration
     */
    private static void initConnectionPool() {
        String poolSize = PropertiesUtil.get(POOL_SIZE);
        int size = poolSize == null ? POOL_SIZE_DEFAULT : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        sourcesConnections = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            Connection proxyConnections = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy)
                            : method.invoke(connection, args));
            pool.add(proxyConnections);
            sourcesConnections.add(connection);
        }
    }

    /**
     * Retrieves a connection from the pool
     */
    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens a new database connection
     */
    private static Connection open() {
        try {
            return DriverManager.getConnection(PropertiesUtil.get(URI_KEY),
                    PropertiesUtil.get(USER_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the database driver
     */
    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the connection pool
     */
    public static void closePool() {
        try {
            for (Connection connection : sourcesConnections) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
