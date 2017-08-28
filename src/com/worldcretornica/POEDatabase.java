package com.worldcretornica;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class POEDatabase {

	private static Connection conn = null;
	private final static String sqlitedb = "POE.db";
    private static Logger logger = Logger.getLogger("BestPathPOE");
    
    private final static String NODE_TABLE = "CREATE TABLE `poeNode` (" + 
            "`id` INTEGER," + 
    		"`name` varchar(255) NOT NULL," +
            "PRIMARY KEY (id));";
    
    private final static String EFFECT_TABLE = "CREATE TABLE `poeEffect` (" + 
            "`id` INTEGER," + 
            "`name` varchar(255)," + 
            "`weight` double NOT NULL DEFAULT '0'," +
            "PRIMARY KEY (id));";
    
    private final static String NODEEFFECT_TABLE = "CREATE TABLE `poeNodeEffect` (" + 
            "`nodeid` INTEGER," + 
            "`effectid` INTEGER," + 
            "`value` double NOT NULL DEFAULT '0'," +
            "PRIMARY KEY (nodeid, effectid));";

    private final static String NEIGHBORNODE_TABLE = "CREATE TABLE `poeNeighborNode` (" + 
            "`nodeid` INTEGER," + 
    		"`neighbornodeid` INTEGER," +
            "PRIMARY KEY (nodeid, neighbornodeid));";
    
    private final static String NODECOLLECTION_TABLE = "CREATE TABLE `poeNodeCollection` (" +
    		"`id` INTEGER," +
    		"`startnodeid` INTEGER," +
    		"PRIMARY KEY (id));";
    
    private final static String NODECOLLECTIONNODE_TABLE = "CREATE TABLE `poeNodeCollectionNode` (" +
    		"`collectionid` INTEGER," +
    		"`nodeorder` INTEGER," +
    		"`nodeid` INTEGER," +
    		"PRIMARY KEY (collectionid,nodeorder));";
    
    public static Connection initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + sqlitedb);
            conn.setAutoCommit(false);
            createTable();
        } catch (SQLException ex) {
            logger.severe("SQL exception on initialize :");
            logger.severe("  " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            logger.severe("You need the SQLite/MySQL library. :");
            logger.severe("  " + ex.getMessage());
        }

        return conn;
    }
    
    public static Connection getConnection() {
        if (conn == null)
            conn = initialize();

        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
                logger.severe("Error on Connection close :");
                logger.severe("  " + ex.getMessage());
            }
        }
    }

    private static boolean tableExists(String name) {
        ResultSet rs = null;
        try {
            Connection conn = getConnection();

            DatabaseMetaData dbm = conn.getMetaData();
            rs = dbm.getTables(null, null, name, null);
            if (!rs.next())
                return false;
            return true;
        } catch (SQLException ex) {
            logger.severe("Table Check Exception :");
            logger.severe("  " + ex.getMessage());
            return false;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException ex) {
                logger.severe("Table Check SQL Exception (on closing) :");
                logger.severe("  " + ex.getMessage());
            }
        }
    }

    private static void createTable() {
        Statement st = null;
        try {
            Connection conn = getConnection();
            st = conn.createStatement();

            if (!tableExists("poeNode")) {
                st.executeUpdate(NODE_TABLE);
                conn.commit();
            }

            if (!tableExists("poeEffect")) {
                st.executeUpdate(EFFECT_TABLE);
                conn.commit();
            }

            if (!tableExists("poeNodeEffect")) {
                st.executeUpdate(NODEEFFECT_TABLE);
                conn.commit();
            }

            if (!tableExists("poeNeighborNode")) {
                st.executeUpdate(NEIGHBORNODE_TABLE);
                conn.commit();
            }

            if (!tableExists("poeNodeCollection")) {
                st.executeUpdate(NODECOLLECTION_TABLE);
                conn.commit();
            }

            if (!tableExists("poeNodeCollectionNode")) {
                st.executeUpdate(NODECOLLECTIONNODE_TABLE);
                conn.commit();
            }

        } catch (SQLException ex) {
            logger.severe("Create Table Exception :");
            logger.severe("  " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                logger.severe("Could not create the table (on close) :");
                logger.severe("  " + ex.getMessage());
            }
        }
    }
    
    
    public static Map<Short, POENode> getAllNodes() {
        HashMap<Short, POENode> nodes = new HashMap<>();
        Statement statementNode = null;
        PreparedStatement statementNeighbor = null;
        ResultSet setNodes = null;
        ResultSet setNeighbor = null;

        try {
            Connection conn = getConnection();

            statementNeighbor = conn.prepareStatement("SELECT neighbornodeid FROM poeNeighborNode WHERE nodeid = ?");
            
            statementNode = conn.createStatement();
            setNodes = statementNode.executeQuery("SELECT * FROM poeNode");
            //int size = 0;
            while (setNodes.next()) {
                //size++;
                short id = setNodes.getShort("id");
                String name = setNodes.getString("name");
                
                Set<Short> neighbors = new HashSet<>();
                
                statementNeighbor.setInt(1, id);
                setNeighbor = statementNeighbor.executeQuery();
                
                while (setNeighbor.next()) {
                	short neighborid = setNeighbor.getShort("neighbornodeid");
                	neighbors.add(neighborid);
                }
                
                POENode node = new POENode(neighbors);
                node.name = name;

                nodes.put(id, node);
            }
            //logger.info(" " + size + " nodes loaded");
        } catch (SQLException ex) {
            logger.severe("Load Exception :");
            logger.severe("  " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (statementNode != null)
                	statementNode.close();
                if (statementNeighbor != null)
                	statementNeighbor.close();
                if (setNodes != null)
                	setNodes.close();
                if (setNeighbor != null)
                	setNeighbor.close();
            } catch (SQLException ex) {
                logger.severe("Load Exception (on close) :");
                logger.severe("  " + ex.getMessage());
            }
        }
        return nodes;
    }
}
